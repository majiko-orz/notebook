package algorithm.string;

/**
 * 找出字符串中第一个匹配项的下标
 * 28-简单
 */
public class StrStr {

    // kmp算法, 时间复杂度O(m + n)，空间复杂度O(m), n 是字符串 haystack 的长度，m 是字符串 needle 的长度
    public int strStr(String haystack, String needle) {
        char[] h = haystack.toCharArray();
        char[] n = needle.toCharArray();
        int hl = h.length, nl = n.length, i = 0, j = 0;
        int[] next = new int[nl];
        next[0] = -1;
        if (nl > 1) {
            next[1] = 0;
            //k表示当前要求next值的位置
            //cn表示当前要和前一个字符比对的下标
            int k = 2, cn = 0;
            while (k < nl) {
                if (n[k - 1] == n[cn]) {
                    next[k++] = ++cn;
                } else if (cn > 0) {
                    cn = next[cn];
                } else {
                    next[k++] = 0;
                }
            }
        }

        while (i < hl && j < nl) {
            if (h[i] == n[j]) {
                i++;
                j++;
            } else if (j == 0) {
                i++;
            } else {
                j = next[j];
            }
        }

        return j == nl ? i - j : -1;
    }
}
