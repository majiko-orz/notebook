package designPattern.behavioralPatterns;

/**
 * 策略模式：定义了一系列算法，并使得这些算法可以相互替换，使得算法的变化独立于使用算法的客户
 * 角色：上下文（Context）、策略（Strategy）、具体策略（Concrete Strategy）
 */

interface PaymentStrategy {
    void pay(int amount);
}

class AlipayStrategy implements PaymentStrategy {
    @Override
    public void pay(int amount) {
        System.out.println("使用支付宝支付：" + amount + "元");
    }
}

class WechatPayStrategy implements PaymentStrategy {
    @Override
    public void pay(int amount) {
        System.out.println("使用微信支付：" + amount + "元");
    }
}

class CardPaymentStrategy implements PaymentStrategy {
    @Override
    public void pay(int amount) {
        System.out.println("使用银行卡支付：" + amount + "元");
    }
}

class PaymentContext {
    private PaymentStrategy paymentStrategy;

    public PaymentContext(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public void pay(int amount) {
        paymentStrategy.pay(amount);
    }
}

class StrategyClient {
    public static void main(String[] args) {
        PaymentContext context1 = new PaymentContext(new AlipayStrategy());
        PaymentContext context2 = new PaymentContext(new WechatPayStrategy());
        PaymentContext context3 = new PaymentContext(new CardPaymentStrategy());

        context1.pay(100);
        context2.pay(200);
        context3.pay(300);
    }
}

public class Strategy {}
