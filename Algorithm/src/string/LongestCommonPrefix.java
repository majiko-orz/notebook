package string;

/**
 * 最长公共前缀
 * 14-简单
 */
public class LongestCommonPrefix {

    // 时间复杂度O(mn)，空间复杂度O(1)
    public String longestCommonPrefix(String[] strs) {
        String longPrefix = "";
        for(int i = 1; i <= strs[0].length(); i++) {
            String prefix = strs[0].substring(0, i);
            for(int j = 0; j < strs.length; j++) {
                if (!strs[j].startsWith(prefix)) {
                    return longPrefix;
                }
            }
            longPrefix = prefix;
        }
        return longPrefix;
    }
}
