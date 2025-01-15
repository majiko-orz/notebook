package designPattern.creationalPatterns;

/**
 * 简单工厂
 */

interface Product {
    void display();
}

class ConcreteProductA implements Product {
    @Override
    public void display() {
        System.out.println("create ProductA");
    }
}

class ConcreteProductB implements Product {
    @Override
    public void display() {
        System.out.println("create ProductB");
    }
}

class SimpleFactory {
    public Product createProduct(String type) {
        switch (type) {
            case "A":
                return new ConcreteProductA();
            case "B":
                return new ConcreteProductB();
            default:
                return null;
        }
    }
}

class SimpleFactoryClient {
    public static void main(String[] args) {
        SimpleFactory factory = new SimpleFactory();
        Product productA = factory.createProduct("A");
        productA.display();
        Product productB = factory.createProduct("B");
        productB.display();
    }
}