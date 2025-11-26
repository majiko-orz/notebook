## 数据结构与算法

### 二分查找

```java
// 基础版 查找不存在的数时可能会导致死循环
// a待查找的升序数组，target待查找的目标值，找到返回索引，找不到返回-1
 
public int binarySearch(int[] a, int target) {
    int i = 0, j = a.length - 1;
    while (i <= j) {
        // int m = (i + j) / 2; 数据量大可能为负数
        int m = (i + j) >>> 1;
        if (a[m] < target) i = m + 1;
        else if (target < a[m]) j = m - 1;
        else return m;
    }
    
    return -1;
}

// 改进版
public int binarySearch(int[] a, int target) {
    int i = 0, j = a.length;
    while (i < j) {
        int m = (i + j) >>> 1;
        if (a[m] < target) i = m + 1;
        else if (target < a[m]) j = m;
        else return m;
    }
    
    return -1;
}

// 平衡版 循环内的平均比较次数减少了
public int binarySearch(int[] a, int target) {
    int i = 0, j = a.length;
    while (0 < j - i) {
        int m = (i + j) >>> 1;
        if (target < a[m]) j = m;
        else i = m;
    }
    
    if (a[i] == target) {
        return i;
    } else {
        return -1;
    }
}

// Leftmost，重复的取最左侧
public int binarySearch(int[] a, int target) {
    int i = 0, j = a.length - 1;
    int candidate = -1;
    while (i <= j) {
        int m = (i + j) >>> 1;
        if (a[m] < target) i = m + 1;
        else if (target < a[m]) j = m - 1;
        else {
            candidate = m;
            j = m - 1;
        };
    }
    
    return candidate;
}

// Rightmost，重复的取最右侧
public int binarySearch(int[] a, int target) {
    int i = 0, j = a.length - 1;
    int candidate = -1;
    while (i <= j) {
        int m = (i + j) >>> 1;
        if (a[m] < target) i = m + 1;
        else if (target < a[m]) j = m - 1;
        else {
            candidate = m;
            i = m + 1;
        };
    }
    
    return candidate;
}

// Leftmost，返回大于等于目标的最靠左索引
public int binarySearch(int[] a, int target) {
    int i = 0, j = a.length - 1;
    while (i <= j) {
        int m = (i + j) >>> 1;
        if (target <= a[m]) j = m - 1;
        else i = m + 1;
    }
    
    return i;
}

// Rightmost，返回小于等于目标的最靠右索引
public int binarySearch(int[] a, int target) {
    int i = 0, j = a.length - 1;
    while (i <= j) {
        int m = (i + j) >>> 1;
        if (a[m] <= target) i = m + 1;
        else j = m - 1;
    }
    
    return i - 1;
}
```

### 数组

数组是由一组元素（值或变量）组成的数据结构，每个元素有至少一个索引或键来标识

知道数组起始地址BaseAddress，就可以由公式BaseAddress + i * size计算出索引i元素的地址

+ i即索引，在Java、C等语言都是从0开始
+ size是每个元素占用字节，例如int占4

Java中数组结构

+ 8字节markword
+ 4字节class指针（压缩class指针的情况）
+ 4字节数组大小（决定了数组最大容量是2的32次方）
+ 数组元素 + 对齐字节（Java中所有对象大小都是8字节的整数倍，不足的要用对齐字节补足）

**动态数组**

参考ArrayList

**二维数组**

```java
int[][] array = {
    {11,12,13,14,15},
    {21,22,23,24,25},
    {31,32,33,34,35},
};
```

局部性原理

+ cpu读取内存（速度慢）数据后，会将其放入高速缓存（速度快）当中，如果后来的计算再用到此数据，在缓存中能读到的话，就不必读内存了
+ 缓存最小存储单位是缓存行（cache line），一般是64bytes，一次读的数据少了不划算，因此最少读64bytes填满一个缓存行，因此读入某个数据时，也会读取其临近的数据，这就是所谓的空间局部性

因此二维数组按行遍历效率高于按列遍历

### 链表

链表是数据元素的线性集合，其每个元素都指向下一个元素，元素存储上并不连续

+ 单向链表，每个元素只知道其下一个元素是谁
+ 双向链表，每个元素知道其上一个和下一个元素
+ 循环链表，通常的链表尾节点tail指向的都是null，而循环链表的tail指向的是头节点head

链表中还有一种特殊的节点称为哨兵节点，也叫做哑元节点，它不存储数据，通常用作头尾，用来简化边界判断

### 递归

```java
// 二分查找
private static int f(int[] a, int target, int i, int j) {
    if(i > j) {
        return -1;
    }
    
    int m = (i + j) >>> 1;
    if(target < a[m]) {
        return f(a, target, i, m - 1);
    } else if(target > a[m]) {
        return f(a, target, m + 1, j);
    } else {
        return m;
    }
}

// 斐波那契数列，递归调用次数 2*f(n+1)-1，时间复杂度O(1.618的n次方)
public static int f(int n) {
    if(n == 0) {
        return 0;
    }
    
    if(n == 1) {
        return 1;
    }
    
    int x = f(n - 1);
    int y = f(n - 2);
    return x + y;
}

// 斐波那契数列优化，记忆法，时间复杂度O(n)
public static int fibonacci(int n) {
    int[] cache = new int[n + 1];
    Arrays.fill(cache, -1);
    cache[0] = 0;
    cache[1] = 1;
    return f(n, cache);
}

public static int f(int n) {
    if(cache[n] != -1) {
        return cache[n];
    }
    
    int x = f(n - 1, cache);
    int y = f(n - 2, cache);
    cache[n] = x + y;
    return cache[n];
}

// 汉诺塔
public class HanoiTower {
    static LinkedList<Integer> a = new LinkedList<>();
    static LinkedList<Integer> b = new LinkedList<>();
    static LinkedList<Integer> c = new LinkedList<>();
    
    static void init(int n) {
        for(int i = n; i >= 1; i--) {
            a.addLast(i);
        }
    }
    
    // n: 圆盘个数
    // a: 源
    // b: 借
    // c: 目
    static void move(int n, 
                     LinkedList<Integer> a,
                     LinkedList<Integer> b,
                     LinkedList<Integer> c) {
        move(n - 1, a, c, b);
        c.addLast(a.removeLast());
        move(n - 1, b, a, c);
    }
    
    public static void main(String[] args) {
        init(3);
        move(3, a, b, c);
    }
}

// 杨辉三角
public class PascalTriangle {
    private static int element(int i, int j) {
        if(j == 0 || i == j) {
            return 1;
        }
        return element(i - 1, j - 1) + element(i - 1, j);
    }
    
    private static void printSpace(int n, int i) {
    	int num = (n - 1 - i) * 2;
        for(int j = 0; j < m; j++) {
            System.out.print(" ");
        }
    }
    
    public static void print(int n) {
        for(int i = 0; i < n; i++) {
            printSpace(n, i);
            for(int j = 0; j <= i j++) {
                System.out.printf("%-4d", element(i, j));
            }
            System.out.println();
        }
    }
    
    public static void main(String[] args) {
        print(5);
    }
    
    
    // 二维数组记忆法优化
    private static int element1(int[][] triangle, int i, int j) {
        if(triangle[i][j] > 0) {
            return triangle[i][j];
        }
        
        if(j == 0 || i == j) {
            triangle[i][j] = 1;
            return 1;
        }
        triangle[i][j] = element1(i - 1, j - 1) + element1(i - 1, j);
        return triangle[i][j];
    }
    
    public static void print1(int n) {
        int[][] triangle = new int[n][];
        for(int i = 0; i < n; i++) {
            triangle[i] = new int[i + 1];
            printSpace(n, i);
            for(int j = 0; j <= i j++) {
                System.out.printf("%-4d", element1(triangle, i, j));
            }
            System.out.println();
        }
    }
    
    // 一维数组记忆法优化
    private static void createRow(int[] row, int i) {
        if(i == 0) {
            row[0] = 1;
            return;
        }
        
        for(int j = i; j > 0, j--) {
            row[j] = row[j] + row[j - 1];
        }
    }
    
    public static void print2(int n) {
        int[] row = new int[n];
        for(int i = 0; i < n; i++) {
            createRow(row, i);
            printSpace(n, i);
            for(int j = 0; j <= i j++) {
                System.out.printf("%-4d", row[j]);
            }
            System.out.println();
        }
    }
}
```

### 队列

以顺序的方式维护的一组数据集合，在一端添加数据，从另一端移除数据，添加的一端称为尾，移除的一端称为头

### 栈

栈是一种线性的数据结构，只能在其一端添加数据和移除数据，这一端称为栈顶，另一端不能操作数据的称之为栈底

### 堆

堆是一种基于树的结构，通常用完全二叉树实现

堆的特性：

+ 在大顶堆中，任意节点C与它的父节点P符合P.value ≥ C.value
+ 在小顶堆中，任意节点C与它的父节点P符合P.value ≤ C.value
+ 最顶层的节点（没有父亲）称之为root根节点

如果从索引0开始存储节点数据

+ 节点i的父节点为floor((i - 1) / 2)，当i > 0时
+ 节点i的左子节点为2i + 1，右子节点为2i + 2，当然它们得 < size

如果从索引1开始存储节点数据

+ 节点i的父节点为floor(i / 2)，当i > 1时
+ 节点i的左子节点为2i，右子节点为2i + 1，同样得 < size

```java
// 大顶堆
public class MaxHeap {
    int[] array;
    int size;
    
    public MaxHeap(int capacity) {
        this.array = new int[capacity];
    }
    
    public MaxHeap(int[] array) {
        this.array = array;
        this.size = array.lenth;
        heapify();
    }
    
    // 建堆
    private void heapify() {
        // 找到最后一个非叶子节点 size / 2 - 1
        for(int i = size / 2 - 1; i >= 0; i--) {
            down(i);
        }
    }
    
    // 获取堆顶元素
    public int peek() {
        return array[0];
    }
    
    // 删除堆顶元素
    public int poll(){
        int top = array[0];
        swap(0, size - 1);
        size--;
        down(0);
        return top;
    }
    
    // 删除指定索引处元素
    public int poll(int index) {
        int deleted = array[index];
        swap(index, size - 1);
        size--;
        down(index);
        return deleted;
    }
    
    // 替换堆顶元素
    public void replace(int replaced) {
        array[0] = replaced;
        down(0);
    }
    
    // 堆的尾部添加元素
    public boolean offer(int offered) {
        if(size == array.length) {
            return false;
        }
        up(offered);
        size++;
        return true;
    }
    
    // 将inserted元素上浮，直至offered小于父元素或到堆顶
    private void up(int offered) {
        int child = size;
        while(child > 0) {
            int parent = (child - 1) / 2;
            if(offered > array[parent]) {
                array[child] = array[parent];
            } else {
                break;
            }
            child = parent;
        }
        array[child] = offered;
    }
    
    // 将parent索引处的元素下潜，与两个孩子较大者交换，直至没孩子或孩子没它大
    private void down(int parent) {
        int left = parent * 2 + 1;
        int right = left + 1;
        int max = parent;
        if(left < size && array[left] > array[max]) {
            max = left;
        }
        if(right < size && array[right] > array[max]) {
            max = right;
        }
        if(max != parent) {
            swap(max, parent);
            down(max);
        }
    }
    
    // 交换两个索引处的元素
    private void swap(int i, int j) {
        int t = array[i];
        array[i] = array[j];
        array[j] = t;
    }
}
```

### 二叉树

#### 二叉树

##### 遍历

广度优先遍历（Breadth-first order）：尽可能先访问距离根最近的节点，也成为层序遍历

深度优先遍历（Depth-first order）：对于二叉树，可以进一步分为三种

+ pre-order前序遍历：对于每一棵子树，先访问该节点，然后是左子树，最后是右子树
+ in-order中序遍历：对于每一棵子树，先访问左子树，然后是该节点，最后是右子树
+ post-order后序遍历：对于每一棵子树，先访问左子树，然后是右子树，最后是该节点

```java
public class TreeNode {
    public int val;
    public TreeNode left;
    public TreeNode right;
    
    public TreeNode(int val) {
        this.val = val;
    }
    
    public TreeNode(TreeNode left, int val, TreeNode right) {
        this.left = left;
        this.val = val;
        this.right = right;
    }
    
    @Override
    public String toString() {
        return String.valueOf(this.val);
    }
}

public class TreeTraversal {
    public static void main(String[] args) {
        /*
               1
              / \
             2   3
            /   / \
           4   5   6
        */
        TreeNode root = new TreeNode(
            new TreeNode(new TreeNode(4), 2, null), 
            1,
            new TreeNode(new TreeNode(5), 3, new TreeNode(6))
        );
        
        LinkedListStack<TreeNode> stack = new LinkedListStack<>();
        TreeNode curr = root;
        
        // 非递归遍历，分开
        while(curr != null || !stack.isEmpty()) {
            if(curr != null) {
                System.out.println("前：" + curr.val);
                stack.push(curr);
                curr = curr.left;         
            } else {
                TreeNode pop = stack.pop();
                System.out.println("中：" + pop.val);
                curr = pop.right;
            }
        }
        
        // 后序
        TreeNode pop = null;
        while(curr != null || !stack.isEmpty()) {
            if(curr != null) {
                stack.push(curr);
                curr = curr.left;
            } else {
                TreeNode peek = stack.peek();
                if(peek.right == null || peek.right = pop) {
                    pop = stack.pop();
                	System.out.println("后：" + pop.val);
                } else {
                    curr = peek.right;
                }
            }
        }
        
        // 非递归遍历，合并
        TreeNode pop = null;								
        while(curr != null || !stack.isEmpty()) {
            if(curr != null) {
                stack.push(curr);
                // 待处理左子树
                System.out.println("前：" + curr.val);
                curr = curr.left;
            } else {
                TreeNode peek = stack.peek();
                // 没有右子树
                if(peek.right == null)) {
                    System.out.println("中：" + pop.val);
                    pop = stack.pop();
                	System.out.println("后：" + pop.val);
                } 
                // 右子树处理完成
                else if(peek.right == pop) {
                    pop = stack.pop();
                	System.out.println("后：" + pop.val);
                 } 
                // 待处理右子树
                else {
                    System.out.println("中：" + pop.val);
                    curr = peek.right;
                }
            }
        }
    }
    
    // 前序遍历
    static void preOrder(TreeNode node) {
        if(node == null) {
            return;
        }
        System.out.println(node.val + "\t");
        preOrder(node.left);
        preOrder(node.right);
    }
    
    // 中序遍历
    static void inOrder(TreeNode node) {
        if(node == null) {
            return;
        }
        inOrder(node.left);
        System.out.println(node.val + "\t");
        inOrder(node.right);
    }
    
    // 后序遍历
    static void postOrder(TreeNode node) {
        if(node == null) {
            return;
        }
        postOrder(node.left);
        postOrder(node.right);
        System.out.println(node.val + "\t");
    }
}
```

