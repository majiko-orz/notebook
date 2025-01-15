package designPattern.behavioralPatterns;

import java.util.ArrayList;
import java.util.List;

/**
 * 迭代器模式：它提供一种方法按顺序访问一个聚合对象中的各个元素，而又不暴露其内部的表示
 * 角色：迭代器接口（Iterator）、具体迭代器（Concrete Iterator）、可迭代对象（Aggregate）、具体可迭代对象（Concrete Aggregate）、客户端（Client）
 */

interface Iterator<T> {
    boolean hasNext();
    T next();
}

class ClassIterator implements Iterator<FitnessClass>{
    private List<FitnessClass> classes;
    private int position = 0;

    public ClassIterator(List<FitnessClass> classes) {
        this.classes = classes;
    }

    @Override
    public boolean hasNext() {
        return position < classes.size();
    }

    @Override
    public FitnessClass next() {
        if (hasNext()) {
            FitnessClass fitnessClass = classes.get(position);
            position++;
            return fitnessClass;
        } else {
            return null;
        }
    }
}

interface ClassAggregate {
    Iterator<FitnessClass> createIterator();
}

class FitnessClassList implements ClassAggregate {
    private List<FitnessClass> classes;

    public FitnessClassList() {
        this.classes = new ArrayList<>();
    }

    public void addClass(FitnessClass fitnessClass) {
        classes.add(fitnessClass);
    }

    @Override
    public Iterator<FitnessClass> createIterator() {
        return new ClassIterator(classes);
    }
}

class FitnessClass {
    private String name;

    public FitnessClass(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class IteratorClient {
    public static void main(String[] args) {
        FitnessClassList fitnessClassList = new FitnessClassList();
        fitnessClassList.addClass(new FitnessClass("Yoga"));
        fitnessClassList.addClass(new FitnessClass("Zumba"));
        fitnessClassList.addClass(new FitnessClass("Pilates"));

        Iterator<FitnessClass> iterator = fitnessClassList.createIterator();
        while (iterator.hasNext()) {
            FitnessClass fitnessClass = iterator.next();
            System.out.println("Fitness Class: " + fitnessClass.getName());
        }
    }
}