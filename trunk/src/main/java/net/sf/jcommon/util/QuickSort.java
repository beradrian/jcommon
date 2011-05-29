package net.sf.jcommon.util;

/**
 * @author Adrian BER
 */
public class QuickSort {

    private QuickSort() {}

    private static void quicksort(int[] a, int p, int q, IntComparator comparator) {
        // partition
        int x = a[p];
        int i = p + 1;
        int j = q;
        int val1, val2;
        while (i <= j) {
            val1 = (comparator == null ? a[i] - x : comparator.compare(a[i], x));
            if (val1 <= 0)
                i++;
            val2 = (comparator == null ? a[j] - x : comparator.compare(a[j], x));
            if (val2 >= 0)
                j--;
            if (val1 > 0 && val2 < 0 && i < j) {
                int aux = a[i];
                a[i] = a[j];
                a[j] = aux;
                i++;
                j--;
            }
        }

        int k = i - 1;
        a[p] = a[k];
        a[k] = x;

        // recursive sort
        if (p < k - 1)
            quicksort(a, p, k - 1, comparator);
        if (k + 1 < q)
            quicksort(a, k + 1, q, comparator);
    }

    public static void quicksort(int[] a, IntComparator comparator) {
        if (a.length > 1)
            quicksort(a, 0, a.length - 1, comparator);
    }
}