##### 对称二叉树

```java
// 判断是否是对称二叉树
public boolean isSymmetric(TreeNode root) {
    return check(root.left, root.right);
}

public boolean check(TreeNode left, TreeNode right) {
    if(left == null && right == null) {
        return true;
    }
    
    if(right == null || left == null) {
        return false;
    }
    
    if(left.val != right.val) {
        return false;
    }
    return check(left.left, right.right) && check(left.right, right.left);
}
```

##### 最大深度

```java
// 递归
public int maxDepth(TreeNode node) {
    if(node == null) {
        return 0;
    }
    int d1 = maxDepth(node.left);
    int d2 = maxDepth(node.right);
    return Integer.max(d1, d2) + 1;
}

// 后序遍历
public int maxDepth(TreeNode node) {
	TreeNode curr = root;
    TreeNode pop = null;
    LinkedList<TreeNode> stack = new LinkedList<>();
    int max = 0;
    while(curr != null || !stack.isEmpty()) {
        if(curr != null) {
            stack.push(curr);
            int size = stack.size();
            if(size > max) {
                max = size;
            }
            curr = curr.left;
        } else {
            TreeNode peek = stack.peek();
            if(peek.right == null || peek.right == pop) {
                pop = stack.pop();
            } else {
                curr = peek.right;
            }
        }
    }
    return max;
}

// 层序遍历
public int maxDepth(TreeNode node) {
    if(root == null) {
        return 0;
    }
	Queue<TreeNode> queue = new LinkedList<>();
    queue.offer(root);
    int depth = 0;
    while(!queue.isEmpty()) {
        int size = queue.size();
        for(int i = 0; i < size; i++) {
            TreeNode poll = queue.poll();
            if(poll.left != null) {
                queue.offer(poll.left);
            }
            if(poll.right != null) {
                queue.offer(poll.right);
            }
        }
        depth++;
    }
    
    return depth;
}
```

##### 最小深度

```java
// 递归
public int minDepth(TreeNode node) {
    if(node == null) {
        return 0;
    }
    int d1 = minDepth(node.left);
    int d2 = minDepth(node.right);
    // 当右子树为null，返回左子树深度+1
    if(d2 == 0) {
        return d1 + 1;
    }
    // 当左子树为null，返回右子树深度+1
    if(d1 == 0) {
        return d2 + 1;
    }
    return Integer.min(d1, d2) + 1;
}

// 层序遍历
public int minDepth(TreeNode node) {
    if(root == null) {
        return 0;
    }
	Queue<TreeNode> queue = new LinkedList<>();
    queue.offer(root);
    int depth = 0;
    while(!queue.isEmpty()) {
        int size = queue.size();
        depth++;
        for(int i = 0; i < size; i++) {
            TreeNode poll = queue.poll();
            if(poll.left == null && poll.right == null) {
                return depth;
            }
            if(poll.left != null) {
                queue.offer(poll.left);
            }
            if(poll.right != null) {
                queue.offer(poll.right);
            }
        }
    }
    
    return depth;
}
```

##### 反转二叉树

```java
public TreeNode invertTree(TreeNode root) {
    fn(root);
    return root;
}

private static void fn(TreeNode node) {
    if(node == null) {
        return;
    }
    TreeNode t = node.left;
    node.left = node.right;
    node.right = t;
    
    fn(node.left);
    fn(node.right);
}
```

##### 根据后缀表达式构造表达式树

```java
public TreeNode constructExpressionTree(String[] tokens) {
    LinkedList<TreeNode> stack = new LinkedList<>();
    for(String t : tokens) {
        switch(t) {
            case "+","-","*","/" -> {
                TreeNode right = stack.pop();
                TreeNode left = stack.pop();
                TreeNode parent = new TreeNode(t);
                parent.left = left;
                parent.right = right;
                stack.push(parent);
            }
            default -> {
                stack.push(new TreeNode(t));
            }
        }
    }
    return stack.peek();
}
```

##### 根据前中序遍历结果构造二叉树

```java
public TreeNode buildTree(int[] preOrder, int[] inOrder) {
    if(preOrder.length == 0) {
        return null;
    }
    int rootValue = preOrder[0];
    TreeNode root = new TreeNode(rootValue);
    for(int i = 0; i < inOrder.length; i++) {
        if(inOrder[i] == rootValue) {
            // 0 ~ i-1 左子树
            // i+1 ~ inOrder.length - 1 右子树
.           int[] inLeft = Arrays.copyOfRange(inOrder, 0, i);
            int[] inRight = Arrays.copyOfRange(inOrder, i + 1, inOrder.length);
            
            int[] preLeft = Arrays.copyOfRange(preOrder, 1, i + 1);
            int[] preRight = Arrays.copyOfRange(preOrder, i + 1, inOrder.length);
            
            root.left = buildTree(preLeft, inLeft);
            root.right = buildTree(preRight, inRight);
            break;
        }
    }
    return root;
}
```

##### 根据中序与后序遍历结果构建二叉树

```java
public TreeNode buildTree(int[] inOrder, int[] postOrder) {
    if(inOrder.length == 0) {
        return null;
    }
    int rootValue = postOrder[postOrder.length - 1];
    TreeNode root = new TreeNode(rootValue);
    for(int i = 0; i < inOrder.length; i++) {
        if(inOrder[i] == rootValue) {
.           int[] inLeft = Arrays.copyOfRange(inOrder, 0, i);
            int[] inRight = Arrays.copyOfRange(inOrder, i + 1, inOrder.length);
            
            int[] postLeft = Arrays.copyOfRange(postOrder, 0, i);
            int[] postRight = Arrays.copyOfRange(postOrder, i, postOrder.length - 1);
            
            root.left = buildTree(inLeft, postLeft);
            root.right = buildTree(inRight, postRight);
            break;
        }
    }
    return root;
}
```

#### 二叉搜索树

1. 树节点增加key属性，用来比较谁大谁小，key不可以重复
2. 对于任意一个树节点，它的key比左子树的key都大，同时也比右子树的key都小

```java
public class BSTTree {
    
    BSTNode root;
    
    static class BSTNode {
        int key;
        Object value;
        BSTNode left;
        BSTNode right;
        
        public BSTNode(int key) {
        	this.key = key;
    	}
    
        public BSTNode(int key, Object value) {
            this.key = key;
            this.value = value;
        }
        
        public BSTNode(int key, Object value, BSTNode left, BSTNode right) {
            this.key = key;
            this.value = value;
            this.left = left;
            this.right = right;
        }
    }

    // 递归
    public Object get(int key) {
        return doGet(root, key);
    }
    
    private Object doGet(BSTNode node, int key) {
        if(node == null) {
            return null;
        }
        if(key < node.key) {
            return doGet(node.left, key);
        } else if(node.key < key) {
            return doGet(node.right, key);
        } else {
            return node.value;
        }
    }
    
    // 非递归
    public Object get(int key) {
        BSTNode node = root;
        while(node != null) {
            if(key < node.key) {
                node = node.left;
            } else if(node.key < key) {
                node = node.right;
            } else {
                return node.value;
            }
        }
        return null;
    }
    
    // 递归
    public Object min() {
        return doMin(root);
    }
    
    private Object doMin(BSTNode node) {
        if(node == null) {
            return null;
        }
        if(node.left == null) {
            return node.value;
        }
        return doMin(node.left);
    }
    
    // 非递归
    public Object min() {
        return min(root);
    }
    
    public Object min(BSTNode node) {
        if(node == null) {
            return null;
        }
        BSTNode p = node;
        while(p.left != null) {
            p = p.left;
        }
        return p.value;
    }
    
    public Object max() {
        return max(root);
    }
    
    public Object max(BSTNode node) {
        if(node == null) {
            return null;
        }
        BSTNode p = node;
        while(p.right != null) {
            p = p.right;
        }
        return p.value;
    }
    
    public void put(int key, Object value) {
        BSTNode node = root;
        BSTNode parent = null;
        while(node != null) {
            parent = node;
            if(key < node.key) {
                node = node.left;
            } else if(node.key < key) {
                node = node.right;
            } else {
                // 1.key 有 更新
                node.value = value;
                return;
            }
        }
        // 2.key 没有 新增
        if(parent == null) {
            root = new BSTNode(key, value);
            return;
        }
        if(key < parent.key) {
            parent.left = new BSTNode(key, value);
        } else {
            parent.right = new BSTNode(key, value);
        }
    }
    
    // 查找关键字的后继值
    public Object successor(int key) {
        BSTNode p = root;
        BSTNode ancestorFromRight = null;
        while(p != null) {
            if(key < p.key) {
                ancestorFromRight = p;
                p = p.left;
            } else if(p.key < key) {
                p = p.right;
            } else {
                break;
            }
        }
        
        // 没找到节点
        if(p == null) {
            return null;
        }
        // 找到节点
        // 1.节点有右子树，此时后继节点即为右子树的最小值
        if(p.right != null) {
            return min(p.right);
        }
        // 2.节点没有右子树，若离它最近的祖先自从右而来，此祖先即为后继
        return ancestorFromRight != null ? ancestorFromRight.value : null;
    }
    
    // 查找关键字的前驱值
    public Object predecessor(int key) {
        BSTNode p = root;
        BSTNode ancestorFromLeft = null;
        while(p != null) {
            if(key < p.key) {
                p = p.left;
            } else if(p.key < key) {
                ancestorFromLeft = p;
                p = p.right;
            } else {
                break;
            }
        }
        
        // 没找到节点
        if(p == null) {
            return null;
        }
        // 找到节点
        // 1.节点有左子树，此时前驱节点就是左子树的最大值
        if(p.left != null) {
            return max(p.left);
        }
        // 2.节点没有左子树，若离它最近的祖先自从左而来，此祖先即为先驱
        return ancestorFromLeft != null ? ancestorFromLeft.value : null;
    }
    
    // 1.删除节点没有左孩子，将右孩子托孤给Parent
    // 2.删除节点没有右孩子，将左孩子托孤给Parent
    // 3.删除节点左右孩子都没有，已经被涵盖在情况1、情况2当中，把null托孤给Parent
    // 4.删除节点左右孩子都有，可以将它的后继节点（称为S）托孤给Parent，再称S的父亲为SP，又分为两种情况
    // 4.1.SP就是被删除节点，此时D与S紧邻，只需将S托孤给Parent
    // 4.2.SP不是被删除节点，此时D与S不相邻，此时需要将S的后代托孤给SP，再将S托孤给Parent
    public Object delete(int key) {
        BSTNode p = root;
        BSTNode parent = null;
        while(p != null) {
            if(key < p.key) {
                parent = p;
                p = p.left;
            } else if(p.key < key) {
                parent = p;
                p = p.right;
            } else {
                break;
            }
        }
        if(p == null) {
            return null;
        }
        // 删除操作
        if(p.left == null) {
            shift(parent, p, p.right); // 情况1
        } else if(p.right == null) {
            shift(parent, p, p.left); // 情况2
        } else {
            // 情况4
            // 4.1 被删除节点找后继
            BSTNode s = p.right;
            BSTNode sParent = p; // 后继父亲
            while(s.left != null) {
                sParent = s;
                s = s.left;
            }
            // 后继节点即为s
            if(sParent != p) { //不相邻
                // 4.2 删除和后继不相邻，处理后继的后事
                shift(sParent, s, s.right);
                s.right = p.right;
            }
            // 4.3 后继取代被删除节点
            shift(parent, p, s);
            s.left = p.left;
        }
        return p.value;
    }
    
    private void shift(BSTNode parent, BSTNode deleted, BSTNode child) {
        if(parent == null) {
            root = child;
        } else if(deleted == parent.left){
            parent.left = child;
        } else {
            parent.right = child;
        }
    }
    
    // 递归删除
    public Object delete(int key) {
        ArrayList<Object> result = new ArrayList<>();
        root = doDelete(root, key, result);
        return result.isEmpty() ? null : result.get(0);
    }
    // 返回值 剩下的孩子或null
    private BSTNode doDelete(BSTNode node, int key, ArrayList<Object> result) {
        if(node == null) {
            return null;
        }
        if(key < node.key) {
            node.left = doDelete(node.left, key, result);
            return node;
        }
        if(node.key < key) {
            node.right = doDelete(node.right, key, result);
            return node;
        }
        result.add(node.value);
        // 情况1 只有右孩子
        if(node.left == null) {
            return node.right;
        }
        // 情况2 只有左孩子
        if(node.right == null) {
            return node.left;
        }
        // 情况3 有两个孩子
        BSTNode s = node.right;
        while(s.left != null) {
            s = s.left;
        }
        s.left = node.left;
        s.right = doDelete(node.right, s.key, new ArrayList<>());
        return s;
    }
    
    // 找 < key的所有的value
    public List<Object> less(int key) {
        ArrayList<Object> result = new ArrayList<>();
        BSTNode p = root;
        LinkedList<BSTNode> stack = new LinkedList<>();
        while(p != null || !stack.isEmpty()) {
            if(p != null) {
                stack.push(p);
                p = p.left;
            } else {
                BSTNode pop = stack.pop();
                if(pop.key < key) {
                    result.add(pop.value);
                } else {
                    break;
                }
                p = pop.right;
            }
        }
        return result;
    }
    
    // 找 > key的所有的value，中序遍历
    public List<Object> greater(int key) {
        ArrayList<Object> result = new ArrayList<>();
        BSTNode p = root;
        LinkedList<BSTNode> stack = new LinkedList<>();
        while(p != null || !stack.isEmpty()) {
            if(p != null) {
                stack.push(p);
                p = p.left;
            } else {
                BSTNode pop = stack.pop();
                if(pop.key > key) {
                    result.add(pop.value);
                }
                p = pop.right;
            }
        }
        return result;
    }
    
    // 找 > key的所有的value，反转中序遍历
    public List<Object> greater(int key) {
        ArrayList<Object> result = new ArrayList<>();
        BSTNode p = root;
        LinkedList<BSTNode> stack = new LinkedList<>();
        while(p != null || !stack.isEmpty()) {
            if(p != null) {
                stack.push(p);
                p = p.right;
            } else {
                BSTNode pop = stack.pop();
                if(pop.key > key) {
                    result.add(pop.value);
                } else {
                    break;
                }
                p = pop.left;
            }
        }
        return result;
    }
    
    // 找 >= key1且 <= key2的所有的value
    public List<Object> between(int key1, int key2) {
        ArrayList<Object> result = new ArrayList<>();
        BSTNode p = root;
        LinkedList<BSTNode> stack = new LinkedList<>();
        while(p != null || !stack.isEmpty()) {
            if(p != null) {
                stack.push(p);
                p = p.left;
            } else {
                BSTNode pop = stack.pop();
                if(pop.key >= key1 && pop.key <= key2) {
                    result.add(pop.value);
                } else if(pop.key > key2) {
                    break;
                }
                p = pop.right;
            }
        }
        return result;
    }
}
```

