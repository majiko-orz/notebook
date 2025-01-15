package designPattern.creationalPatterns;

/**
 * 抽象工厂
 */

interface AProduct {
    void display();
}

interface AProductA extends AProduct {

}

interface AProductB extends AProduct {

}

class ConcreteAProductA implements AProductA {
    @Override
    public void display() {
        System.out.println("create AProductA");
    }
}

class ConcreteAProductB implements AProductB {
    @Override
    public void display() {
        System.out.println("create AProductB");
    }
}

interface AFactory {
    AProductA createAProductA();
    AProductB createAProductB();
}

class ConcreteAFactory implements AFactory {
    @Override
    public AProductA createAProductA() {
        return new ConcreteAProductA();
    }

    @Override
    public AProductB createAProductB() {
        return new ConcreteAProductB();
    }
}

class AbstractFactoryClient {
    public static void main(String[] args) {
        ConcreteAFactory factory = new ConcreteAFactory();
        AProductA productA = factory.createAProductA();
        productA.display();

        AProductB productB = factory.createAProductB();
        productB.display();
    }
}

public class AbstractFactory {}
