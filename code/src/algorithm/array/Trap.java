package algorithm.array;

/**
 * 接雨水
 * 42-困难
 */
public class Trap {

    // 时间复杂度O(N)，空间复杂度O(1)
    public int trap(int[] height) {
        int left = 0, right = height.length - 1, res = 0;
        int leftMax = 0, rightMax = 0;
        while (left < right) {
            leftMax = Math.max(leftMax, height[left]);
            rightMax = Math.max(rightMax, height[right]);
            if (height[left] <  height[right]) {
                res += leftMax - height[left];
                left++;
            } else {
                res += rightMax - height[right];
                right--;
            }
        }
        return res;
    }
}