#### AVL树

二叉搜索树在插入和删除时，节点可能失衡。如果在插入和删除时通过旋转，始终让二叉搜索树保持平衡，称为自平衡的二叉搜索树。AVL树是自平衡二叉搜索树的实现之一。

如果一个节点的左右孩子高度差超过1，则此节点失衡，才需要旋转

```java
public class AVLTree {
    
    static class AVLNode {
        int key;
        Object value;
        AVLNode left;
        AVLNode right;
        int height = 1;
        
        public AVLNode(int key) {
            this.key = key;
        }
        
        public AVLNode(int key, Object value) {
            this.key = key;
            this.value = value;
        }
        
        public AVLNode(int key, Object value, AVLNode left, AVLNode right) {
            this.key = key;
            this.value = value;
            this.left = left;
            this.right = this.right;
        }
    }
    
    // 求节点的高度
    private int height(AVLNode node) {
        return node == null ? 0 : node.height;
    }
    
    // 更新节点高度（新增、删除、旋转）
    private void updateHeight(AVLNode node) {
        node.height = Integer.max(height(node.left), height(node.right)) + 1;
    }
    
    // 平衡因子（balance factor）= 左子树高度 - 右子树高度
    private int bf(AVLNode node) {
        return height(node.left) - height(node.right);
    }
    
    // LL：失衡节点的bf > 1，即左边更高
    // 失衡节点的左孩子的bf >= 0，即左孩子这边也是左边更高或等高
    
    // LR：失衡节点的bf > 1，即左边更高
    // 失衡节点的左孩子的bf < 0，即左孩子这边是右边更高
    
    // RL：失衡节点的bf < -1，即右边更高
    // 失衡节点的右孩子的bf > 0，即右孩子这边左边更高
    
    // RR：失衡节点的bf < -1，即右边更高
    // 失衡节点的右孩子的bf <= 0，即右孩子这边右边更高或等高
    private AVLNode rightRotate(AVLNode red) {
        AVLNode yellow = red.left;
        AVLNode green = yellow.right;
        yellow.right = red;
        red.left = green;
        updateHeight(red);
        updateHeight(yellow);
        return yellow;
    }
    
    private AVLNode leftRotate(AVLNode node) {
        AVLNode yellow = red.right;
        AVLNode green = yellow.left;
        yellow.left = red;
        red.right = green;
        updateHeight(red);
        updateHeight(yellow);
        return yellow;
    }
    
    // 先左旋左子树，再右旋根节点
    private AVLNode leftRightRotate(AVLNode node) {
        node.left = leftRotate(node.left);
        return rightRotate(node);
    }
    
    // 先右旋右子树，再左旋根节点
    private AVLNode rightLeftRotate(AVLNode node) {
        node.right = rightRotate(node.right);
        return leftRotate(node);
    }
    
    // 检查节点是否失衡，重新平衡代码
    private AVLNode balance(AVLNode node) {
        if(node == null) {
            return null;
        }
        int bf = bf(node);
        if(bf > 1 && bf(node.left) >= 0) { // LL
            return rightRotate(node);
        } else if(bf > 1 && bf(node.left) < 0){ // LR
            return leftRightRotate(node);
        } else if(bf < -1 && bf(node.right) > 0){ // RL 
            return rightLeftRotate(node);
        } else if(bf < -1 && bf(node.right) <= 0){ // RR
            return leftRotate(node);
        }
        return node;
    }
    
    AVLNode root;
    public void put(int key, Object value) {
        root = doPut(root, key, value);
    }
    
    private AVLNode doPut(AVLNode node, int key, Object value) {
        // 1.找到空位，创建新节点
        if(node == null) {
            return new AVLNode(key, value);
        }
        // 2.key已存在，更新
        if(key == node.key) {
            node.value = value;
            return node;
        }
        // 3.继续查找
        if(key < node.key) {
            node.left = doPut(node.left, key, value);
        } else {
            node.right = doPut(node.right, key, value);
        }
        updateHeight(node);
        return balance(node);
    }
    
    public void remove(int key) {
        root = doRemove(root, key);
    }
    
    private AVLNode doRemove(AVLNode node, key) {
        // 1.node == null
        if(node == null) {
            return null;
        }
        // 2.没找到key
        if(key < node.key) {
            node.left = doRemove(node.left, key);
        } else if(node.key < key) {
            node.right = doRemove(node.right, key);
        } else {
            // 3.找到key 1) 没有 2) 只有一个孩子 3) 有两个孩子
            if(node.left == null && node.right == null) {
                return null;
            } else if(node.left == null) {
                node = node.right;
            } else if(node.right == null) {
                node = node.left;
            } else {
                AVLNode s = node.right;
                while(s.left != null) {
                    s = s.left;
                }
                // s 后继节点
                s.right = doRemove(node.right, s.key);
                s.left = node.left;
                node = s;
            }
        }
        // 4.更新高度
        updateHeight(node);
        // 5.balance
        balance(node);
    }
}
```

#### 红黑树

红黑树也是一种自平衡的二叉搜索树，较之AVL，插入和删除时旋转次数更少

红黑树特性：

+ 所有节点都有两种颜色：红与黑
+ 所有null视为黑色
+ 红色节点不能相邻
+ 根节点是黑色
+ 从根到任意一个叶子节点，路径中的黑色节点数一样（黑色完美平衡）

```java
public class RedBlackTree {
    enum Color {
        RED, BLACK;
    }
    
    private Node root;
    
    private static class Node {
        int key;
        Object value;
        Node left;
        Node right;
        Node parent;
        Color color = RED;
        
        // 是否是左孩子
        boolean isLeftChild() {
            return parent != null && parent.left == this;
        }
        
        // 叔叔
        Node uncle() {
            if(parent == null || parent.parent == null) {
                return null;
            }
            if(parent.isLeftChild()) {
                return parent.parent.right;
            } else {
                return parent.parent.left;
            }
        }
        
        // 兄弟
        Node sibling() {
            if(parent == null) {
                return null;
            }
            if(this.isLeftChild()) {
                return parent.right;
            } else {
                return parent.left;
            }
        }
    }
    
    // 判断红
    boolean isRed(Node node) {
        return node != null && node.color == RED;
    }
    
    // 判断黑
    boolean isBlack(Node node) {
        return node == null && node.color == BLACK;
    }
    
    // 右旋
    private void rightRotate(Node pink) {
        Node parent = pink.parent;
        Node yellow = pink.left;
        Node green = yellow.right;
        if(green != null) {
            green.parent = pink;
        }
        yellow.right = pink;
        yellow.parent = parent;
        pink.left = green;
        pink.parent = yellow;
        if(parent == null) {
            root = yellow;
        } else if(parent.left == pink) {
            parent.left = yellow;
        } else {
            parent.right = yellow;
        }
    }
    
    // 左旋
    private void leftRotate(Node pink) {
        Node parent = pink.parent;
        Node yellow = pink.right;
        Node green = yellow.left;
        if(green != null) {
            green.parent = pink;
        }
        yellow.left = pink;
        yellow.parent = parent;
        pink.right = green;
        pink.parent = yellow;
        if(parent == null) {
            root = yellow;
        } else if(parent.right == pink) {
            parent.right = yellow;
        } else {
            parent.left = yellow;
        }
    }
    
    // 新增或更新，正常增、遇到红红不平衡进行调整
    public void put(int key, Object value) {
        Node p = root;
        Node parent = null;
        while(p != null) {
            parent = p;
            if(key < p.key) {
                p = p.left;
            } else if(p.key < key) {
                p = p.right;
            } else {
                p.value = value;
                return;
            }
        }
        Ndoe inserted = new Node(key, value);
        if(parent == null) {
            root = inserted;
        } else if(key < parent.key) {
            parent.left = inserted;
            inserted.parent = parent;
        } else {
            parent.right = inserted;
            inserted.parent = parent;
        }
        fixRedRed(inserted);
    }
    
    // 插入节点均视为红色
    // 1.插入节点为根节点，将根节点变黑
    // 2.插入节点的父亲若为黑色，树的红黑性质不变，无需调整
    // 插入节点的父亲为红色，触发红红相邻
    // 3.叔叔为红色
    // 4.叔叔为黑色
    void fixRedRed(Node x) {
        // 1.插入节点为根节点，将根节点变黑
        if(x == root) {
            x.color = BLACK;
            return;
        }
        // 2.插入节点的父亲若为黑色，树的红黑性质不变，无需调整
        if(isBlack(x.parent)) {
            return;
        }
        // 3.红红相邻，叔叔为红色
        // 需要将父亲、叔叔变黑、祖父变红，然后对祖父做递归处理
        Node parent = x.parent;
        Node uncle = x.uncle();
        Node grandparent = parent.parent;
        if(isRed(uncle)) {
            parent.color = BLACK;
            uncle.color = BLACK;
            grandparent.color = RED;
            fixRedRed(grandparent);
        }
        
        // 4.红红相邻，叔叔为黑色
        // -父亲为左孩子，插入节点也是左孩子，此时即LL不平衡
        // -父亲为左孩子，插入节点是右孩子，此时即LR不平衡
        // -父亲为右孩子，插入节点也是右孩子，此时即RR不平衡
        // -父亲为右孩子，插入节点是左孩子，此时即RL不平衡
        if(parent.isLeftChild() && x.isLeftChild()) { // LL
            parent.color = BLACK;
            grandparent.color = Red;
            rightRotate(grandparent);
        } else if(parent.isLeftChild()) { // LR
            leftRotate(parent);
            x.color = BLACK;
            grandparent = RED;
            rightRotate(grandparent);
        } else if(!x.isLeftChild()){ // RR
            parent.color = BLACK;
            grandparent.color = RED;
            leftRotate(grandparent);
        } else { // RL
            rightRotate(parent);
            x.color = BLACK;
            grandparent = RED;
            leftRotate(grandparent);
        }
        
    }
    
    // 删除，正常删、会用到李代桃僵技巧，遇到黑黑不平衡进行调整
    public void remove(int key) {
        Node deleted = find(key);
        if(deleted == null) {
            return;
        }
        doRemove(deleted);
    }
    
    // 处理双黑(case3、case4、case5)
    // 删除节点和剩下节点都是黑，触发双黑，双黑意思是少了一个黑
    // case3 被调整节点的兄弟为红，此时两个侄子定为黑
    // case4 被调整节点的兄弟为黑，两个侄子都为黑
    // -将兄弟变红，目的是将删除节点和兄弟那边的黑色高度同时减少1
    // -如果父亲是红，则需将父亲变为黑，避免红红，此时路径黑节点数目不变
    // -如果父亲是黑，说明这条路径则少了一个黑，再次让父节点触发双黑
    // case5 被调整节点的兄弟为黑，至少一个红侄子
    // -如果兄弟是左孩子，左侄子是红，LL不平衡
    // -如果兄弟是左孩子，右侄子是红，LR不平衡
    // -如果兄弟是右孩子，右侄子是红，RR不平衡
    // -如果兄弟是右孩子，左侄子是红，RL不平衡
    private void fixDoubleBlack(Node x) {
        if(x == root) {
            return;
        }
        Node parent = x.parent;
        Node sibling = x.sibling();
        // case3 兄弟节点是红色
        if(isRed(sibling)) {
            if(x.isLeftChild()) {
                leftRotate(parent);
            } else {
                rightRotate(parent);
            }
            parent.color = RED;
            sibling.color = BLACK;
            fixDoubleBlack(x);
            return;
        }
        if(sibling != null) {
            // case4 兄弟是黑色，两个侄子也是黑色
            if(isBlack(sibling.left) && isBlack(sibling.right)) {
            	sibling.color = RED;
                if(isRed(parent)) {
                    parent.color = BLACK;
                } else {
                    fixDoubleBlack(parent);
                }
        	} else { // case5 兄弟是黑色，侄子有红色
                // LL
                if(sibling.isLeftChild() && isRed(sibling.left)) {
                    rightRotate(parent);
                    sibling.lefft.color = BLACK;
                    sibling.color = parent.color;
                }
                // LR
                else if(sibling.isLeftChild() && isRed(sibling.right)) {
                    sibling.right.color = parent.color;
                    leftRotate(sibling);
                    rightRotate(parent);
                }
                // RL
                else if(!sibling.isLeftChild() && isRed(sibling.left)) {
                    sibling.left.color = parent.color;
                    rightRotate(sibling);
                    leftRotate(parent);
                }
                // RR
                else {
                    leftRotate(parent);
                    sibling.right.color = BLACK;
                    sibling.color = parent.color
                }
                parent.color = BLACK;
            }
        } else {
            fixDoubleBlack(parent);
        }
    }
    
    private void doRemove(Node deleted) {
        Node replaced = findReplaced(deleted);
        Node parent = deleted.parent;
        // 没有孩子
        if(replaced == null) {
            // case1 删的是根节点
            if(deleted == root) {
                root = null;
            } else {
                if(isBlack(deleted)) {
                    // 复杂调整
                    fixDoubleBlack(deleted);
                } else {
                    // 红色叶子，无需任何处理
                }
                if(deleted.isLeftChild()) {
                    parent.left = null;
                } else {
                    parent.right = null;
                }
                deleted.parent = null;
            }
            return;
        }
        // 有一个孩子
        if(deleted.left == null || deleted.right == null) {
            // case1 删除的是根节点
            if(deleted == root) {
                root.key = replaced.key;
                root.value = replaced.value;
                root.left = root.right = null;
            } else {
                if(deleted.isLeftChild()) {
                    parent.left = replaced;
                } else {
                    parent.right = replaced;
                }
                replaced.parent = parent;
                deleted.left = deleted.right = deleted.parent = null;
                if(isBlack(deleted) && isBlack(replaced)) {
                    // 复杂处理
                    fixDoubleBlack(replaced);
                } else {
                    // case2 删的是黑，剩的是红，剩下这个节点红变黑
                    replaced.color = BLACK;
                }
            }
            return;
        }
        // case0 有两个孩子 转换为 有一个孩子 或 没有孩子
        int t = deleted.key;
        deleted.key = replaced.key;
        replaced.key = t;
        
        Object v = deleted.value;
        deleted.value = replaced.value;
        replaced.value = v;
        doRemove(replaced);
    }
    
    // 查找删除节点
    private Node find(int key) {
        Node p = root;
        while(p != null) {
            if(key < p.key) {
                p = p.left;
            } else if(p.key < key) {
                p = p.right;
            } else {
                return p;
            } 
        }
        return null;
    }
    
    // 查找剩余节点
    private Node findReplaced(Node deleted) {
        if(deleted.left == null && deleted.right == null) {
            return null;
        }
        if(deleted.left == null) {
            return deleted.right;
        }
        if(deleted.right == null) {
            return deleted.left;
        }
        Node s = deleted.right;
        while(s.left != null) {
            s = s.left;
        }
        return s;
    }
}
```

