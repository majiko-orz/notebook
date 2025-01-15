package designPattern.behavioralPatterns;

/**
 * 模版方法模式：定义一个算法的骨架，而将一些步骤延迟到子类中。它使得子类可以在不改变算法结构的情况下重新定义算法中的某些步骤
 * 角色：模版类（AbstractClass）、具体模板类（ConcreteClass）
 */

abstract class AbstractClass {
    public final void templateMethod() {
        primitiveOperation1();
        primitiveOperation2();
        concreteOperation();
        hook();
    }

    protected abstract void primitiveOperation1();

    protected abstract void primitiveOperation2();

    private void concreteOperation() {
        System.out.println("Concrete Operation");
    }

    protected void hook() {

    };
}

class ConcreteClass1 extends AbstractClass {
    @Override
    protected void primitiveOperation1() {
        System.out.println("ConcreteClass1 primitiveOperation1");
    }

    @Override
    protected void primitiveOperation2() {
        System.out.println("ConcreteClass1 primitiveOperation2");
    }

    @Override
    protected void hook() {
        System.out.println("ConcreteClass1 hook");
    }
}

class ConcreteClass2 extends AbstractClass {
    @Override
    protected void primitiveOperation1() {
        System.out.println("ConcreteClass2 primitiveOperation1");
    }

    @Override
    protected void primitiveOperation2() {
        System.out.println("ConcreteClass2 primitiveOperation2");
    }
}

class TemplateMethodClient {
    public static void main(String[] args) {
        AbstractClass class1 = new ConcreteClass1();
        AbstractClass class2 = new ConcreteClass2();

        class1.templateMethod();
        class2.templateMethod();
    }
}

public class TemplateMethod {}
