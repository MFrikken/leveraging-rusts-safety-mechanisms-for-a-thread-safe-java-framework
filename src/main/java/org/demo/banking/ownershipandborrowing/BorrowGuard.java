package org.demo.banking.ownershipandborrowing;

public class BorrowGuard<T> implements AutoCloseable {
    private final Exclusive<T> reference;
    private final T value;

    public BorrowGuard(Exclusive<T> reference, T value) {
        this.reference = reference;
        this.value = value;
    }

    public T get(){
        return value;
    }

    @Override
    public void close() {
        reference.release();
    }
}
