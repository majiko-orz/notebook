package designPattern.behavioralPatterns;

import java.util.ArrayList;
import java.util.List;

/**
 * 访问者模式：主要用于对一组对象的元素进行操作，而不暴露这些对象的内部结构。通过访问者模式，可以在不修改现有代码的情况下向现有类层次结构中添加新的操作
 * 角色：抽象访问者（Visitor）、具体访问者（Concrete Visitor）、抽象元素（Element）、具体元素（Concrete Element）、对象结构（Object Structure）
 */

interface Visitor {
    void visit(ConcreteElementA elementA);
    void visit(ConcreteElementB elementB);
}

class ConcreteVisitorA implements Visitor {
    @Override
    public void visit(ConcreteElementA elementA) {
        System.out.println("ConcreteVisitorA visit elementA");
    }

    @Override
    public void visit(ConcreteElementB elementB) {
        System.out.println("ConcreteVisitorA visit elementB");
    }
}

class ConcreteVisitorB implements Visitor {
    @Override
    public void visit(ConcreteElementA elementA) {
        System.out.println("ConcreteVisitorB visit elementA");
    }

    @Override
    public void visit(ConcreteElementB elementB) {
        System.out.println("ConcreteVisitorB visit elementB");
    }
}

interface Element {
    void accept(Visitor visitor);
}

class ConcreteElementA implements Element {
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

class ConcreteElementB implements Element {
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

class ObjectStructure {
    private List<Element> elements = new ArrayList<>();

    public void addElement(Element element) {
        elements.add(element);
    }

    public void accept(Visitor visitor) {
        for (Element element : elements) {
            element.accept(visitor);
        }
    }
}

class VisitorClient {
    public static void main(String[] args) {
        ObjectStructure structure = new ObjectStructure();
        structure.addElement(new ConcreteElementA());
        structure.addElement(new ConcreteElementB());

        Visitor visitorA = new ConcreteVisitorA();
        structure.accept(visitorA);

        Visitor visitorB = new ConcreteVisitorB();
        structure.accept(visitorB);
    }
}