### B树

度（degree）：指树中节点孩子数

阶（order）：指所有节点孩子数最大值

特性：

+ 每个节点最多有m个孩子，其中m称为B-树的阶
+ 除根节点和叶子节点外，其他每个节点至少有ceil(m/2)个孩子
+ 若根节点不是叶子节点，则至少有两个孩子
+ 所有叶子节点都在同一层
+ 每个非叶子节点由n个关键字和n+1个指针组成，其中ceil(m/2)-1 < n < m-1
+ 关键字按非降序排列

```java
public class BTree {
    
    static class Node {
        int[] keys; // 关键字
        Node[] children; // 孩子
        int keyNumber; // 有效关键字数目
        boolean leaf; // 是否是叶子节点
        int t; // 最小度数（最小孩子数）
        
        public Node(int t) { // t>=2
            this.t = t;
            this.children = new Node[2 * t];
            this.keys = new int[2 * t -1];
        }
        
        @Override
        public String toString() {
            return Arrays.toString(Arrays.copyOfRnage(keys, 0, keyNumber));
        }
        
        // 多路查找
        Node get(int key) {
            int i = 0;
            while(i < keyNumber) {
                if(keys[i] == key){
                    return this;
                }
                if(key[i] > key) {
                    break;
                }
                i++;
            }
            // 执行到此时keys[i] > key或i == keyNumber
            if(leaf) {
                return null;
            }
            // 非叶子情况
            return children[i].get(key);
        }
        
        // 向指定索引处插入key
        void insertKey(int key, int index) {
            System.arraycopy(keys, index, keys, index + 1, keyNumber - index);
            keys[index] = key;
            keyNumber++;
        }
        
        // 向指定索引处插入child
        void insertChild(Node child, int index) {
            System.arraycopy(children, index, children, index + 1, keyNumber - index);
            children[index] = child;
        }
        
        // 移除指定index处的key
        int removeKey(int index) {
            int t = keys[index];
            System.arraycopy(keys, index + 1, keys, index, --keyNumber - index);
            return t;
        }
        
        // 移除最左边的key
        int removeLeftmostKey() {
            return removeKey(0);
        }
        
        // 移除最右边的key
        int removeRightmostKey() {
            return removeKey(keyNumber - 1);
        }
        
        // 移除指定index处的child
        Node removeChild(int index) {
            Node t = children[index];
            System.arraycopy(children, index + 1, children, index, keyNumber - index);
            children[keyNumber] = null;
            return t;
        }
        
        // 移除最左边的child
        Node removeLeftmostChild() {
            return removeChild(0);
        }
        
        // 移除最右边的child
        Node removeRighttmostChild() {
            return removeChild(keyNumber);
        }
        
        // index 孩子处左边的兄弟
        Node childLeftSibling(int index) {
            return index > 0 ? children[index - 1] : null;
        }
        
        // index孩子处右边的兄弟
        Node childRightSibling(int index) {
            return index == keyNumber ? null : children[index = 1];
        }
        
        // 复制当前节点所有key和child到target
        void moveToLeft(Node target) {
            int start = target.keyNumber;
            if(!leaf) {
                for(int i = 0; i <= keyNumber; i++) {
                    target.children[start + 1] = children[i];
                }
            }
            for(int i = 0; i < keyNumber; i++) {
                target.keys[target.keyNumber++] = keys[i];
            }
        }
    }
    
    Node root;
    
    int t; // 树中节点最小度数
    final int MIN_KEY_NUMBER; // 最小key数目
    final int MAX_KEY_NUMBER; // 最大key数目
    
    public BTree() {
        this(2);
    }
    
    public BTree(int t) {
        this.t = t;
        root = new Node(t);
        MAX_KEY_NUMBER = 2 * t - 1;
        MIN_KEY_NUMBER = t - 1;
    }
    
    // 是否存在
    public boolean contains(int key) {
        return root.get(key) != null;
    }
    
    // 新增
    // 首先查找本节点中的插入位置，如果没有空位（key被找到），应该走更新的逻辑，目前什么没做
    // 接下来分两种情况：
    // -如果节点是叶子节点，可以直接插入
    // -如果节点是非叶子节点，需要继续在children[i]处继续递归插入
    // 无论哪种情况，插入完成后都可能超过节点keys数目限制，此时应当执行节点分裂
    public void put(int key) {
        doPut(root, key, null, 0);
    }
    
    private void doPut(Node node, int key, Node parent, int index) {
    	int i = 0;
        while(i < node.keyNumber) {
            if(node.keys[i] == key) {
                return; // 更新
            }
            if(node.keys[i] > key) {
                break; // 找到了插入位置
            }
            i++;
        }
        if(node.leaf) {
            node.insertKey(key, i);
        } else {
            doPut(node.children[i], key, node, i);
        }
        if(node.keyNumber == MAX_KEY_NUMBER) {
            split(node, parent, index);
        }
    }
    
    // 分裂方法
    // left 要分裂的节点，parent 分裂节点的父节点，index 分裂节点是第几个孩子
    private void split(Node left, Node parent, int index) {
        // 如果parent == null表示要分裂的是根节点，此时需要创建新根，原来的根节点作为新根的0孩子
        if(parent == null) { 
            Node newRoot = new Node(t);
            newRoot.leaf = false;
            newRoot.insertChild(left, 0);
            this.root = newRoot;
            parent = newRoot;
        }
        // 1.创建right节点（分裂后大于当前left节点的），把t以后的key和child都拷贝过去
        Node right = new Node(t);
        right.leaf = left.leaf;
        System.arraycopy(left.keys, t, right.keys, 0, t - 1);
        // 分裂节点是非叶子的情况
        if(!left.leaf) {
            System.arraycopy(left.children, t, right.children, 0, t);
        }
        right.keyNumber = t - 1;
        left.keyNumber = t - 1;
        // 2.t-1处的key插入到parent的index处，index指left作为孩子时的索引
        int mid = left.keys[t - 1];
        parent.insertKey(mid, index);
        // 3.right节点作为parent的孩子插入到index + 1处
        parent.insertChild(right, index + 1);
    }
    
    // 删除
    // case1 当前节点是叶子节点，没找到
    // case2 当前节点是叶子节点，找到了
    // case3 当前节点是非叶子节点，没找到
    // case4 当前节点是非叶子节点，找到了
    // case5 删除后key数目 < 下线（不平衡）
    // case6 根节点
    public void remove(int key) {
        doRemove(null, root, 0, key);
    }
    
    private void doRemove(Node parent, Node node, int index, int key) {
        int i = 0;
        while(i < node.keyNumber) {
            if(node.keys[i] >= key) {
                break;
            }
            i++;
        }
        // i找到，代表待删除key的索引
        // i没找到，代表到第i个孩子继续查找
        if(node.leaf) {
            if(!found(node, key, i)) { // case1
            	return;
            } else { // case2
				node.removeKey(i);
            }
        } else {
            if(!found(node, key, i)) { // case3
            	doRemove(node, node.children[i], i, key);
            } else { // case4
                // 1.找到后继key
                Node s = node.children[i + 1];
                while(!s.leaf) {
                    s = s.children[0];
                }
                int skey = s.keys[0];
                // 2.替换待删除的key
                node.keys[i] = skey;
                // 3.删除后继key
                doRemove(node, node.children[i + 1], i + 1, skey);
            }
        }
        if(node.keyNumber < MIN_KEY_NUMBER) {
            // 调整平衡 case5 case6
            balance(parent, node, index);
        }
    }
    
    private void balance(Node parent, Node x, int i) {
        // case6 根节点
        if(x == root) {
            if(root.keyNumber == 0 && root.children[0] != null) {
                root = root.children[0];
            }
            reutrn;
        }
        Node left = parent.childLeftSibling(i);
        Node right = parent.childRightSibling(i);
        // case5-1 左边富裕，右旋
        if(left != null && left.keyNumber > MIN_KEY_NUMBER) {
            // 父结点中前驱key旋转下来
            x.insertKey(parent.keys[i - 1], 0);
            if(!left.leaf) {
                // left中最大的孩子换爹
                x.insertChild(left.removeRightmostChild(), 0);
            }
            // left中最大的key旋转上去
            parent.keys[i - 1] = left.removeRightmostKey();
            return;
        }
        // case5-2 右边富裕，左旋
        if(right != null && right.keyNumber > MIN_KEY_NUMBER) {
            // 父节点中后继key选择下来
            x.insertKey(parent.keys[i], x.keyNumber);
            // right中最小的孩子换爹
            if(!right.leaf) {
                x.insertChild(right.removeLeftmostChild(), x.keyNumber + 1);
            }
            // right中最小的key旋转上去
            parent.keys[i] = right.removeLeftmostKey();
            return;
        }
        // case5-3 两边都不够借，向左合并
        if(left != null) {
            // 向左兄弟合并
            parent.removeChild(i);
            left.insertKey(parent.removeKey(i - 1), left.keyNumber);
            x.moveToTarget(left);
        } else {
            // 向自己合并
            parent.removeChild(i + 1);
            x.insertKey(parent.removeKey(i), x.keyNumber);
            right.moveToTarget(x);
        }
    }
    
    private boolean found(Node node, int key, int i) {
        return i < node.keyNumber && node.keys[i] == key;
    }
}
```

### 哈希表

```java
public class HashTable {
    
    static class Entry {
        int hash;
        Object key;
        Object value;
        Entry next;
    }
    
    public Entry(int hash, Object key, Object value) {
        this.hash = hash;
        this.key = key;
        this.value = value;
    }
    
    Entry[] table = new Entry[16];
    int size = 0;
    float loadFactor = 0.75f;
    int threshold = (int) (loadFactor * table.length);
    
    // 求模运算替换为位运算
    // -前提：数组长度是2的n次方
    // -hash % 数组长度等价于hash & (数组长度 - 1)
    
    Object get(int hash, Object key) {
        int idx = hash & (table.length - 1);
        if(table[idx] == null) {
            return null;
        }
        Entry p = table[idx];
        while(p != null) {
            if(p.key.equals(key)) {
                return p.value;
            }
            p = p.next;
        }
        return null;
    }
    
    void put(int hash, Object key, Obejct value) {
        int idx = hash & (table.length - 1);
        // 1.idx处有空位，直接新增
        if(table[idx] == null) {
            table[idx] = new Entry(hash, key, value);
        } else {
            // 2.idx处无空位，沿链表查找
            Entry p = table[idx];
            while(true) {
                if(p.key.equals(key)) {
                    p.value = value;
                    return;
                }
                if(p.next == null) {
                    break;
                }
                p = p.next;
            }
            p.next = new Entry(hash, key, value);
        }
        size++;
        if(size > threshold) {
            resize();
        }
    }
    
    private void resize() {
        Entry[] newTable = new Entry[table.length << 1];
        for(int i = 0; i < table.length; i++) {
            Entry p = table[i];
            if(p != null) {
                //  拆分链表，移动到新数组
                // 拆分规律
                // hash & table.length == 0的一组
                // hash & table.length != 0的一组
                Entry a = null;
                Entry b = null;
                Entry aHead = null;
                Entry bHead = null;
                while(p != null) {
                    if((p.hash & table.length) == 0) {
                        if(a != null) {
                            a.next = p;
                        } else {
                            aHead = p;
                        }
                        a = p; // 分配到a
                    } else {
                        if(b != null) {
                            b.next = p;
                        } else {
                            bHead = p;
                        }
                        b = p; // 分配到b
                    }
                    p = p.next;
                }
                if(a != null) {
                    a.next = null;
                    newTable[i] = aHead;
                }
                if(b != null) {
                    b.next = null;
                    newTable[i + table.length] = bHead;
                }
            }
        }
        table = newTable;
        threshold = (int) (loadFactor * table.length);
    }
    
    Object remove(int hash, Object key) {
        int idx = hash & (table.length - 1);
        if(table[idx] == null) {
            return null;
        }
        Entry p = table[idx];
        Entry prev = null;
        while(p != null) {
            if(p.key.equals(key)) {
                // 找到了删除
                if(prev == null) {
                    table[idx] = p.next;
                } else {
                   prev.next = p.next; 
                }
                size--;
                return p.value;
            }
            prev = p;
            p = p.next;
        }
        return null;
    }
    
    public Object get(Object key) {
        int hash = hash(key);
        return get(hash, key);
    }
    
    public void put(Object key, Object value) {
        int hash = hash(key);
        put(hash, key, value);
    }
    
    public Object remove(Object key) {
        int hash = hash(key);
        return remove(hash, key);
    }
    
    private static int hash(Obejct key) {
        return key.hashCode();
    }
}
```

