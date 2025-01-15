package designPattern.behavioralPatterns;

import java.util.ArrayList;
import java.util.List;

/**
 * 备忘录模式：它允许对象在不暴露其内部状态的情况下捕获并保存其内部状态，并在以后将其恢复到此状态
 * 角色：发起人（Originator）、备忘录（Memento）、负责人（Caretaker）
 */

class Memento {
    private String state;

    public Memento(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}

class Originator {
    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Memento createMemento() {
        return new Memento(state);
    }

    public void restoreFromMemento(Memento memento) {
        this.state = memento.getState();
    }
}

class Caretaker {
    private List<Memento> history = new ArrayList<>();

    public void pushMemento(Memento memento) {
        history.add(memento);
    }

    public Memento popMemento() {
        int lastIndex = history.size() - 1;
        if (lastIndex >= 0) {
            return history.remove(lastIndex);
        }
        return null;
    }
}

class MementoClient {
    public static void main(String[] args) {
        Originator originator = new Originator();
        Caretaker caretaker = new Caretaker();

        originator.setState("first content");
        caretaker.pushMemento(originator.createMemento());

        originator.setState("second content");
        caretaker.pushMemento(originator.createMemento());

        // Output: second content
        originator.restoreFromMemento(caretaker.popMemento());
        System.out.println(originator.getState());

        // Output: first content
        originator.restoreFromMemento(caretaker.popMemento());
        System.out.println(originator.getState());
    }
}