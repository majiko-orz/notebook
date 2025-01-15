package algorithm.array;

/**
 * 删除有序数组中的重复项
 * 26-简单
 */
public class RemoveDuplicates {

    // 时间复杂度O(n)，空间复杂度O(1)
    public int removeDuplicates(int[] nums) {
        int n = nums.length;
        if (n == 0) {
            return 0;
        }

        int read = 1, write = 1;
        while (read < n) {
            if (nums[read] != nums[read - 1]) {
                nums[write] = nums[read];
                write++;
            }
            read++;
        }

        return write;
    }
}
