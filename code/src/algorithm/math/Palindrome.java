package algorithm.math;

/**
 * 回文数
 * 9-简单
 */
public class Palindrome {

    // 时间复杂度O(logN)，空间复杂度O(1)
    public boolean isPalindrome(int x) {
        if(x < 0 || (x % 10 == 0 && x != 0)){
            return false;
        }

        int revertNumber = 0;
        while(x > revertNumber) {
            revertNumber = revertNumber * 10 + x % 10;
            x /= 10;
        }

        return x == revertNumber || x == revertNumber / 10;
    }
}
