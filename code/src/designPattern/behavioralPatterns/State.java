package designPattern.behavioralPatterns;

/**
 * 状态模式：允许对象在其内部状态改变时改变它的行为
 * 角色：环境（Context）、抽象状态（State）、具体状态（ConcreteState）
 */

class Context {
    private State state;

    public Context(State state) {
        this.state = state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void request() {
        state.handler();
    }
}

interface State {
    void handler();
}

class ConcreteStateA implements State {
    @Override
    public void handler() {
        System.out.println("StateA handler");
    }
}

class ConcreteStateB implements State {
    @Override
    public void handler() {
        System.out.println("StateB handler");
    }
}

class StateClient {
    public static void main(String[] args) {
        State stateA = new ConcreteStateA();
        State stateB = new ConcreteStateB();

        Context context = new Context(stateA);
        context.request();

        context.setState(stateB);
        context.request();
    }
}