**哈希算法**

hash算法是一种将任意长度的数据通过一个算法，变成固定长度数据的过程，这个固定长度的数据就是hash值，这个固定长度的数值就是hash值。常见的hash算法有MD5、SHA1、SHA256、SHA512、CRC32等

### 排序算法

| 算法 | 最好     | 最坏     | 平均     | 空间     | 稳定 | 思想 | 注意事项                                                     |
| ---- | -------- | -------- | -------- | -------- | ---- | ---- | ------------------------------------------------------------ |
| 冒泡 | O(n)     | O(n²)    | O(n²)    | O(1)     | Y    | 比较 | 最好情况需额外判断                                           |
| 选择 | O(n²)    | O(n²)    | O(n²)    | O(1)     | N    | 比较 | 顺序选择元素，交换次数较多，不适合大规模数据                 |
| 堆   | O(nlogn) | O(nlogn) | O(nlogn) | O(1)     | N    | 选择 | 堆排序的辅助性较强，理解先前理解的堆的数据结构               |
| 插入 | O(n)     | O(n²)    | O(n²)    | O(1)     | Y    | 比较 | 插入排序对于近乎有序的数据处理速度比较快，复杂度有所降低，可以提前结束 |
| 希尔 | O(nlogn) | O(n²)    | O(nlogn) | O(1)     | N    | 插入 | gap序列的构造有多种方式，不同方式处理的数据复杂度可能不同    |
| 归并 | O(nlogn) | O(nlogn) | O(nlogn) | O(n)     | Y    | 归并 | 需要额外的O(n)的存储空间                                     |
| 快速 | O(nlogn) | O(n²)    | O(nlogn) | O(nlogn) | N    | 分治 | 快排可能存在最坏的情况，需要把枢轴值选取得尽量随机化来缓解最坏情况下的时间复杂度 |

#### 冒泡排序

+ 每轮冒泡不断地比较相邻的两个元素，如果它们是逆序的，则交换它们的位置

+ 下一轮冒泡，可以调整未排序的右边界，减少不必要的比较

```java
public void bubbleSort(int[] a) {
    for (int i = 0; i < a.length - 1; i++) {
        for (int j = 0; j < a.length - 1 - i; j++) {
            if (a[j] > a[j + 1]) {
                int temp = a[j];
                a[j] = a[j + 1];
                a[j + 1] = temp;
            }
        }
    }
}

public void bubbleSort(int[] a) {
    int j = a.length - 1;
    do {
        int x = 0;
        for (int i = 0; i < j; i++) {
            if (a[i] > a[i + 1]) {
                int temp = a[i];
                a[i] = a[i + 1];
                a[i + 1] = temp;
                x = i;
            }
        }
        j = x;
    } while(j != 0);
}
```

#### 选择排序

+ 每一轮选择，找出最大（最小）的元素，并把它交换到合适的位置

```java
public void selectionSort(int[] a) {
    for (int i = a.length - 1; i > 0; i--) {
        int max = i;
        for (int j = 0; j < i; j++) {
            if(a[j] > a[max]) {
                max = j;
            }
        }
        if(max != i) {
            int temp = a[i];
        	a[i] = a[max];
        	a[max] = temp;  
        }
    }
}
```

#### 堆排序

+ 建立大顶堆
+ 每次将堆顶元素（最大值）交换到末尾，调整堆顶元素，让它重新符合大顶堆特性

```java
public void heapSort(int[] a) {
    heapify(a, a.length);
    for(int right = a.length - 1; right > 0; right--) {
        swap(a, 0, right);
        down(a, 0, right);
    }
}

// 建堆
public void heapify(int[] array, int size) {
    // 找到最后一个非叶子节点 size / 2 - 1
    for(int i = size / 2 - 1; i >= 0; i--) {
        down(array, i, size);
    }
}

// 下潜
public void down(int[] array, int parent, int size) {
    while(true) {
        int left = parent * 2 + 1;
        int right = left + 1;
        int max = parent;
        if(left < size && array[left] > array[max]) {
            max = left;
        }
        if(right < size && array[right] > array[max]) {
            max = right;
        }
        if(max == parent) { // 没找到更大的孩子
            break;
        }
        swap(array, max, parent);
        parent = max;
    }
}

// 交换
public void swap(int[] array, int i, int j) {
    int t = array[i];
    array[i] = array[j];
    array[j] = t;
}
```

#### 插入排序

+ 将数组分为两部分[0, low - 1]，[low, a.length - 1]
  + 左边[0, low - 1]是已排序部分
  + 右边[low, a.length - 1]是未排序部分
+ 每次从未排序区域取出low位置的元素，插入到已排序区域

```java
public void insertSort(int[] a) {
   for(int low = 1; low < a.length; low++) {
       int t = a[low];
       int i = low - 1;
       while(i >= 0 && t < a[i]) {
           a[i + 1] = a[i];
           i--;
       }
       if(i != low - 1) {
           a[i + 1] = t;
       }
   }
}
```

#### 希尔排序

+ 简单的说，就是分组实现插入，每组元素间隙称为gap
+ 每轮排序后gap逐渐变小，直至gap为1完成排序
+ 对插入排序的优化，让元素更快速地交换到最终位置

```java
public void shellSort(int[] a) {
    for(int gap = a.length >> 1; gap >= 1; gap = gap >> 1) {
        for(int low = gap; low < a.length; low++) {
           int t = a[low];
           int i = low - gap;
           while(i >= 0 && t < a[i]) {
               a[i + gap] = a[i];
               i -= gap;
           }
           if(i != low - gap) {
               a[i + gap] = t;
           }
       }
    }
}
```

#### 归并排序

+ 分 - 每次从中间切一刀，处理的数据少一半
+ 治 - 当数据仅剩一个时可以认为有序
+ 合 - 两个有序的结果，可以进行合并

```java
// 自顶至下
public void mergeSort(int[] a1) {
    int[] a2 = new int[a1.length];
    split(a1, 0, a.length - 1, a2);
}

public void split(int[] a1, int left, int right, int[] a2) {
    // 2.治
    if(left == right) {
        return;
    }
    // 1.分
    int m = (left + right) >> 1;
    split(a1, left, m, a2);
    split(a1, m + 1, right, a2);
    // 3.合
    merge(a1, left, m, m + 1, right, a2);
    System.arraycopy(a2, left, a1, left, right - left + 1);
}

// a1 原始数组，i~iEnd 第一个有序范围，j~jEnd 第二个有序范围，a2 临时数组
public void merge(int[] a, int i, int iEnd, int j, int jEnd, int[] a2) {
    int k = i;
    while(i <= iEnd && j <= jEnd) {
        if(a1[i] < a1[j]) {
            a2[k] = a1[i];
            i++;
        } else {
            a2[k] = a1[j];
            j++;
        }
        k++;
    }
    if(i > iEnd) {
       System.arraycopy(a1, j, a2, k, jEnd - j + 1);
    }
    if(j > jEnd) {
       System.arraycopy(a1, i, a2, k, iEnd - i + 1);
    }
}

// 自下至上
public void mergeSort(int[] a1) {
    int n = a1.length;
    int[] a2 = new int[n];
    // width 代表有序区间的宽度
    for(int width = 1; width < n; width *= 2) {
        // left right 分别代表带合并区间的左右边界
        for(int left = 0; left < n; left += 2 * width) {
            int right = Math.min(left + 2 * width - 1, n - 1);
            int m = Math.min(left + width - 1, n - 1);
            merge(a1, left, m, m + 1, right, a2);
        }
        System.arraycopy(a2, 0, a1, 0, n);
    }
}

// 归并 + 插入
public void split(int[] a1, int left, int right, int[] a2) {
    // 2.治
    if(left - right <= 32) {
        // 插入排序
        insertion(a1, left, right);
        return;
    }
    // 1.分
    int m = (left + right) >> 1;
    split(a1, left, m, a2);
    split(a1, m + 1, right, a2);
    // 3.合
    merge(a1, left, m, m + 1, right, a2);
    System.arraycopy(a2, left, a1, left, right - left + 1);
}

public void insertion(int[] a, int left, int right) {
   for(int low = left + 1; low <= right; low++) {
       int t = a[low];
       int i = low - 1;
       while(i >= left && t < a[i]) {
           a[i + 1] = a[i];
           i--;
       }
       if(i != low - 1) {
           a[i + 1] = t;
       }
   }
}
```

#### 快速排序

**单边循环快排（lomuto洛穆托分区方案）**

核心思想：每轮找一个基准点元素，把比它小的放到它左边，比它大的放到它右边，这称为分区

+ 选择最右侧元素作为基准点
+ j找比基准点小的，i找比基准点大的，一旦找到，二者进行交换
+ 最后基准点与i交换，i即为基准点最终索引

```java
public void quickSort(int[] a) {
    quick(a, 0, a.length - 1);
}

public void quick(int[] a, int left, int right) {
    if(left >= right) {
        return;
    }
    // p代表基准点元素索引
    int p = partition(a, left, right);
    quick(a, left, p - 1);
    quick(a, p + 1, right);
}

public int partition(int[] a, int left, int right) {
    int pv = a[right]; // 基准点元素
    int i = left;
    int j = left;
    while(j < right) {
        if(a[j] < pv) { // j找到比基准点小的了
            if(i != j) {
                swap(a, i, j);
            }
            i++;
        }
        i++;
        j++;
    }
    swap(a, i, right);
    return i;
}

public void swap(int[] array, int i, int j) {
    int t = array[i];
    array[i] = array[j];
    array[j] = t;
}
```

**双边循环**

+ 选择最左侧元素作为基准点
+ j找比基准点小的，i找比基准点大的，一旦找到，二者进行交换
  + i从左向右
  + j从右向左
+ 最后基准点与i交换，i即为基准点最终索引

```java
public int partition(int[] a, int left, int right) {
    int pv = a[left]; // 基准点元素
    int i = left;
    int j = right;
    while(i < j) {
        // 1.j从右向左找小的
        while(i < j && a[j] > pv) {
            j--;
        }
        // 2.i从左向右找大的
        while(i < j && a[i] <= pv) {
            i++;
        }
        // 3.交换位置
        swap(a, i, j);
    }
    swap(a, left, i);
    return i;
}

// 随机元素作为基准点
public int partition(int[] a, int left, int right) {
    int idx = ThreadLocalRandom.current().nextInt(right - left + 1) + left;
    swap(a, idx, left);
    int pv = a[left]; // 基准点元素
    int i = left;
    int j = right;
    while(i < j) {
        // 1.j从右向左找小的
        while(i < j && a[j] > pv) {
            j--;
        }
        // 2.i从左向右找大的
        while(i < j && a[i] <= pv) {
            i++;
        }
        // 3.交换位置
        swap(a, i, j);
    }
    swap(a, left, i);
    return i;
}

// 处理重复
// i从left + 1开始，从左向右找大的或相等的
// j从right开始，从右向左找小的或相等的
// 交换，i++ j--
public int partition(int[] a, int left, int right) {
    int pv = a[left]; // 基准点元素
    int i = left + 1;
    int j = right;
    while(i <= j) {
        // 1.j从右向左找小的
        while(i <= j && a[j] > pv) {
            j--;
        }
        // 2.i从左向右找大的
        while(i <= j && a[i] < pv) {
            i++;
        }
        // 3.交换位置
        if(i <= j) {
            swap(a, i, j);
            i++;
            j--;
        }
    }
    swap(a, j, left);
    return j;
}
```

#### 计数排序

+ 找最大值，创建一个大小为最大值+1的count数组
+ count数组的索引对应原始数组的元素，用来统计该元素的出现次数
+ 遍历count数组，根据count数组的索引（即原始数组的元素）以及出现次数，生成排序后内容

前提：待排序元素 >= 0，且最大值不能太大

```java
public void sort(int[] a) {
    int max = a[0];
    for(int i = 1; i < a.length; i++) {
        if(a[i] > max) {
            max = a[i];
        }
    }
    int[] count = new int[max + 1];
    for(int v : a) {
        count[v]++;
    }
    int k = 0;
    for(int i = 0; i < count.length; i++) {
        while(count[i] > 0) {
            a[k++] = i;
            count[i]--;
        }
    }
}

// 存在小于0的数
// 让原始数组的最小值映射到count[0] 最大值映射到count最右侧
public void sort(int[] a) {
    int max = a[0];
    int min = a[0];
    for(int i = 1; i < a.length; i++) {
        if(a[i] > max) {
            max = a[i];
        }
        if(a[i] < min) {
            min = a[i];
        }
    }
    int[] count = new int[max - min + 1];
    for(int v : a) {
        count[v - min]++;
    }
    int k = 0;
    for(int i = 0; i < count.length; i++) {
        while(count[i] > 0) {
            a[k++] = i + min;
            count[i]--;
        }
    }
}
```

#### 桶排序

```java
public void sort(int[] ages) {
    // 1.准备桶
    DynamicArray[] buckets = new DynamicArray[10];
    for(int i = 0; i < buckets.length; i++) {
        buckets[i] = new DynamicArray();
    }
    // 2.放入数据
    for(int age : ages) {
        buckets[age / 10].addLast(age);
    }
    int k = 0;
    for(DynamicArray bucket : buckets) {
        // 3.排序桶内元素
        int[] array = bucket.array();
        InsertionSort.sort(array);
        // 4.放入原始数组
        for(int v : array) {
            ages[k++] = v;
        }
    }
}

// 改进
public void sort(int[] ages, int range) {
    int max = a[0];
    int min = a[0];
    for(int i = 1; i < a.length; i++) {
        if(a[i] > max) {
            max = a[i];
        }
        if(a[i] < min) {
            min = a[i];
        }
    }
    DynamicArray[] buckets = new DynamicArray[(max - min) / range + 1];
    for(int i = 0; i < buckets.length; i++) {
        buckets[i] = new DynamicArray();
    }
    // 2.放入数据
    for(int age : ages) {
        buckets[(age - min) / range].addLast(age);
    }
    int k = 0;
    for(DynamicArray bucket : buckets) {
        // 3.排序桶内元素
        int[] array = bucket.array();
        InsertionSort.sort(array);
        // 4.放入原始数组
        for(int v : array) {
            ages[k++] = v;
        }
    }
}
```

