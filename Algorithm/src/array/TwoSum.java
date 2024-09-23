package array;

import java.util.HashMap;

/**
 * 两数之和
 * 1-简单
 */
public class TwoSum {

    // O(n²)
    public int[] twoSum1(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[]{};
    }

    // O(n²)
    public int[] twoSum2(int[] nums, int target) {
        HashMap<Integer, Integer> hashMap = new HashMap<>(nums.length - 1);
        for (int i = 0; i < nums.length; i++) {
            if (hashMap.containsKey(target - nums[i])) {
                return new int[]{i, hashMap.get(target - nums[i])};
            }
            hashMap.put(nums[i], i);
        }
        return new int[]{};
    }
}
