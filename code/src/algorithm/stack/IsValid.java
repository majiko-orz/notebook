package algorithm.stack;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * 有效的括号
 * 20-简单
 */
public class IsValid {

    // 时间复杂度：O(n)，空间复杂度：O(n+∣Σ∣)
    public boolean isValid(String s) {
        int n = s.length();
        if (n % 2 == 1) {
            return false;
        }

        HashMap<Character, Character> map = new HashMap<Character, Character>() {{
            put(')', '(');
            put('}', '{');
            put(']', '[');
        }};

        Deque<Character> stack = new LinkedList<>();

        for (int i = 0; i < n; i++) {
            Character c = s.charAt(i);
            if (map.containsKey(c)) {
                if (stack.isEmpty() || stack.peek() != map.get(c)) {
                    return false;
                }
                stack.pop();
            } else {
              stack.push(c);
            }
        }

        return stack.isEmpty();
    }
}
