package org.demo.banking.core;

public class Bank {
    public void transfer(BankAccount from, BankAccount to, int amount) {
        try {
            if (from.withdraw(amount))
                to.deposit(amount);
        } catch (Exception e) {
            System.err.println("Transfer failed: " + e.getMessage());
        }
    }
}
