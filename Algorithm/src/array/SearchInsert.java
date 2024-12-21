package array;

/**
 * 搜索插入位置
 * 35-简单
 */
public class SearchInsert {

    // 时间复杂度O(logN)，空间复杂度O(1)
    public int searchInsert(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (target > nums[mid]) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return left;
    }
}
