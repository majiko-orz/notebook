import java.util.Arrays;

/**
 * 冒泡排序、选择排序、插入排序、希尔排序、归并排序、快速排序
 */
public class SortAlgorithm {

    public static void main(String[] args) {
        //1 2 3 3 4 4 5 5 6 6 7 7 8 9
        int[] a = {3,5,2,6,1,7,9,4,8,4,6,3,7,5};
        sort(a);
        Arrays.stream(a).forEach(e -> System.out.print(e + " "));
    }

    //冒泡排序
    public static void bubbleSort(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            for (int j = 0; j < a.length - i - 1; j++) {
                if (a[j] > a[j + 1]) {
                    int temp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = temp;
                }
            }
        }
    }

    //选择排序
    public static void selectSort(int[] a) {
        for (int i = 0; i < a.length; i++) {
            int min = i;
            for (int j = i + 1; j < a.length; j++) {
                if (a[min] > a[j]) {
                    min = j;
                }
            }
            int temp = a[i];
            a[i] = a[min];
            a[min] = temp;
        }
    }

    //插入排序
    public static void insertSort(int[] a) {
        for (int i = 1; i < a.length; i++) {
            for (int j = i; j > 0 && a[j] < a[j - 1]; j--) {
                int temp = a[j];
                a[j] = a[j - 1];
                a[j - 1] = temp;
            }
        }
    }

    //希尔排序
    public static void shellSort(int[] a) {
        int step = a.length / 2;
        while (step > 0) {
            for (int i = step; i < a.length; i++) {
                for (int j = i; j - step >= 0 && a[j] < a[j - step]; j -= step) {
                    int temp = a[j];
                    a[j] = a[j - step];
                    a[j - step] = temp;
                }
            }
            step = step / 2;
        }
    }

    //归并排序,自顶向下
    public static void mergeSort(int[] a) {
        int[] aux = new int[a.length];
        mergeSort(a, 0, a.length - 1, aux);
    }

    public static void mergeSort(int[] a, int lo, int hi, int[] aux) {
        if (hi <= lo) return;
        int mid = lo + (hi - lo) / 2;
        mergeSort(a, lo, mid, aux);
        mergeSort(a, mid + 1, hi, aux);
        merge(a, lo, mid, hi, aux);
    }

    public static void merge(int[] a, int lo, int mid, int hi, int[] aux) {
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) aux[k] = a[k];
        for (int k = lo; k <= hi; k++) {
            if (i > mid) a[k] = aux[j++];
            else if (j > hi) a[k] = aux[i++];
            else if (aux[j] < aux[i]) a[k] = aux[j++];
            else a[k] = aux[i++];
        }
    }

    //归并排序,自底向上
    public static void mergeSort2(int[] a) {
        int N = a.length;
        int[] aux = new int[N];
        for (int sz = 1; sz < N; sz += sz)
            for (int lo = 0; lo < N - sz; lo += sz + sz)
                merge(a, lo, lo + sz - 1, Math.min(lo + sz + sz - 1, N - 1), aux);
    }

    //快速排序
    public static void quickSort(int[] a) {
        quickSort(a, 0, a.length - 1);
    }

    public static void quickSort(int[] a, int lo, int hi) {
        if (hi <= lo) return;;
        int j = partition(a, lo, hi);
        quickSort(a, lo, j - 1);
        quickSort(a, j + 1, hi);
    }

    public static int partition(int[] a, int lo, int hi) {
        int i = lo, j = hi + 1;
        int v = a[lo];
        while (true) {
            while (a[++i] < v) if (i == hi) break;
            while (v < a[--j]) if (j == lo) break;
            if (i >= j) break;
            int temp = a[i];
            a[i] = a[j];
            a[j] = temp;
        }
        int temp = a[lo];
        a[lo] = a[j];
        a[j] = temp;
        return j;
    }

    //三向切分的快速排序
    public static void quickSort2(int a[]) {
        quickSort3(a, 0, a.length - 1);
    }

    public static void quickSort3(int[] a, int lo, int hi) {
        if (hi <= lo) return;
        int lt = lo, i = lo + 1, gt = hi;
        int v = a[lo];
        while (i <= gt) {
            if (a[i] < v) exch(a, lt++, i++);
            else if (a[i] > v) exch(a, i, gt--);
            else i++;
        }
        quickSort3(a, lo, lt - 1);
        quickSort3(a, gt + 1, hi);
    }

    public static void exch(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    //
    public static void sort(int a[]) {

    }
}
