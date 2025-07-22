package org.demo.banking.ownershipandborrowing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

public class BorrowUtils {
    public static GuardGroup borrowMutAll(Exclusive<?>... exclusives) {
        if (exclusives == null || exclusives.length == 0) { throw new IllegalArgumentException("Input must not be null or empty"); }

        // Create sorted list for deterministic locking
        List<Exclusive<?>> sorted = Arrays.asList(Arrays.copyOf(exclusives, exclusives.length));
        sorted.sort(Comparator.comparingInt(System::identityHashCode));

        return synchronizeAll(sorted, () -> {
            List<BorrowGuard<?>> guards = new ArrayList<>();
            for (Exclusive<?> exclusive : exclusives) {
                guards.add(exclusive.borrowMut());
            }
            return new GuardGroup(guards);
        });
    }

    private static <T> T synchronizeAll(List<Exclusive<?>> locks, Supplier<T> body) {
        return synchronizeRecursively(locks, 0, body);
    }

    private static <T> T synchronizeRecursively(List<Exclusive<?>> locks, int index, Supplier<T> body) {
        if (index >= locks.size()) {
            return body.get();
        }

        synchronized (locks.get(index)) {
            return synchronizeRecursively(locks, index + 1, body);
        }
    }

    public static void releaseAll(Exclusive<?>... exclusives) {
        for (Exclusive<?> exclusive : exclusives) {
            exclusive.release();
        }
    }
}
