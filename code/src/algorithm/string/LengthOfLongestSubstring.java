package algorithm.string;

import java.util.HashSet;

/**
 * 无重复字符的最长子串
 * 3-中等
 */
public class LengthOfLongestSubstring {

    // 时间复杂度O(N)，空间复杂度O(∣Σ∣)，其中 Σ 表示字符集（即字符串中可以出现的字符），∣Σ∣ 表示字符集的大小
    public int lengthOfLongestSubstring(String s) {
        HashSet<Character> set = new HashSet<>();
        int right = 0, max = 0;
        for (int i = 0; i < s.length(); i++) {
            if (i != 0) {
                set.remove(s.charAt(i - 1));
            }

            while (right < s.length()) {
                if (!set.contains(s.charAt(right))) {
                    set.add(s.charAt(right));
                    right++;
                } else {
                    break;
                }
            }
            max = Math.max(max, right - i);
        }
        return max;
    }
}
