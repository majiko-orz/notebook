package algorithm.string;

import java.util.ArrayList;
import java.util.List;

/**
 * 找到字符串中所有字母异位词
 * 438-中等
 */
public class FindAnagrams {

    // 时间复杂度：O(n+m+Σ)，其中 n 为字符串 s 的长度，m 为字符串 p 的长度，其中Σ 为所有可能的字符数
    // 空间复杂度：O(Σ)
    public List<Integer> findAnagrams(String s, String p) {
        int slen = s.length(), plen = p.length();
        if (slen < plen) {
            return new ArrayList<>();
        }

        List<Integer> list = new ArrayList<>();
        int[] count = new int[26];
        for (int i = 0; i < plen; i++) {
            count[s.charAt(i) - 'a']--;
            count[p.charAt(i) - 'a']++;
        }

        int differ = 0;
        for (int i = 0; i < 26; i++) {
            if (count[i] != 0) {
                differ++;
            }
        }

        if (differ == 0) {
            list.add(0);
        }

        for (int i = 0; i < slen - plen; i++) {
            if (count[s.charAt(i) - 'a'] == 1) {
                differ--;
            } else if (count[s.charAt(i) - 'a'] == 0) {
                differ++;
            }
            --count[s.charAt(i) - 'a'];

            if (count[s.charAt(i + plen) - 'a'] == -1) {
                differ--;
            } else if (count[s.charAt(i + plen) - 'a'] == 0) {
                differ++;
            }
            ++count[s.charAt(i + plen) - 'a'];

            if (differ == 0) {
                list.add(i + 1);
            }
        }
        return list;
    }
}
