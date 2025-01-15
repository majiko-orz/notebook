package designPattern.creationalPatterns;

/**
 * 单例模式：确保类只有一个实例，并提供一个全局访问点
 */

public class Singleton {

    /**
     * 饿汉式（静态变量）
     * private static final Singleton instance = new Singleton();
     * private Singleton() {};
     * public static Singleton getInstance() {
     *     return instance;
     * }
     */

    /**
     * 饿汉式（静态代码块）
     * private static final Singleton instance;
     * private Singleton() {};
     * static {
     *     instance = new Singleton();
     * }
     * public static Singleton getInstance() {
     *     return instance;
     * }
     */

    // 懒汉式（双重检查锁）
    private static volatile Singleton instance;
    private Singleton() {};
    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }

    /**
     * 懒汉式（静态内部类）
     * private Singleton() {};
     * private static class SingletonInstance {
     *     private static final Singleton INSTANCE = new Singleton();
     * }
     * public static Singleton getInstance() {
     *     return SingletonInstance.INSTANCE;
     * }
     */

}
