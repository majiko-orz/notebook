package array;

/**
 * 移除元素
 * 27-简单
 */
public class RemoveElement {

    // 时间复杂度O(n)，空间复杂度O(1)
    public int removeElement(int[] nums, int val) {
        int k = nums.length - 1;
        int n = 0;
        while (n <= k) {
            if (nums[n] == val) {
                int temp = nums[n];
                nums[n] = nums[k];
                nums[k] = temp;
                k--;
            } else {
                n++;
            }
        }

        return n;
    }
}
