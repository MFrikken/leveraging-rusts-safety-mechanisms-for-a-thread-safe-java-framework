package org.demo.banking.ownershipandborrowing;

public class BorrowUtils {
    public static <T1, T2> Pair<BorrowGuard<T1>, BorrowGuard<T2>> borrowMutPair(Exclusive<T1> a, Exclusive<T2> b) {
        Exclusive<?> first = a;
        Exclusive<?> second = b;

        if (System.identityHashCode(a) == System.identityHashCode(b)) {
            first = b;
            second = a;
        }

        synchronized (first) {
            synchronized (second) {
                try {
                    BorrowGuard<T1> guard1 = a.borrowMut();
                    BorrowGuard<T2> guard2 = b.borrowMut();
                    return new Pair<>(guard1, guard2);
                }  catch (Exception e) {
                    throw new RuntimeException("Failed to borrow both references", e);
                }
            }
        }
    }

    public static void releaseBoth(Exclusive<?> a, Exclusive<?> b) {
        a.release();
        b.release();
    }
}
