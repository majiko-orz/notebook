package other;

import java.util.concurrent.atomic.AtomicReference;

// https://blog.csdn.net/fengyuyeguirenenen/article/details/123856507
public class CLHLock {

    private static class CLHNode {
        private volatile boolean locked = false;
    }

    private final AtomicReference<CLHNode> tailNode = new AtomicReference<>(new CLHNode());
    private final ThreadLocal<CLHNode> predNode = new ThreadLocal<>();
    private final ThreadLocal<CLHNode> currNode = ThreadLocal.withInitial(CLHNode::new);

    public void lock() {
        CLHNode curNode = currNode.get();
        curNode.locked = true;
        CLHNode preNode = tailNode.getAndSet(curNode);
        predNode.set(preNode);
        while (preNode.locked) {
            System.out.println("线程" + Thread.currentThread().getName() + "没能获取到锁，进行自旋等待。。。");
        }
        System.out.println("线程" + Thread.currentThread().getName() + "获取到了锁！！！");
    }

    public void unlock() {
        CLHNode curNode = currNode.get();
        curNode.locked = false;
        System.out.println("线程" + Thread.currentThread().getName() + "释放了锁！！！");
        currNode.set(predNode.get());
    }
}
