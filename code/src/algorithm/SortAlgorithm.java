package algorithm;

import java.util.Arrays;

/**
 * 冒泡排序、选择排序、插入排序、希尔排序、归并排序、快速排序、堆排序
 */
public class SortAlgorithm {

    public static void main(String[] args) {
        //1 2 3 3 4 4 5 5 6 6 7 7 8 9
        int[] a = {3,5,2,6,1,7,9,4,8,4,6,3,7,5};
        sort(a);
        Arrays.stream(a).forEach(e -> System.out.print(e + " "));
    }

    // 1、冒泡排序
    public static void bubbleSort(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            for (int j = 0; j < a.length - i - 1; j++) {
                if (a[j] > a[j + 1]) {
                    exch(a, j, j+ 1);
                }
            }
        }
    }

    // 2、选择排序
    public static void selectSort(int[] a) {
        for (int i = a.length - 1; i > 0; i--) {
            int max = i;
            for (int j = 0; j < i; j++) {
                if (a[j] > a[max]) {
                    max = j;
                }
            }
            exch(a, i, max);
        }
    }

    // 3、插入排序
    public static void insertSort(int[] a) {
        for(int low = 1; low < a.length; low++) {
            int t = a[low];
            int i = low - 1;
            while(i >= 0 && t < a[i]) {
                a[i + 1] = a[i];
                i--;
            }
            if(i != low - 1) {
                a[i + 1] = t;
            }
        }
    }

    // 4、希尔排序
    public static void shellSort(int[] a) {
        for(int gap = a.length >> 1; gap >= 1; gap = gap >> 1) {
            for(int low = gap; low < a.length; low++) {
                int t = a[low];
                int i = low - gap;
                while(i >= 0 && t < a[i]) {
                    a[i + gap] = a[i];
                    i -= gap;
                }
                if(i != low - gap) {
                    a[i + gap] = t;
                }
            }
        }
    }

    // 5、归并排序,自顶向下
    public static void mergeSort(int[] a) {
        int[] aux = new int[a.length];
        mergeSort(a, 0, a.length - 1, aux);
    }

    public static void mergeSort(int[] a, int lo, int hi, int[] aux) {
        if (hi <= lo) {
            return;
        }
        int mid = lo + (hi - lo) / 2;
        mergeSort(a, lo, mid, aux);
        mergeSort(a, mid + 1, hi, aux);
        merge(a, lo, mid, hi, aux);
    }

    public static void merge(int[] a, int lo, int mid, int hi, int[] aux) {
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            aux[k] = a[k];
        }
        for (int k = lo; k <= hi; k++) {
            if (i > mid) {
                a[k] = aux[j++];
            } else if (j > hi) {
                a[k] = aux[i++];
            } else if (aux[j] < aux[i]) {
                a[k] = aux[j++];
            } else {
                a[k] = aux[i++];
            }
        }
    }

    // 归并排序,自底向上
    public static void mergeSort2(int[] a) {
        int N = a.length;
        int[] aux = new int[N];
        for (int sz = 1; sz < N; sz += sz) {
            for (int lo = 0; lo < N - sz; lo += sz + sz) {
                merge(a, lo, lo + sz - 1, Math.min(lo + sz + sz - 1, N - 1), aux);
            }
        }
    }

    // 6、快速排序
    public static void quickSort(int[] a) {
        quickSort(a, 0, a.length - 1);
    }

    private static void quickSort(int[] a, int l, int r) {
        if (l >= r) {
            return;
        }
        int p = partition(a, l, r);
        quickSort(a, l, p - 1);
        quickSort(a, p + 1, r);
    }

    private static int partition(int[] a, int l, int r) {
        int v = a[l];
        int i = l, j = r;
        while (i < j) {
            while (a[j] > v && i < j) {
                j--;
            }
            while (a[i] <= v && i < j) {
                i++;
            }
            exch(a, i, j);
        }
        exch(a, l, i);
        return i;
    }

    // 三向切分的快速排序
    public static void quickSort2(int a[]) {
        quickSort3(a, 0, a.length - 1);
    }

    public static void quickSort3(int[] a, int lo, int hi) {
        if (hi <= lo) {
            return;
        }
        int lt = lo, i = lo + 1, gt = hi;
        int v = a[lo];
        while (i <= gt) {
            if (a[i] < v) {
                exch(a, lt++, i++);
            } else if (a[i] > v) {
                exch(a, i, gt--);
            } else {
                i++;
            }
        }
        quickSort3(a, lo, lt - 1);
        quickSort3(a, gt + 1, hi);
    }

    // 7、堆排序
    public void heapSort(int[] a) {
        heapify(a, a.length);
        for(int right = a.length - 1; right > 0; right--) {
            exch(a, 0, right);
            down(a, 0, right);
        }
    }

    public void heapify(int[] array, int size) {
        // 找到最后一个非叶子节点 size / 2 - 1
        for(int i = size / 2 - 1; i >= 0; i--) {
            down(array, i, size);
        }
    }

    public void down(int[] array, int parent, int size) {
        while(true) {
            int left = parent * 2 + 1;
            int right = left + 1;
            int max = parent;
            if(left < size && array[left] > array[max]) {
                max = left;
            }
            if(right < size && array[right] > array[max]) {
                max = right;
            }
            if(max == parent) { // 没找到更大的孩子
                break;
            }
            exch(array, max, parent);
            parent = max;
        }
    }

    public static void exch(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    // 自测
    public static void sort(int a[]) {

    }
}
