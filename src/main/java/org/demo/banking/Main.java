package org.demo.banking;

import org.demo.banking.core.Bank;
import org.demo.banking.core.BankAccount;
import org.demo.banking.ownershipandborrowing.BorrowGuard;
import org.demo.banking.ownershipandborrowing.BorrowUtils;
import org.demo.banking.ownershipandborrowing.Exclusive;
import org.demo.banking.ownershipandborrowing.GuardGroup;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Exclusive<BankAccount> accA = new Exclusive<>(new BankAccount("A", 1000));
        Exclusive<BankAccount> accB = new Exclusive<>(new BankAccount("B", 1000));

        Bank bank = new Bank();

        Runnable transferTask = () -> {
            // aquire both ownerships at once to prevent deadlocks
            try (GuardGroup multipleGuards = BorrowUtils.borrowMutAll(accA, accB)) {
                BankAccount from = (BankAccount) multipleGuards.get(0).get();
                BankAccount to = (BankAccount) multipleGuards.get(1).get();
                bank.transfer(from, to, 600);
            } catch (Exception e) {
                System.err.println("Transfer: " + e.getMessage());
            }
        };

        Thread t1 = new Thread(transferTask);
        Thread t2 = new Thread(transferTask);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        // Read balances
        try (
                BorrowGuard<BankAccount> a = accA.borrow();
                BorrowGuard<BankAccount> b = accB.borrow();
        ) {
            System.out.println("A: " + a.get().getBalance());
            System.out.println("B: " + b.get().getBalance());
        }
    }
}