#### 基数排序

```java
public void radixSort(String[] a, int length) {
    // 1.准备桶
    ArrayList<String>[] buckets = new ArrayList<>(10);
    for(int i = 0; i < buckets.length; i++) {
        buckets[i] = new ArrayList<>();
    }
    for(int i = length - 1; i >= 0 ; i--) {
        for(String s : a) {
            buckets[s.charAt(i) - '0'].add(s);
        }
        int k = 0;
        for(ArrayList<String> bucket : buckets) {
            for(String s : bucket) {
                a[k++] = s;
            }
            bucket.clear();
        }
    }
}
```

#### Java中的排序

Arrays.sort

JDK7~13中的排序实现

| 排序目标                      | 条件                                       | 采用算法             |
| ----------------------------- | ------------------------------------------ | -------------------- |
| int[] long[] float[] double[] | size < 47                                  | 混合插入排序（pair） |
|                               | size < 286                                 | 双基准点快排         |
|                               | 有序度高                                   | 归并排序             |
|                               | 有序度低                                   | 双基准点快排         |
| byte[]                        | size > 29                                  | 计数排序             |
|                               | size <= 29                                 | 插入排序             |
| char[] short[]                | size > 3200                                | 计数排序             |
|                               | size < 47                                  | 插入排序             |
|                               | size < 286                                 | 双基准点快排         |
|                               | 有序度高                                   | 归并排序             |
|                               | 有序度低                                   | 双基准点快排         |
| Object[]                      | -Djava.util.Arrays.useLegacyMergeSort=true | 传统归并排序         |
|                               |                                            | TimSort              |

JDK14~20中的排序实现

| 排序目标                      | 条件                                          | 采用算法            |
| ----------------------------- | --------------------------------------------- | ------------------- |
| int[] long[] float[] double[] | size < 65 并不是最左侧                        | 混合插入排序（pin） |
|                               | size < 44 并位于最左侧                        | 插入排序            |
|                               | 递归次数超过384                               | 堆排序              |
|                               | 对于整个数组或非最左侧size > 4096，若接近有序 | 归并排序            |
|                               | 不是上面情况                                  | 双基准点快排        |
| byte[]                        | size > 64                                     | 计数排序            |
|                               | size <= 64                                    | 插入排序            |
| char[] short[]                | size > 1750                                   | 计数排序            |
|                               | size < 44                                     | 插入排序            |
|                               | 递归次数超过384                               | 计数排序            |
|                               | 不是上面情况                                  | 双基准点快排        |
| Object[]                      | -Djava.util.Arrays.useLegacyMergeSort=true    | 传统归并排序        |
|                               |                                               | TimSort             |

+ 其中TimSort是用归并+二分插入排序的混合排序算法
+ 值得注意的是从JDK8开始支持Arrays.parallelSort并行排序
+ 根据最新的提交记录来看JDK21可能会引入基数排序等优化

### 图

#### 基本概念

图是由**顶点（vertex）**和**边（edge）**组成的数据结构

**有向图**边是单向的，**无向图**边是双向的

**度**是指与该顶点相邻的边的数量，有向图中细分为**入度**和**出度**

**权**代表从源顶点到目标顶点的距离、费用、时间或其他度量

**路径**被定义为从一个顶点到另一个顶点的一系列连续边

**路径长度**

+ 不考虑权重，长度就是边的数量
+ 考虑权重，一般就是权重累加

**环**在有向图中，从一个顶点开始，可以通过若干条有向边返回到该顶点，那么就形成了一个环

**图的连通性**

如果两个顶点之间存在路径，则这两个顶点是联通的，所有顶点都连通，则该图被称之为连通图，若子图联通，则称为连通分量

**图的表示**

```
   B
  / \
A     D
  \ /
   C
   
邻接矩阵表示
  A B C D
A 0 1 1 0
B 1 0 0 1
C 1 0 0 1
D 0 1 1 0

邻接表表示
A -> B -> C
B -> A -> D
C -> A -> D
D -> B -> C

 -> B ->
|       |
A       D
|       |
 -> C ->
 
邻接矩阵表示
  A B C D
A 0 1 1 0
B 0 0 0 1
C 0 0 0 1
D 0 0 0 0

邻接表表示
A - B - C
B - D
C - D
D - empty
```

**Java表示**

```java
public class Vertex {
    
    String name;
    List<Edge> edges;
    boolean visited; // 是否被访问过，用于BFS和DFS
    int inDegree; // 入度
    int status; // 状态 0-未访问 1-访问中 2-访问过，用在拓扑排序
    
    int dist = INF; // 距离
    static final Integer INF = Integer.MAX_VALUE;
    Vertex prev = null;
    
    public Vertex(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return name + '(' + dist + ')';
    }
    
    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return Objects.equals(name, vertex.name);
    }
    
    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}

public class Edge {
    
    Vertex linked;
    int weight;
    
    public Edge(Vertex linked) {
        this(linked, 1);
    }
    
    public Edge(Vertex linked, int weight) {
        this.linked = linked;
        this.weight = weight;
    }
}

// 深度优先遍历
public void dfs(Vertex v) {
    v.visited = true;
    System.out.println(v.name);
    for(Edge edge : v.edges) {
        if(!edge.linked.visited) {
            dfs(edge.linked);
        }
    }
}

public void dfs(Vertex v) {
    LinkedList<Vertex> stack = new LinkedList<>();
    stack.push(v);
    while(!stack.isEmpty) {
        Vertex pop = stack.pop();
        pop.visited = true;
        System.out.println(pop.name);
        for(Edge edge : pop.edges){
            if(!edge.linked.visited) {
                stack.push(edge.linked);
            }
        }
    }
}

// 广度优先遍历
public void bfs(Vertex v) {
    LinkedList<Vertex> queue = new LinkedList<>();
    queue.offer(v);
    v.visited = true;
    while(!queue.isEmpty) {
        Vertex poll = queue.poll();
        System.out.println(poll.name);
        for(Edge edge : poll.edges){
            if(!edge.linked.visited) {
                edge.linked.visited = true;
                queue.offer(edge.linked);
            }
        }
    }
}
```

#### 拓扑排序

拓扑排序主要用来解决有向无环图（DAG 图）的所有节点的排序。简单来说就是找到做事情的先后顺序，拓扑排序的结果可能不是唯一的

![](/img/cb_1.png)

```java
public void topologicalSort() {
    Vertex v1 = new Vertex("网页基础");
    Vertex v2 = new Vertex("Java基础");
    Vertex v3 = new Vertex("JavaWeb");
    Vertex v4 = new Vertex("Spring框架");
    Vertex v5 = new Vertex("微服务框架");
    Vertex v6 = new Vertex("数据库");
    Vertex v7 = new Vertex("实战项目");
    
    v1.edges = List.of(new Edge(v3));
    v2.edges = List.of(new Edge(v3));
    v3.edges = List.of(new Edge(v4));
    v4.edges = List.of(new Edge(v4));
    v5.edges = List.of(new Edge(v5));
    v6.edges = List.of(new Edge(v7));
    v7.edges = List.of();
    
    List<Vertex> graph = List.of(v1, v2, v3, v4, v5, v6, v7);
    // 1.统计每个顶点的入度
    for(Vertex v : graph) {
        for(Edge edge : v.edges) {
            edge.linked.inDegree++;
        }
    }
    // 2.将入度为0的顶点加入队列
    LinkedList<Vertex> queue = new LinkedList<>();
    for(Vertex v : graph) {
        if(v.inDegree == 0) {
            queue.offer(v);
        }
    }
    // 3.队列中不断移除顶点，每移除一个顶点，把它相邻顶点入度减1，若减到0再入队
    List<String> result = new ArrayList<>();
    while(!queue.isEmpty()) {
        Vertex poll = queue.poll();
        result.add(poll.name);
        for(Edge edge : poll.edges) {
            edge.linked.inDegree--;
            if(edge.linked.inDegree == 0) {
                queue.offer(edge.linked);
            }
        }
    }
    if(result.size() != graph.size()) {
        System.out.println("发现了环");
    }
    
    // DFS
    List<Vertex> graph = List.of(v1, v2, v3, v4, v5, v6, v7);
    LinkedList<String> stack = new LinkedList<>();
    for(Vertex v : graph) {
        dfs(v, stack);
    }
    
    public void dfs(Vertex v, LinkedList<String> stack) {
        if(v.status == 2) {
            return;
        }
        if(v.status == 1) {
            throw new RuntimeException("发现了环");
        }
        v.status = 1;
        for(Edge edge : v.edges) {
            dfs(edge.linked. stack);
        }
        v.status = 2;
        stack.push(v.name);
    }
}
```

#### Dijkstra算法（单源最短路径算法）

1. 将所有顶点标记为未访问。创建一个未访问顶点的集合。
2. 为每个顶点分配一个临时距离值
   + 对于我们的初始顶点，将其设置为零
   + 对于所有其他顶点，将其设置为无穷大
3. 每次选择最小临时距离的未访问顶点，作为新的当前顶点
4. 对于当前顶点，遍历其所有未访问的邻居，并更新他们的临时距离为更小
   + 例如，1->6的距离是14，而1->3->6的距离是11，这时将距离更新为11
   + 否则，将保留上次距离值
5. 当前顶点的邻居处理完后，把它从未访问集合中删除

![](/img/cb_2.png)

```java
public class Dijkstra {
    public static void main(String[] args) {
        Vertex v1 = new Vertex("v1");
        Vertex v2 = new Vertex("v2");
        Vertex v3 = new Vertex("v3");
        Vertex v4 = new Vertex("v4");
        Vertex v5 = new Vertex("v5");
        Vertex v6 = new Vertex("v6");

        v1.edges = List.of(new Edge(v3, 9), new Edge(v2, 7), new Edge(v6, 14));
        v2.edges = List.of(new Edge(v4, 15));
        v3.edges = List.of(new Edge(v4, 11), new Edge(v6, 2));
        v4.edges = List.of(new Edge(v5, 6));
        v5.edges = List.of();
        v6.edges = List.of(new Edge(v5, 9));

        List<Vertex> graph = List.of(v1, v2, v3, v4, v5, v6);
        dijkstra(graph, v1);
    }
    
    private static void dijkstra(List<Vertex> graph, Vertex source) {
        PriorityQueue<Vertex> queue = new PriorityQueue<>(Comparator.comparingInt()v -> v.dist);
        source.dist = 0;
        for(Vertex v : graph) {
            queue.offer(v);
        }
        
        while(!queue.isEmpty()) {
            // 3.选取当前顶点
            Vertex curr = queue.peek();
            // 4.更新当前顶点邻居距离
            if(!curr.visted) {
                updateNeighboursDist(curr, queue);
                curr.visited = true;
            }
            // 5.移除当前顶点
            queue.poll();
        }
        
        for(Vertex v : graph) {
            System.out.println(v.name + " " + v.dist + " " + (v.prev != null ? v.prev.name : "null"));
        }
    }
    
    private static Vertex updateNeighboursDist(Vertex curr, PriorityQueue<Vertex> queue) {
        for(Edge edge : curr.edges) {
            Vertex n = edge.linked;
            if(!n.visited) {
                int dist = curr.dist + edge.weight;
                if(dist < n.dist) {
                    n.dist = dist;
                    n.prev = curr;
                    queue.offer(n);
                }
            }
        }
    }
}
```

#### Bellman-Ford算法（处理负边）

Dijkstra不能处理负边，Bellman-Ford算法可以处理负边，但不能处理负环的情况

![](/img/cb_3.png)

```java
public class BellmanFord {
    public static void main(String[] args) {
        Vertex v1 = new Vertex("v1");
        Vertex v2 = new Vertex("v2");
        Vertex v3 = new Vertex("v3");
        Vertex v4 = new Vertex("v4");

        v1.edges = List.of(new Edge(v2, 2), new Edge(v3, 1));
        v2.edges = List.of(new Edge(v3, -2));
        v3.edges = List.of(new Edge(v4, 1));
        v4.edges = List.of();

        List<Vertex> graph = List.of(v1, v2, v3, v4);
        bellmanFord(graph, v1);
    }
    
    private static void bellmanFord(List<Vertex> graph, Vertex source) {
        source.dist = 0;
        int size = graph.size();
        // 1.进行顶点个数 - 1轮处理
        for(int i = 0; i < size - 1; i++) {
            // 2.遍历所有的边
            for(Vertex s : graph) {
                for(Edge edge : s.edges) {
                    // 3.处理每一条边
                    Vertex e = edge.linked;
                    if(s.dist != Integer.MAX_VALUE && s.dist + edge.weight < e.dist) {
                        e.dist = s.dist + edge.weight;
                        e.prev = s;
                    }
            	}
            } 
        }
        for(Vertex v : graph) {
            System.out.println(v + " " + (v.prev != null ? v.prev.name : "null"));
        }
    }
}
```

**负环情况**

![](/img/cb_4.png)

#### Floyd-Warshall算法（多源最短路径算法）

![](/img/cb_5.png)

