package designPattern.structuralPatterns;

/**
 * 装饰者模式：允许动态地将责任附加到对象上。装饰器模式提供了一种灵活的方式，可以通过将对象包装在装饰器类的对象中来扩展其行为
 * 角色：组件（Component）、具体组件（Concrete Component）、装饰器（Decorator）、具体装饰器（Concrete Decorator）
 */

interface DComponent {
    void operation();
}

class ConcreteComponent implements DComponent {
    @Override
    public void operation() {
        System.out.println("ConcreteComponent operation");
    }
}

abstract class Decorator implements DComponent {
    protected DComponent component;

    public Decorator(DComponent component) {
        this.component = component;
    }

    @Override
    public void operation() {
        component.operation();
    }
}

class ConcreteDecorator extends Decorator {
    public ConcreteDecorator(DComponent component) {
        super(component);
    }

    @Override
    public void operation() {
        System.out.println("ConcreteDecorator operation");
        super.operation();
    }
}

class Client {
    public static void main(String[] args) {
        DComponent component = new ConcreteComponent();
        DComponent decorator = new ConcreteDecorator(component);
        decorator.operation();
    }
}