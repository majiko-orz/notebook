package algorithm.array;

import java.util.HashMap;

/**
 * 两数之和
 * 1-简单
 */
public class TwoSum {

    // 时间复杂度O(N)，空间复杂度O(N)
    public int[] twoSum(int[] nums, int target) {
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
