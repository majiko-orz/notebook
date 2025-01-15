package algorithm.string;

/**
 * 最后一个单词的长度
 * 58-简单
 */
public class LengthOfLastWord {

    // 时间复杂度O(N)，空间复杂度O(1)
    public int lengthOfLastWord(String s) {
        char[] chars = s.toCharArray();
        int l = 0;
        for (int i = chars.length - 1; i >= 0; i--) {
            if (chars[i] != ' ') l++;
            if (chars[i] == ' ' && l != 0) break;
        }
        return l;
    }
}