```java
public class FloydWarshall {
    public static void main(String[] args) {
        Vertex v1 = new Vertex("v1");
        Vertex v2 = new Vertex("v2");
        Vertex v3 = new Vertex("v3");
        Vertex v4 = new Vertex("v4");

        v1.edges = List.of(new Edge(v3, -2));
        v2.edges = List.of(new Edge(v1, 4), new Edge(v3, 3));
        v3.edges = List.of(new Edge(v4, 2));
        v4.edges = List.of(new Edge(v2, -1));

        List<Vertex> graph = List.of(v1, v2, v3, v4);
        floydWarshall(graph);
    }
    
    static void floydWarshall(List<Vertex> graph) {
        int size = graph.size();
        int[][] dist = new int[size][size];
        Vertex[][] prev = new Vertex[size][size];
        // 1.初始化
        for(int i = 0; i < size; i++) {
            Vertex v = graph.get(i);
            Map<Vertex, Integer> map = v.edges.stream().collect(Collectors.toMap(e -> e.linked, e -> e.weight));
            for(int j = 0; j < size; j++) {
                Vertex u = graph.get(j);
                if(v == u) {
                    dist[i][j] == 0;
                } else {
                    dist[i][j] = map.getOrDefault(u, Integer.MAX_VALUE);
                    prev[i][j] = map.get(u) != null ? v : null;
                }
            }
        }
        print(dist);
        print(prev);
        
        // 2.看能否借路到达其他顶点
        for(int k = 0; k < size; k++) {
            for(int i = 0; i < size; i++) {
            	for(int j = 0; j < size; j++) {
                    // dist[i][k] + dist[k][j] i行的顶点，借助k顶点，到达j列顶点
                    if(dist[i][k] != Integer.MAX_VALUE && dist[k][j] != Integer.MAX_VALUE && dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        prev[i][j] = prev[k][j];
                    }
        		}
        	}
            print(dist);
        }
        print(prev);
        for(int i = 0; i < size; i++) {
            for(int i = 0; i < size; i++) {
               path(prev, graph, i, j);
        	}   	 
        }
    }
    
    static void path(Vertex[][] prev, List<Vertex> graph, int i, int j) {
        LinkedList<String> stack = new LinkedList<>();
        System.out.println("[" + graph.get[i].name + "," + graph.get(j).name + "]");
        satck.push(graph.get(j).name);
        while(i != j) {
            Vertex p = prev[i][j];
            stack.push(p.name);
            j = garph.indexOf(p);
        }
        System.out.println(stack);
    }
    
    static void print(int[][] dist) {
        System.out.println("--------------------");
        for(int[] row : dist) {
            System.out.println(Arrays.stream(row).boxed()
                              .map(x -> x == Integer.MAX_VALUE ? "∞" : String.valueOf(x))
                              .map(s -> String.format("%2s", s))
                              .collect(Collectors.joining(",", "[", "]")));
        }
    }
    
    static void print(Vertex[][] prev) {
        System.out.println("--------------------");
        for(Vertex[] row : prev) {
            System.out.println(Arrays.stream(row).map(v -> v == null ? "null" : v.name)
                              .map(s -> String.format("%5s", s))
                              .collect(Collectors.joining(",", "[", "]")));
        }
    }
}
```

#### Prim算法（最小生成树算法）

![](/img/cb_6.png)

```java
public class Prim {
    public static void main(String[] args) {
        Vertex v1 = new Vertex("v1");
        Vertex v2 = new Vertex("v2");
        Vertex v3 = new Vertex("v3");
        Vertex v4 = new Vertex("v4");
        Vertex v5 = new Vertex("v5");
        Vertex v6 = new Vertex("v6");
        Vertex v7 = new Vertex("v7");

        v1.edges = List.of(new Edge(v2, 2), new Edge(v3, 4), new Edge(v4, 1));
        v2.edges = List.of(new Edge(v1, 2), new Edge(v4, 3), new Edge(v5, 10));
        v3.edges = List.of(new Edge(v1, 4), new Edge(v4, 2), new Edge(v6, 5));
        v4.edges = List.of(new Edge(v1, 1), new Edge(v2, 3), new Edge(v3, 2), new Edge(v5, 7), new Edge(v6, 8), new Edge(v7, 4));
        v5.edges = List.of(new Edge(v2, 10), new Edge(v4, 7), new Edge(v7, 6));
        v6.edges = List.of(new Edge(v3, 5), new Edge(v4, 8), new Edge(v7, 1));
        v7.edges = List.of(new Edge(v4, 4), new Edge(v5, 6), new Edge(v6, 1));

        List<Vertex> graph = List.of(v1, v2, v3, v4, v5, v6, v7);
        prim(graph, v1);
    }
    
    private static void prim(List<Vertex> graph, Vertex source) {
        PriorityQueue<Vertex> queue = new PriorityQueue<>(Comparator.comparingInt()v -> v.dist);
        source.dist = 0;
        for(Vertex v : graph) {
            queue.offer(v);
        }
        
        while(!queue.isEmpty()) {
            // 3.选取当前顶点
            Vertex curr = queue.peek();
            // 4.更新当前顶点邻居距离
            if(!curr.visted) {
                updateNeighboursDist(curr, queue);
                curr.visited = true;
            }
            // 5.移除当前顶点
            queue.poll();
        }
        
        for(Vertex v : graph) {
            System.out.println(v.name + " " + v.dist + " " + (v.prev != null ? v.prev.name : "null"));
        }
    }
    
    private static Vertex updateNeighboursDist(Vertex curr, PriorityQueue<Vertex> queue) {
        for(Edge edge : curr.edges) {
            Vertex n = edge.linked;
            if(!n.visited) {
                // 和Dijkstra区别
                int dist = edge.weight;
                if(dist < n.dist) {
                    n.dist = dist;
                    n.prev = curr;
                    queue.offer(n);
                }
            }
        }
    }
}
```

#### Kruskal算法（最小生成树算法）

![](/img/cb_6.png)

```java
public class Kruskal {
    static class Edge implements Comparable(Edge) {
        List<Vertex> vertices;
        int start;
        int end;
        int weight;
        
        public Edge(List<Vertex> vertices, int start, int end, int weight) {
            this.vertices = vertices;
            this.start = start;
            this.end = end;
            this.weight = weight;
        }
        
        public Edge(int start, int end, int weight) {
            this.start = start;
            this.end = end;
            this.weight = weight;
        }
        
        @Override
        public int compareTo(Edge O) {
            return Integer.compare(this.weight, o.weight);
        }
        
        @Override
        public String toString() {
            return vertices.get(start).name + "<->" + vertices.get(end).name + "(" + weight + ")";
        }
    }
    
    public static void main(String[] args) {
        Vertex v1 = new Vertex("v1");
        Vertex v2 = new Vertex("v2");
        Vertex v3 = new Vertex("v3");
        Vertex v4 = new Vertex("v4");
        Vertex v5 = new Vertex("v5");
        Vertex v6 = new Vertex("v6");
        Vertex v7 = new Vertex("v7");
        List<Vertex> vertices = List.of(v1, v2, v3, v4, v5, v6, v7);
        
        PriorityQueue<Edge> queue = new PriorityQueue<>(List.of(
            new Edge(vertices, 0, 1, 2),
        	new Edge(vertices, 0, 2, 4), 
            new Edge(vertices, 0, 3, 1),
            new Edge(vertices, 1, 3, 3),
            new Edge(vertices, 1, 4, 10),
            new Edge(vertices, 2, 3, 2),
            new Edge(vertices, 2, 5, 5),
            new Edge(vertices, 3, 4, 7),
            new Edge(vertices, 3, 5, 8),
            new Edge(vertices, 3, 6, 6),
            new Edge(vertices, 4, 6, 6),
            new Edge(vertices, 5, 6, 1)
        ));
        kruskal(vertices.size(), queue);
    }
    
    static void kruskal(int size, PriorityQueue<Edge> queue) {
        List<Edge> list = new ArrayList<>();
        DisjointSet set = new DisjointSet(size);
        while(list.size() < size - 1) {
            Edge poll = queue.poll();
            int i = set.find(poll.start);
            int j = set.find(poll.end);
            if(i != j) { // 未相交
                list.add(poll);
                set.union(i, j); // 相交
            }
        }
        
        for(Edge edge : list) {
            System.out.println(edge);
        }
    }
}
```

**不相交集合（并查集合）**

```java
public class DisjointSet {
    // 索引对应顶点
    // 元素是用来表示与之有关系的顶点
    int[] s;
    int[] size;
    
    public DisjointSet(int size) {
        s = new int[size];
        for(int i = 0; i < size; i++) {
            s[i] = i;
            this.size[i] = 1;
        }
    }
    
    // 找到老大
    public int find(int x) {
        if(x == s[x]) {
            return x;
        } else {
            return s[x] = find(s[x]);
        }
    }
    
    // 让两个集合相交，即选出新老大，x，y是原老大索引
    public void union(int x, int y) {
        if(size[x] < size[y]) {
            int t = x;
            x = y;
            y = t;
        }
        s[y] = x;
        size[x] = size[x] + size[y]; // 更新老大元素个数
    }
    
    @Override
    public String toString() {
        return "内容：" + Arrays.toString(s) + "\n大小：" + Arrays.toString(size);
    }
}
```

### 贪心算法

1. 将寻找最优解的问题分为若干个步骤
2. 每一步骤都采用贪心原则，选取当前最优解
3. 因为没有考虑所有可能，局部最优的堆叠不一定让最终解最优

Dijkstra、Prim、Kruskal都可以算作贪心算法

其他例子：选择排序、堆排序、拓扑排序、并查集合中的union by size和union by height、哈夫曼编码、钱币找零、任务编排、近似算法

| 问题名称             | 是否能用贪心得到最优解 | 替换解法     |
| -------------------- | ---------------------- | ------------ |
| Dijkstra(不存在负边) | 是                     |              |
| Dijkstra(存在负边)   | 否                     | Bellman-Ford |
| Prim                 | 是                     |              |
| Kruskal              | 是                     |              |
| 零钱兑换             | 否                     | 动态规划     |
| Huffman树            | 是                     |              |
| 活动选择问题         | 是                     |              |
| 分数背包问题         | 是                     |              |
| 0-1背包问题          | 否                     | 动态规划     |

#### 零钱兑换

leetcode518，可以凑成总金额所需的所有组合可能数

```java
// 暴力递归
public class LeetCode518 {
    // coins由大到小排列效率更高
    public int coinChange(int[] coins, int amount) {
        return rec(0, coins, amount, new LinkedList<>(), true);
    }
    
    // index-当前硬币索引，coins-硬币面值数组，remainder-剩余金额，stack，first-第一次调用
    public int rec(int index, int[] coins, int remainder, LinkedList<Integer> stack, boolean first) {
        if(!first) {
            stack.push(coins[index]);
        }
        int count = 0;
        if(remainder < 0) { // 情况1：剩余金额 < 0 无解
            System.out.println("无解" + stack);
        } else if(remainder == 0) { // 情况3：剩余金额 == 0 有解
            System.out.println("有解" + stack);
            count = 1;
        } else { // 情况2：剩余金额 > 0 继续递归
            int count = 0;
            for(int i = index; i < coins.length; i++) {
                count += rec(i, coins, remainder - coins[i], stack, false);
            }
        }
        if(!stack.isEmpty()) {
            stack.pop();
        }
        return count;
    }
}
```

#### 零钱兑换2

leetcode322，凑成总金额的凑法中，需要硬币最少个数是几

```java
// 暴力递归
public class Leetcode322 {
    static int min = -1; // 需要的最少硬币数
    public int coinChange(int[] coins, int amount) {
        rec(0, coins, amount, new AtomicInteger(-1), new LinkedList<>(), true);
        return min;
    }
    
    // count 代表某一组合钱币的总数
    public void rec(int index, int[] coins, int remainder, AtomicInteger count, LinkedList<Integer> stack, boolean first) {
        if(!first) {
            stack.push(coins[index]);
        }
        count.incrementAndGet();
        if(remainder == 0) {
            System.out.println(stack);
            if(min == -1) {
                min = count.get();
            } else {
                min = Integer.min(min, count.get());
            }
        } else if(remainder > 0) {
            for(int i = index; i < coins.length; i++) {
                count += rec(i, coins, remainder - coins[i], count, stack, false);
            }
        }
        count.decrementAndGet();
        if(!stack.isEmpty()) {
            stack.pop();
        }
    }
}

// 贪心算法：可能得到错误的解
public class Leetcode322 {
    // coins从大到小排列
    public int coinChange(int[] coins, int amount) {
        // 每次循环找到当前最优解：面值最大的硬币，它凑出来的硬币数最小
        int remainder = amount;
        int count = 0;
        for(int coin : coins) {
            while(remainder > coin) {
                remainder -= coin;
                count++;
            }
            if(remainder == coin) {
                remiander = 0;
                count++;
                break;
            }
        }
        if(remainder > 0) {
            return -1;
        } else {
            return count;
        }
    }
}
```

#### Huffman编码

```java
public class HuffmanTree {
    
    // Huffman树的构建过程
    // 1.将统计了出现频率的字符，放入优先级队列
    // 2.每次出队两个频次最低的元素，给它俩找个爹
    // 3.把爹重新放入队列，重复2~3
    // 4.当队列只剩一个元素时，Hudffman树构建完成
    
    static class Node {
        Character ch; // 字符
        int freq; // 频次
        Node left;
        Node right;
        String code; // 编码
        
        public Node(Character ch) {
            this.ch = ch;
        }
        
        public Node(int freq, Node left, Node right) {
            this.freq = freq;
            this.left = left;
            this.right = right;
        }
        
        int freq() {
            return freq;
        }
        
        boolean isLeaf() {
            return left == null;
        }
        
        @Override
        public String toString() {
            return "Node{" + "ch=" + ch + ", freq=" + freq + "}";
        }
    }
    
    String str;
    Map<Character, Node> map = new HashMap<>();
    Node root;
    
    public HuffmanTree(String str) {
        this.str = str;
        // 功能1：统计频率
        char[] chars = str.toCharArray();
        for(char c : chars) {
            Node node = map.computeIfAbsent(c, Node::new);
            node.freq++;
        }
        
        // 功能2：构造树
        PriorityQueue<Node> queue = new PriorityQueue(
            Comparator.comparingInt(Node::freq)
        );
        queue.addAll(map.values());
        while(queue.size() >= 2) {
            Node x = queue.poll();
            Node y = queue.poll();
            int freq = x.freq + y.freq;
            queue.offer(new Node(freq, x, y));
        }
        root = queue.poll();
        // 功能3：计算每个字符的编码，功能4：字符串编码后占用bits
        int sum = dfs(root, new StringBuilder());
        for(Node node : map.values()) {
            System.out.println(node + " " + node.code);
        }
        System.out.println("总共会占用bits:" + sum);
    }
    
    private int dfs(Node node, StringBuilder code) {
        int sum = 0;
        if(node.isLeaf()) {
            // 找到编码
            node.code = code.toString();
            sum = node.freq * code.length();
        } else {
            sum += dfs(node.left, code.append("0"));
            sum += dfs(node.right, code.append("1")); 
        }
        if(code.length() > 0) {
            code.deleteCharAt(code.length() - 1);
        }
        return sum;
    }
    
    // 编码
    public String encode() {
        char[] chars = str.toCharArray();
        StringBuilder sb = new StringBuilder();
        for(char c : chars) {
            sb.append(map.get(c).code);
        }
        return sb.toString();
    }
    
    // 解码
    public String decode(String str) {
        // 从根节点，寻找数字对应的字符
        // 数字是 0 向左走
        // 数字是 1 向右走
        // 如果没走到头，每走一步数字的索引i++，走到头就可以找到解码字符，再将node重置为根节点
        char[] chars = str.toCharArray();
        int i = 0;
        StringBuilder sb = new StringBuilder();
        while(i < chars.length) {
            if(!node.isLeaf()) { // 非叶子
                if(chars[i] == '0') { // 向左走
                    node = node.left;
                } else if(chars[i] == '1') { // 向右走
                    node = node.right;
                }
                i++;
            }
            if(node.isLeaf()) {
                sb.append(node.ch);
                node = root;
            }
        }
        return sb.toString();
    }
    
    public static void main(String[] args) {
        HuffmanTree tree = new HuffmanTree("abbccccccc");
        String encoded = tree.encode();
        System.out.println(encoded);
    }
}
```

