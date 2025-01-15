package designPattern.structuralPatterns;

/**
 * 适配器模式：主要目的是使不兼容的接口能够一起工作。适配器模式允许一个类的接口被另一个类所使用，起到连接两个不同接口的作用
 * 角色：目标接口（Target）、适配器（Adapter）、被适配者（Adaptee）
 */

interface Target {
    void request();
}

class Adaptee {
    public void specificRequest() {
        System.out.println("Specific Request");
    }
}

class Adapter implements Target {

    private Adaptee adaptee;

    public Adapter(Adaptee adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void request() {
        adaptee.specificRequest();
    }
}

class AdapterClient {
    public static void main(String[] args) {
        Adaptee adaptee = new Adaptee();
        Target target = new Adapter(adaptee);
        target.request();
    }
}
