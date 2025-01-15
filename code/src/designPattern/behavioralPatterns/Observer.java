package designPattern.behavioralPatterns;

import java.util.ArrayList;
import java.util.List;

/**
 * 观察者模式：定义一种一对多的依赖关系，一个主题对象可被多个观察者对象同时监听，使得每当主题对象状态变化时，所有依赖于它的对象都会得到通知并被自动更新
 * 角色：主题（Subject）、具体主题（Concrete Subject）、观察者（Observer）、具体观察者（Concrete Observer）
 */

interface Subject {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers(String message);
}

class ConcreteSubject implements Subject {
    private List<Observer> observers = new ArrayList<>();
    private String state;

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    public void setState(String state) {
        this.state = state;
        notifyObservers("State changed to : " + state);
    }
}

interface Observer {
    void update(String message);
}

class EmailNotification implements Observer {
    private String email;

    public EmailNotification(String email) {
        this.email = email;
    }

    @Override
    public void update(String message) {
        System.out.println("send email to " + email + ": " + message);
    }
}

class ObserverClient {
    public static void main(String[] args) {
        ConcreteSubject subject = new ConcreteSubject();
        Observer emailObserver1 = new EmailNotification("user1@example.com");
        Observer emailObserver2 = new EmailNotification("user2@example.com");

        subject.addObserver(emailObserver1);
        subject.addObserver(emailObserver2);

        subject.setState("new state 1");
        subject.setState("new state 2");
    }
}