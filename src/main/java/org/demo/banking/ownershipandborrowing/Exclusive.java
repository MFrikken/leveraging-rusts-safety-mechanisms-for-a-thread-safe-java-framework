package org.demo.banking.ownershipandborrowing;

public class Exclusive<T> {
    private final T value;
    private boolean isMutablyBorrowed = false;
    private int immutableBorrowCount = 0;

    public Exclusive(T value) {
        this.value = value;
    }

    public synchronized BorrowGuard<T> borrow() {
        while (isMutablyBorrowed) {
            try {
                System.out.println(String.format("Waiting for borrow ... (%s)", this.value));
                wait(); // block until reference will be released
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        immutableBorrowCount++;
        return new BorrowGuard<>(this, value);
    }

    public synchronized BorrowGuard<T> borrowMut() {
        if (isMutablyBorrowed || immutableBorrowCount > 0) {
            try {
                System.out.println(String.format("Waiting for borrow ... (%s)", this.value));
                wait(); // block until reference will be released
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        isMutablyBorrowed = true;
        return new BorrowGuard<>(this, value);
    }

    public synchronized void release() {
        if (isMutablyBorrowed) {
            isMutablyBorrowed = false;
        } else if (immutableBorrowCount > 0) {
            immutableBorrowCount--;
        }
        notifyAll(); // notify waiting threads about the released reference
    }
}
