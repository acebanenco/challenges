package com.acebanenco.challenge.algebra.model;

public class NodeFactoryImpl implements NodeFactory {
    @Override
    public Node functionNode(String func, boolean derivative, Node argument) {
        if ( func.startsWith("$") ) {
            return new CaptureFunctionNode(func, derivative, argument);
        }
        return new FunctionNode(func, derivative, argument);
    }

    @Override
    public Node derivativeNode(String derivative, Node argument) {
        Node result = argument;
        for (Derivative d : Derivative.parse(derivative)) {
            result = new DerivativeNode(d, result);
        }
        return result;
    }

    @Override
    public Node idNode(String variable) {
        if ( variable.startsWith("$") ) {
            return new CaptureNode(variable);
        }
        return new IdNode(variable);
    }

    @Override
    public Node constantNode(String value) {
        return new ConstantNode(value);
    }

    @Override
    public Node exponentNode(Node base, Node exponent) {
        return new ExponentNode(base, exponent);
    }

    @Override
    public Node negateNode(Node argument) {
        return new NegateNode(argument);
    }

    @Override
    public Node multiplyNode(Node lhs, Node rhs) {
        return new MultiplyNode(lhs, rhs);
    }

    @Override
    public Node divideNode(Node dividend, Node divider) {
        return new DivideNode(dividend, divider);
    }

    @Override
    public Node addNode(Node lhs, Node rhs) {
        return new AddNode(lhs, rhs);
    }

    @Override
    public Node subtractNode(Node lhs, Node rhs) {
        return new SubtractNode(lhs, rhs);
    }
}
