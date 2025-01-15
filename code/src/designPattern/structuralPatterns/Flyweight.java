package designPattern.structuralPatterns;


import java.util.HashMap;
import java.util.Map;

/**
 * 享元模式（蝇量模式）：主要目的是通过共享尽可能多的相似对象来最小化内存使用或计算开销。该模式的核心思想是将对象的状态分为内部状态和外部状态，其中内部状态是可以共享的，而外部状态则取决于具体的场景
 * 角色：享元接口（Flyweight）、具体享元对象（Concrete Flyweight）、非共享具体享元对象（UnsharedConcrete Flyweight）、享元工厂（FlyweightFactory）、客户端（Client）
 */

interface Flyweight {
    void operation(String extrinsicState);
}

class ConcreteFlyweight implements Flyweight {
    private String intrinsicState;

    public ConcreteFlyweight(String intrinsicState) {
        this.intrinsicState = intrinsicState;
    }

    @Override
    public void operation(String extrinsicState) {
        System.out.println("IntrinsicState: " + intrinsicState + ", Extrinsic State: " + extrinsicState);
    }
}

class FlyweightFactory {
    private Map<String, Flyweight> flyweights = new HashMap<>();

    public Flyweight getFlyweight(String key) {
        Flyweight flyweight = flyweights.get(key);
        if (flyweight == null) {
            flyweight = new ConcreteFlyweight(key);
            flyweights.put(key, flyweight);
        }
        return flyweight;
    }
}

class FlyweightClient {
    public static void main(String[] args) {
        FlyweightFactory factory = new FlyweightFactory();
        Flyweight flyweightA = factory.getFlyweight("A");
        Flyweight flyweightB = factory.getFlyweight("B");
        flyweightA.operation("1");
        flyweightB.operation("2");
    }
}