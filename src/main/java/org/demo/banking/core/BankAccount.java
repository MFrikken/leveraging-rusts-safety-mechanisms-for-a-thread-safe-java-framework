package org.demo.banking.core;

public class BankAccount {
    private final String name;
    private double balance;

    public BankAccount(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    public double getBalance() {
        return this.balance;
    }

    public void deposit(double amount) {
        this.balance += amount;
    }

    public boolean withdraw(double amount) {
        if (this.balance < amount) { throw new RuntimeException("Insufficient balance"); }
        this.balance -= amount;
        return true;
    }

    @Override
    public String toString() {
        return "Account: " + this.name;
    }
}
