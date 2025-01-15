package designPattern.structuralPatterns;

import java.util.ArrayList;
import java.util.List;

/**
 * 组合模式：将对象组合成树形结构以表示部分-整体层次结构，使得客户端对单个对象和组合对象的使用具有一致性
 * 角色：组件（Component）、叶子（Leaf）、容器（Composite）
 */

interface Component {
    void operation();
}

class Leaf implements Component {
    @Override
    public void operation() {
        System.out.println("Leaf operation");
    }
}

class Composite implements Component {
    private List<Component> children = new ArrayList<>();

    public void add(Component component) {
        children.add(component);
    }

    public void remove(Component component) {
        children.remove(component);
    }

    @Override
    public void operation() {
        System.out.println("Composite operation");
        for (Component child : children) {
            child.operation();
        }
    }
}

class CompositeClient {
    public static void main(String[] args) {
        Composite root = new Composite();
        Leaf leaf1 = new Leaf();
        Leaf leaf2 = new Leaf();
        root.add(leaf1);
        root.add(leaf2);

        root.operation();
    }
}
