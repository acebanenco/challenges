package com.acebanenco.challenge.algebra.eval;

import com.acebanenco.challenge.algebra.model.*;

public class CopyEvaluator extends AbstractEvaluator<Node> {

    @Override
    protected Node evalImpl(DerivativeNode node) {
        Node argument = eval(node.getArgument());
        if ( argument == node.getArgument() ) {
            return node;
        }
        return new DerivativeNode(node.getDerivative(), argument);
    }

    @Override
    protected Node evalImpl(SubtractNode node) {
        Node lhs = eval(node.getLhs());
        Node rhs = eval(node.getRhs());
        if ( lhs == node.getLhs() && rhs == node.getRhs() ) {
            return node;
        }
        return new SubtractNode(lhs, rhs);
    }

    @Override
    protected Node evalImpl(DivideNode node) {
        Node lhs = eval(node.getDividend());
        Node rhs = eval(node.getDivider());
        if ( lhs == node.getDividend() && rhs == node.getDivider() ) {
            return node;
        }
        return new DivideNode(lhs, rhs);
    }

    @Override
    protected Node evalImpl(ConstantNode node) {
        return node;
    }

    @Override
    protected Node evalImpl(ExponentNode node) {
        Node lhs = eval(node.getBase());
        Node rhs = eval(node.getExponent());
        if ( lhs == node.getBase() && rhs == node.getExponent() ) {
            return node;
        }
        return new ExponentNode(lhs, rhs);
    }

    @Override
    protected Node evalImpl(FunctionNode node) {
        Node argument = eval(node.getArgument());
        if ( argument == node.getArgument() ) {
            return node;
        }
        return new FunctionNode(
                node.getFunction(),
                node.isDerivative(),
                argument);
    }

    @Override
    protected Node evalImpl(IdNode node) {
        return node;
    }

    @Override
    protected Node evalImpl(MultiplyNode node) {
        Node lhs = eval(node.getLhs());
        Node rhs = eval(node.getRhs());
        if ( lhs == node.getLhs() && rhs == node.getRhs() ) {
            return node;
        }
        return new MultiplyNode(lhs, rhs);
    }

    @Override
    protected Node evalImpl(AddNode node) {
        Node lhs = eval(node.getLhs());
        Node rhs = eval(node.getRhs());
        if ( lhs == node.getLhs() && rhs == node.getRhs() ) {
            return node;
        }
        return new AddNode(lhs, rhs);
    }

    @Override
    protected Node evalImpl(NegateNode node) {
        Node argument = eval(node.getArgument());
        if ( argument == node.getArgument() ) {
            return node;
        }
        return new NegateNode(argument);
    }

    @Override
    protected Node evalImpl(CaptureFunctionNode node) {
        Node argument = eval(node.getArgument());
        if ( argument == node.getArgument() ) {
            return node;
        }
        return new CaptureFunctionNode(node.getPlaceholder(), node.isDerivative(), argument);
    }

    @Override
    protected Node evalImpl(CaptureNode node) {
        return node;
    }
}
