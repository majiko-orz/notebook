package designPattern.creationalPatterns;

/**
 * 建造者模式：它的主要目的是将一个复杂对象的构建过程与其表示分离，使得同样的构建过程可以创建不同的表示
 * 角色：产品类（Product）、抽象建造者（Builder）、具体建造者（Concrete Builder）、指挥者（Director）、客户端（Client）
 */

class Computer {
    private String CPU;
    private String memory;
    private String storage;

    public String getCPU() {
        return CPU;
    }

    public void setCPU(String CPU) {
        this.CPU = CPU;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    @Override
    public String toString() {
        return "Computor{" +
                "CPU='" + CPU + '\'' +
                ", memory='" + memory + '\'' +
                ", storage='" + storage + '\'' +
                '}';
    }
}

interface ComputerBuilder {
    void buildCPU(String cpu);
    void buildMemory(String memory);
    void buildStorage(String storage);
    Computer getResult();
}

class ConcreteComputerBuilder implements ComputerBuilder {
    private Computer computer = new Computer();

    @Override
    public void buildCPU(String cpu) {
        computer.setCPU(cpu);
    }

    @Override
    public void buildMemory(String memory) {
        computer.setMemory(memory);
    }

    @Override
    public void buildStorage(String storage) {
        computer.setStorage(storage);
    }

    @Override
    public Computer getResult() {
        return computer;
    }
}

class Director {
    private ComputerBuilder builder;

    public Director(ComputerBuilder builder) {
        this.builder = builder;
    }

    public void construct() {
        builder.buildCPU("i7");
        builder.buildMemory("32G");
        builder.buildStorage("1T");
    }
}

class BuilderClient {
    public static void main(String[] args) {
        ComputerBuilder builder = new ConcreteComputerBuilder();
        Director director = new Director(builder);
        director.construct();
        Computer computer = builder.getResult();
        System.out.println(computer);
    }
}

public class Builder {}
