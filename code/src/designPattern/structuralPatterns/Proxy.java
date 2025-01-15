package designPattern.structuralPatterns;

/**
 * 代理模式：它通过引入代理对象来控制对于原始对象的访问，从而实现在不改变原始对象的前提下，对其进行一些额外的操作
 * 角色：抽象主题（Subject）、真实主题（RealSubject）、代理主题（Proxy）
 */

interface Subject {
    void request();
}

class RealSubject implements Subject {
    @Override
    public void request() {
        System.out.println("RealSubject request");
    }
}

class Proxy implements Subject {
    private RealSubject realSubject;

    public Proxy(RealSubject realSubject) {
        this.realSubject = realSubject;
    }

    @Override
    public void request() {
        System.out.println("Proxy: Pre-processing request.");
        realSubject.request();
        System.out.println("Proxy: Post-processing request.");
    }
}

class ProxyClient {
    public static void main(String[] args) {
        RealSubject realSubject = new RealSubject();
        Proxy proxy = new Proxy(realSubject);
        proxy.request();
    }
}
