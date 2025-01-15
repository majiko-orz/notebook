package designPattern.behavioralPatterns;

/**
 * 解释器模式：定义了一种语言的文法，并且建立一个解释器来解释该语言中的句子。它属于一种解释型模式，主要用于自定义的脚本语言解析等场景
 * 角色：抽象表达式（AbstractExpression）、终结符表达式（TerminalExpression）、非终结符表达式（NonterminalExpression）、上下文（Context）、客户端（Client）
 */

interface Expression {
    int interpret();
}

class NumberExpression implements Expression {
    private int number;

    public NumberExpression(int number) {
        this.number = number;
    }

    @Override
    public int interpret() {
        return number;
    }
}

class AddExpression implements Expression {
    private Expression left;
    private Expression right;

    public AddExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public int interpret() {
        return left.interpret() + right.interpret();
    }
}

class SubtractExpression implements Expression {
    private Expression left;
    private Expression right;

    public SubtractExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public int interpret() {
        return left.interpret() - right.interpret();
    }
}

class InterpreterClient {
    public static void main(String[] args) {
        Expression expression = new SubtractExpression(new AddExpression(new NumberExpression(1), new NumberExpression(2)), new NumberExpression(3));
        System.out.println("result: " + expression.interpret());
    }
}

public class Interpreter {}
