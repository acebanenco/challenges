package com.acebanenco.challenge.algebra.eval;

import com.acebanenco.challenge.algebra.model.ConstantNode;
import com.acebanenco.challenge.algebra.model.FunctionNode;
import com.acebanenco.challenge.algebra.model.Node;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.DoubleUnaryOperator;

import static java.lang.Double.parseDouble;

public class Arithmetics {

    public Node minus(ConstantNode argument) {
        double value = parseDouble(argument.getValue());
        return new ConstantNode(Double.toString(-value));
    }

    public Node add(ConstantNode lhs, ConstantNode rhs) {
        double a = parseDouble(lhs.getValue());
        double b = parseDouble(rhs.getValue());
        return new ConstantNode(Double.toString(a + b));
    }

    public Node multiply(ConstantNode lhs, ConstantNode rhs) {
        double a = parseDouble(lhs.getValue());
        double b = parseDouble(rhs.getValue());
        return new ConstantNode(Double.toString(a * b));
    }


    public Node power(ConstantNode base, ConstantNode exponent) {
        double a = parseDouble(base.getValue());
        double b = parseDouble(exponent.getValue());
        return new ConstantNode(Double.toString(Math.pow(a, b)));
    }


    public Node divide(ConstantNode dividend, ConstantNode divider) {
        double a = parseDouble(dividend.getValue());
        double b = parseDouble(divider.getValue());
        return new ConstantNode(Double.toString(a / b));
    }

    public Node subtract(ConstantNode lhs, ConstantNode rhs) {
        double a = parseDouble(lhs.getValue());
        double b = parseDouble(rhs.getValue());
        return new ConstantNode(Double.toString(a - b));
    }

    public Node eval(String function, ConstantNode argument) {
        double x = parseDouble(argument.getValue());
        DoubleUnaryOperator op = getDoubleUnaryOperator(function);
        if ( op == null ) {
            return new FunctionNode(function, false, argument);
        }
        return new ConstantNode(Double.toString(op.applyAsDouble(x)));
    }

    private static DoubleUnaryOperator getDoubleUnaryOperator(String function) {
        return switch (function) {
            case "sin" -> Math::sin;
            case "cos" -> Math::cos;
            case "tan" -> Math::tan;

            case "sinh" -> Math::sinh;// todo derivative
            case "cosh" -> Math::cosh;// todo derivative
            case "tanh" -> Math::tanh;// todo derivative

            case "acos" -> Math::acos;
            case "asin" -> Math::asin;
            case "atan" -> Math::atan;

            case "cbrt" -> Math::cbrt;// todo derivative
            case "exp" -> Math::exp;
            case "expm1" -> Math::expm1;// todo derivative
            case "log" -> Math::log;
            case "log1p" -> Math::log1p;// todo derivative
            case "log10" -> Math::log10;// todo derivative

            default -> null;
        };
    }
}
