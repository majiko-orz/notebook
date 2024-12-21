package math;

import java.util.HashMap;

/**
 * 罗马数字转整数
 * 13-简单
 */
public class RomanToInt {

    // 时间复杂度O(N)，空间复杂度O(1)
    public int romanToInt(String s) {
        HashMap<Character, Integer> map = new HashMap<>();
        map.put('I', 1);
        map.put('V', 5);
        map.put('X', 10);
        map.put('L', 50);
        map.put('C', 100);
        map.put('D', 500);
        map.put('M', 1000);

        int value = 0;
        int l = s.length();
        for (int i = 0; i < l; i++) {
            int v = map.get(s.charAt(i));
            if (i < l - 1 && v < map.get(s.charAt(i + 1))) {
                value -= v;
            } else {
                value += v;
            }
        }

        return value;
    }
}
