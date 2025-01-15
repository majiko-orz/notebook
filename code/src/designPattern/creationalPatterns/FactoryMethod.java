package designPattern.creationalPatterns;

/**
 * 工厂方法
 */

interface MProduct {
    void display();
}

class ConcreteMProductA implements MProduct {
    @Override
    public void display() {
        System.out.println("create MProductA");
    }
}

class ConcreteMProductB implements MProduct {
    @Override
    public void display() {
        System.out.println("create MProductB");
    }
}

interface MFactory {
    MProduct createMProduct();
}

class ConcreteMFactoryA implements MFactory {
    @Override
    public MProduct createMProduct() {
        return new ConcreteMProductA();
    }
}

class ConcreteMFactoryB implements MFactory {
    @Override
    public MProduct createMProduct() {
        return new ConcreteMProductB();
    }
}

class FactoryMethodClient {
    public static void main(String[] args) {
        MFactory factoryA = new ConcreteMFactoryA();
        MProduct productA = factoryA.createMProduct();
        productA.display();

        MFactory factoryB = new ConcreteMFactoryB();
        MProduct productB = factoryB.createMProduct();
        productB.display();
    }
}

public class FactoryMethod {}