#### 活动选择问题

Leetcode435

要在一个会议室举办n个活动，每个活动有它们各自的起始和结束时间，找出在时间上互不冲突的活动组合，能够最充分利用会议室（举办的活动次数最多）

几种贪心策略：

1. 优先选择持续时间最短的活动
2. 优先选择冲突最少的活动
3. 优先选择最先开始的活动
4. 优先选择最先结束的活动

```java
// 优先选择最先结束的活动
public class ActivitySelectionProblem {
    
    static class Activity {
        int index;
        int start;
        int finish;
        
        public Activity(int index, int start, int finish) {
            this.index = index;
            this.start = start;
            this.finish = finish;
        }
        
        public int getFinish() {
            return finish;
        }
        
        @Override
        public String toString() {
            return "Activity(" + index + ")";
        }
    }
    
    public static void main(String[] args) {
        Activity[] activities = new Activity[] {
            new Activity(1, 2, 4),
            new Activity(0, 1, 3),
            new Activity(2, 3, 5)
        };
        Arrays.sort(activities, Comparator.comparingInt(Activity::getFinish));
        
        select(activities, activities.length);
    }
    
    public static void select(Activity[] activities, int n) {
        List<Activity> result = new ArrayList<>();
        Activity prev = activities[0]; // 上次被选中的活动
        result.add(prev);
        for(int i = 1; i < n; i++) {
            Activity curr = activities[i]; // 正在处理的活动
            if(curr.start >= prev.finish) {
                result.add(curr);
                prev = curr;
            }
        }
    }
}
```

#### 分数背包问题

1. n个物品都是液体，有重量和价值
2. 现在你要取走10升的液体
3. 每次可以不拿，全拿，或拿一部分，问最高价值是多少

```java
public class FractionKnapsackProblem {
    
    static class Item {
        int index;
        int weight;
        int value;
        
        public Item(int index, int weight, int value) {
            this.index = index;
            this.weight = weight;
            this.value = value;
        }
        
        public int unitValue() {
            return value / weight;
        }
        @Override
        public String toString() {
            return "Item(" + index + ")";
        }
    }
    
    public static void main(String[] args) {
        Item[] items = new Item[]{
            new Item(0, 4, 24),
            new Item(1, 8, 160),
            new Item(2, 2, 4000),
            new Item(3, 6, 108),
            new Item(4, 1, 4000),
        };
        select(items, 10);
    }
    
    static void select(Item[] items, int total) {
        Arrays.sort(items, Comparator.comparingInt(Item::unitValue).reversed());
        int max = 0; // 最大值
        for(Item item : items) {
            if(total >= item.weight) { // 可以拿完
                total -= item.weight;
                max += item.value;
            } else { // 拿不完
                max += total * item.unitValue();
                break;
            }
        }
        System.out.println("最大价值是：" + max);
    }
}
```

### 动态规划

Dynamic-Programming 动态编程

Programming：在这里指用数学方法来根据子问题求解当前问题（通俗理解就是找到递推公式）

Dynamic：指缓存上一步结果，根据上一步结果计算当前结果（多阶段进行）

合在一起：将找出递推公式，将当前问题分解成子问题，分阶段进行求解，求解过程缓存子问题的解，避免重复计算

#### 斐波那契

```java
public class Fibonacci {
    // 要点1：从已知子问题的解，推导出当前问题的解，推导过程可以表示为一个数学公式
    // 要点2：用一维或二维数组来保存之前的计算结果（可以进一步优化）
    public static int fibonacci(int n) {
        if(n == 0) {
            return 0;
        }
        if(n == 1) {
            return 1;
        }
        int a = 0;
        int b = 1;
        for(int i = 2; i <= n; i++) {
            int c = a + b;
            a = b;
            b = c;
        }
        return b;
    }
}
```

#### Bellman-Ford

![](/img/cb_7.png)

```java
public class BellmanFord {
    
    // f(v)用来表示从起点出发，到达v这个顶点的最短距离
    // 初始时 f(v) = 0 当v==起点时，f(v) = ∞ 当v!=起点时
    // 之后 f(to) = min(f(to), f(from) + from.weight)
    
    public static void main(String[] args) {
        List<Edge> edges = List.of(
            new Edge(vertices, 6, 5, 9),
        	new Edge(vertices, 4, 5, 6), 
            new Edge(vertices, 1, 6, 14),
            new Edge(vertices, 3, 6, 2),
            new Edge(vertices, 3, 4, 11),
            new Edge(vertices, 2, 4, 15),
            new Edge(vertices, 1, 3, 9),
            new Edge(vertices, 1, 2, 7)
        );
        
        int[] dp = new int[7]; // 一维数组用来缓存结果
        dp[1] = 0;
        for(int i = 2; i < dp.length; i++) {
            dp[i] = Integer.MAX_VALUE;
        }
        for(int i = 0; i < 5; i++) {
            for(Edge e : edges) {
                if(dp[e.from] != Integer.MAX_VALUE) {
                    dp[e.to] = Integer.min(dp[e.to], dp[e.from] + e.weight);
                }
            }
        }
    }
}
```

#### Leetcode-63 不同路径

机器人要从左上角走到右下角，每次只能向右或向下，问一共有多少条不同路径

![](/img/cb_8.png)

```java
public class UniquePaths {
    public static void main(String[] args) {
        int count = new UniquePaths().uniquePaths(3, 7);
        System.out.println(count);
    }
    
    // 二维数组
    public int uniquePahts(int m, int n) {
        int[][] dp = new int[m][n];
        for(int i = 0; i < m; i++) {
            dp[i][0] = 1;
        }
        for(int j = 0; j < n; j++) {
            dp[0][j] = 1;
        }
        for(int i = 1; i < m; i++) {
            for(int j = 1; j < n; j++) {
                dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
            }
        }
        return dp[m - 1][n - 1];
    }
    
    // 优化为一维数组
    public int uniquePahts(int m, int n) {
        int[] dp = new int[n];
        for(int j = 0; j < n; j++) {
            dp[j] = 1;
        }
        for(int i = 1; i < m; i++) {
            dp[0] = 1;
            for(int j = 1; j < n; j++) {
                dp[j] = dp[j] + dp[j - 1];
            }
        }
        return dp[n - 1];
    }
}
```

#### 0-1背包问题

1. n个物品都是固体，有重量和价值
2. 现在你要取走10克的物品
3. 每次可以不拿或全拿，问最高价值是多少

```java
public class KnapsackProblem {
    public static void main(String[] args) {
        Item[] items = new Item[]{
            new Item(1, "黄金", 4, 1600),
            new Item(2, "宝石", 8, 2400),
            new Item(3, "白银", 5, 30),
            new Item(4, "钻石", 1, 10000)
        }
    }
    
    // 二维数组
    static int select(Item[] items, int total) {
        int[][] dp = new int[item.length][total + 1];
        Item item0 = items[0];
        for(int j = 0; j < total + 1; j++) {
            if(j >= item0.weight) {
                dp[0][j] = item0.value; // 装得下
            } else {
                dp[0][j] = 0; // 装不下
            }
        }
        for(int i = 1; i < dp.length; j++) {
            Item item = items[i];
            for(int j = 0; j < total + 1; j++) {
                if(j >= item.weight) {
                    dp[i][j] = Integer.max(dp[i - 1][j], item.value + dp[i - 1][j - item.weight]); // 装得下
                } else {
                    dp[i][j] = dp[i - 1][j]; // 装不下
                }
            }
        }
        return dp[dp.length - 1][total];
    }
    
    // 优化为一维数组
    static int select(Item[] items, int total) {
        int[] dp = new int[total + 1];
        Item item0 = items[0];
        for(int j = 0; j < total + 1; j++) {
            if(j >= item0.weight) {
                dp[j] = item0.value; // 装得下
            } else {
                dp[j] = 0; // 装不下
            }
        }
        for(int i = 1; i < items.length; j++) {
            Item item = items[i];
            for(int j = total; j > 0; j--) {
                if(j >= item.weight) {
                    dp[j] = Integer.max(dp[j], item.value + dp[j - item.weight]); // 装得下
                }
            }
        }
        return dp[total];
    }
}
```

### 分治

分治思想

+ 将大问题划分为两个到多个子问题
+ 子问题可以继续拆分为更小的子问题，直到能够简单求解
+ 如有必要，将子问题的解进行合并，得到原始问题的解

经典分而治之的列子：二分查找、快速排序、归并排序、合并K个排序链表

对比动态规划

+ 都需要拆分子问题
+ 动态规划的子问题有重叠，因此需要记录之前的子问题解，避免重复运算
+ 分而治之的子问题无重叠

#### 快速选择算法

求排在第i名的元素，i从0开始，由小到大排

```java
public class QuickSelect {
    
    static int quick(int[] array, int left, int right, int i) {
        int p = partition(array, left, right);
        if(p == i) {
            return array[p];
        }
        if(i < p) { // 到左边找
            return quick(array, left, p - 1, i);
        } else { // 到右边找
            return quick(array, p + 1, right, i);
        }
    }
    
    public int partition(int[] a, int left, int right) {
        int idx = ThreadLocalRandom.current().nextInt(right - left + 1) + left;
        swap(a, idx, left);
        int pv = a[left]; // 基准点元素
        int i = left;
        int j = right;
        while(i < j) {
            // 1.j从右向左找小的
            while(i < j && a[j] > pv) {
                j--;
            }
            // 2.i从左向右找大的
            while(i < j && a[i] <= pv) {
                i++;
            }
            // 3.交换位置
            swap(a, i, j);
        }
        swap(a, left, i);
        return i;
    }

    public void swap(int[] array, int i, int j) {
        int t = array[i];
        array[i] = array[j];
        array[j] = t;
    }
    
    public static void main(String[] args) {
        
    }
}
```

#### 快速幂

```java
public class QuickPowLeetcode50 {
    
    static double myPow(double x, int n) {
        long p = n;
        if(p < 0) {
            p = -p;
        }
        double r = myPositive(x, p);
        return n < 0 ? 1 / r : r;
    }
    
    // 只能是正数
    static double myPowPositive(double x, int n) {
        if(n == 0) {
            return 1.0;
        }
        if(n == 1) {
            return x;
        }
        double y = myPowPositive(x, n / 2);
        if(n % 2 == 0) { // 偶数
            return y * y;
        } else { // 奇数
            return x * y * y;
        }
    }
    
}
```

#### 平方根整数部分

```java
public class SqrtLeetcode69 {
    
    static int mySqrt(int x) {
        int i = 1, j = x;
        int r = 0;
        while(i <= j) {
            int m = (i + j) >>> 1;
            if(m <= x / m) {
                i = m + 1;
                r = m;
            } else {
                j = m - 1;
            }
        }
        return r;
    }
}
```

### 回溯

+ 程序在运行过程中分成了多个阶段
+ 通过某些手段，将数据恢复到之前某一阶段，这就称之为回溯
+ 手段包括：方法栈、自定义栈

#### 全排列

```java
public class PermuteLeetcode46 {
    
    static List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        dfs(nums, new boolean[nums.length], new LinkedList<>(), result);
        return result;
    }
    
    static void dfs(int[] nums, boolean[] visited, LinkedList<Integer> stack, List<List<Integer>> result) {
        if(stack.size() == nums.length) {
            result.add(new ArrayList<>(stack));
            return;
        }
        // 遍历nums数组，发现没有被使用的数字，则将其标记为使用，并加入stack
        for(int i = 0; i < nums.length; i++) {
            if(!visited[i]) {
                stack.push(nums[i]);
                visited[i] = true;
                dfs(nums, visited, stack);
                visited[i] = false;
                stack.pop(); 
            }
        }
    }
    
    public static void main(String args[]) {
        int[] nums = {1, 2, 3};
        Arrays.sort(nums);
        List<List<Integer>> permute = permute(nums);
        for(List<Integer> list : permute) {
            System.out.println(list);
        }
    }
}
```

#### 全排列无重复

```java
public class PermuteLeetcode47 {
    
    static List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        dfs(nums, new boolean[nums.length], new LinkedList<>(), result);
        return result;
    }
    
    static void dfs(int[] nums, boolean[] visited, LinkedList<Integer> stack, List<List<Integer>> result) {
        if(stack.size() == nums.length) {
            result.add(new ArrayList<>(stack));
            return;
        }
        for(int i = 0; i < nums.length; i++) {
            if(i > 0 && nums[i] == nums[i - 1] && !visited[i - 1]) { // 找出重复数字
                continue;
            }
            if(!visited[i]) {
                stack.push(nums[i]);
                visited[i] = true;
                dfs(nums, visited, stack);
                visited[i] = false;
                stack.pop(); 
            }
        }
    }
    
    public static void main(String args[]) {
        int[] nums = {1, 1, 3};
        Arrays.sort(nums);
        List<List<Integer>> permute = permute(nums);
        for(List<Integer> list : permute) {
            System.out.println(list);
        }
    }
}
```

