package designPattern.structuralPatterns;

/**
 * 桥接模式：将抽象部分与实现部分分离，使它们可以独立变化。这种模式涉及创建一个包含抽象类和实现类的接口，一个作为桥接的接口
 * 角色：抽象类（Abstraction）、扩充抽象类（RefinedAbstraction）、实现接口（Implementor）、具体实现类（ConcreteImplementor）
 * 适用场景：抽象和实现之间有多个维度的变化、一个抽象类存在多个实现类、不希望使用继承
 */

interface Implementor {
    void operation();
}

class ConcreteImplementorA implements Implementor {
    @Override
    public void operation() {
        System.out.println("ConcreteImplementorA Implementor");
    }
}

class ConcreteImplementorB implements Implementor {
    @Override
    public void operation() {
        System.out.println("ConcreteImplementorB Implementor");
    }
}

abstract class Abstraction {
    protected Implementor implementor;

    public Abstraction(Implementor implementor) {
        this.implementor = implementor;
    }

    public abstract void operation();
}

class RefinedAbstraction extends Abstraction {
    public RefinedAbstraction(Implementor implementor) {
        super(implementor);
    }

    @Override
    public void operation() {
        System.out.println("RefinedAbstraction operation");
        implementor.operation();
    }
}

class BridgeClient {
    public static void main(String[] args) {
        Implementor implementorA = new ConcreteImplementorA();
        Abstraction abstractionA = new RefinedAbstraction(implementorA);
        abstractionA.operation();

        Implementor implementorB = new ConcreteImplementorB();
        Abstraction abstractionB = new RefinedAbstraction(implementorB);
        abstractionB.operation();
    }
}
