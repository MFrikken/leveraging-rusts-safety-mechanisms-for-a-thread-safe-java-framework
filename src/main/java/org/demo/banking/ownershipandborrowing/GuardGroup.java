package org.demo.banking.ownershipandborrowing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GuardGroup implements AutoCloseable{
    private final List<BorrowGuard<?>> guards;

    public GuardGroup(List<BorrowGuard<?>> guards) {
        this.guards = Collections.unmodifiableList(new ArrayList<>(guards));
    }

    public GuardGroup(BorrowGuard<?>... guards) {
        this(Arrays.asList(guards));
    }

    public <T> BorrowGuard<T> get(int index) {
        return (BorrowGuard<T>) guards.get(index);
    }

    public int size() {
        return guards.size();
    }

    @Override
    public void close() {
        for (BorrowGuard<?> guard : guards) {
            guard.close();
        }
    }
}
