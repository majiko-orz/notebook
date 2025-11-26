package algorithm.array;

/**
 * 移动零
 * 283-简单
 */
public class MoveZeroes {

    // 时间复杂度O(N)，空间复杂度O(1)
    public void moveZeroes(int[] nums) {
        int n = nums.length, left = 0, right = 0;
        while (right < n) {
            if (nums[right] != 0) {
                int temp = nums[left];
                nums[left] = nums[right];
                nums[right] = temp;
                left++;
            }
            right++;
        }
    }
}
