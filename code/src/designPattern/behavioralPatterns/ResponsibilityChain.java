package designPattern.behavioralPatterns;

/**
 * 责任链模式：允许你将请求沿着处理者链进行传递，直到有一个处理者能够处理它为止
 * 角色：处理者（Handler）、具体处理者（Concrete Handler）、客户端（Client）
 */

class PurchaseRequest {
    private int number;
    private double amount;

    public PurchaseRequest(int number, double amount) {
        this.number = number;
        this.amount = amount;
    }

    public int getNumber() {
        return number;
    }

    public double getAmount() {
        return amount;
    }
}

abstract class Approver {
    protected Approver successor;

    public void setSuccessor(Approver successor) {
        this.successor = successor;
    }

    public abstract void processRequest(PurchaseRequest request);
}

class Manager extends Approver {
    @Override
    public void processRequest(PurchaseRequest request) {
        if (request.getAmount() <= 1000) {
            System.out.println("Manager approves purchase request #" + request.getNumber());
        } else if (successor != null) {
            successor.processRequest(request);
        }
    }
}

class Director extends Approver {
    @Override
    public void processRequest(PurchaseRequest request) {
        if (request.getAmount() <= 5000) {
            System.out.println("Director approves purchase request #" + request.getNumber());
        } else if (successor != null) {
            successor.processRequest(request);
        }
    }
}

class President extends Approver {
    @Override
    public void processRequest(PurchaseRequest request) {
        if (request.getAmount() <= 10000) {
            System.out.println("President approves purchase request #" + request.getNumber());
        } else {
            System.out.println("Purchase request #" + request.getNumber() + " requires a board meeting.");
        }
    }
}

class ResponsibilityChainClient {
    public static void main(String[] args) {
        Approver manager = new Manager();
        Approver director = new Director();
        Approver president = new President();

        manager.setSuccessor(director);
        director.setSuccessor(president);

        PurchaseRequest request = new PurchaseRequest(123, 5000);
        manager.processRequest(request);
    }
}

public class ResponsibilityChain {}
