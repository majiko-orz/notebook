package algorithm.array;

/**
 * 盛最多水的容器
 * 11-中等
 */
public class MaxArea {

    // 时间复杂度O(N)，空间复杂度O(1)
    public int maxArea(int[] height) {
        int left = 0, right = height.length - 1, max = 0;
        while (left < right) {
            int area = Math.min(height[left], height[right]) * (right - left);
            max = Math.max(max, area);
            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        return max;
    }
}
