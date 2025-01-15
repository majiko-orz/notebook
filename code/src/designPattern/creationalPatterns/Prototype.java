package designPattern.creationalPatterns;

/**
 * 原型模式
 */

class Prototype implements Cloneable {

    @Override
    public Prototype clone() {
        try {
            return (Prototype) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}

class PrototypeClient {
    public static void main(String[] args) {
        Prototype prototype = new Prototype();
        Prototype clone1 = prototype.clone();
        Prototype clone2 = prototype.clone();
    }
}
