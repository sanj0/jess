package de.sanj0.jess;

import java.util.ArrayList;
import java.util.List;

/**
 * static utility functions
 */
public class JessUtils {
    // O(n)
    // throws IllegalArgumentException when from-to range is illogical
    public static List<Integer> intArrayToList(final int[] ints, final int from, final int to) {
        final int arrayLength = ints.length;
        if (from < 0 || to < from || to > arrayLength) {
            throw new IllegalArgumentException("from-to range must be logical");
        }
        final List<Integer> list = new ArrayList<>(arrayLength);
        for (int i = from; i < to; i++) {
            list.add(ints[i]);
        }

        return list;
    }
}
