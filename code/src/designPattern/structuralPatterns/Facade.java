package designPattern.structuralPatterns;

/**
 * 外观模式（门面模式）：提供了一个统一的接口，用于访问一组接口中的一组相互关联的接口
 * 角色：门面（Facade）、子系统（Subsystem）、客户端（Client）
 */

class SubsystemA {
    public void operationA() {
        System.out.println("SubsystemA operationA");
    }
}

class SubsystemB {
    public void operationB() {
        System.out.println("SubsystemB operationB");
    }
}

class Facade {
    private SubsystemA subsystemA;
    private SubsystemB subsystemB;

    public Facade() {
        subsystemA = new SubsystemA();
        subsystemB = new SubsystemB();
    }

    public void operation() {
        subsystemA.operationA();
        subsystemB.operationB();
    }
}

class FacadeClient {
    public static void main(String[] args) {
        Facade facade = new Facade();
        facade.operation();
    }
}