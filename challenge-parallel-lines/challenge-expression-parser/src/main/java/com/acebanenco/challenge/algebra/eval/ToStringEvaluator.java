package com.acebanenco.challenge.algebra.eval;

import com.acebanenco.challenge.algebra.model.*;

public class ToStringEvaluator extends AbstractEvaluator<String> {

    public static final ToStringEvaluator shared = new ToStringEvaluator();

    @Override
    protected String evalImpl(SubtractNode node) {
        return eval(node, node.getLhs(), node.getRhs(), " - ");
    }

    @Override
    protected String evalImpl(DivideNode node) {
        return eval(node, node.getDividend(), node.getDivider(), " / ");
    }

    @Override
    protected String evalImpl(ConstantNode node) {
        return node.getValue();
    }

    @Override
    protected String evalImpl(ExponentNode node) {
        return eval(node, node.getBase(), node.getExponent(), "^");
    }

    @Override
    protected String evalImpl(FunctionNode node) {
        String derivative = node.isDerivative() ? "'" :  "";
        return node.getFunction() + derivative + "("+eval(node.getArgument())+")";
    }

    @Override
    protected String evalImpl(IdNode node) {
        return node.getVariable();
    }

    @Override
    protected String evalImpl(MultiplyNode node) {
        return eval(node, node.getLhs(), node.getRhs(), " * ");
    }

    @Override
    protected String evalImpl(AddNode node) {
        return eval(node, node.getLhs(), node.getRhs(), " + ");
    }

    @Override
    protected String evalImpl(NegateNode node) {
        Node argument = node.getArgument();
        if ( argument instanceof ComplexNode ) {
            return "-(" + eval(argument) + ")";
        }
        return "-" + eval(argument);
    }

    @Override
    protected String evalImpl(DerivativeNode node) {
        if ( node.getArgument() instanceof IdNode idNode ) {
            return "" + idNode + node.getDerivative();
        }
        return "("+eval(node.getArgument())+")" + node.getDerivative();
    }

    private String eval(ComplexNode node, Node lhs, Node rhs, String operator) {
        StringBuilder result = new StringBuilder();
        if ( lhs instanceof ComplexNode cn && cn.getOrder() > node.getOrder() ) {
            result.append("(").append(eval(lhs)).append(")");
        } else {
            result.append(eval(lhs));
        }
        result.append(operator);
        if ( rhs instanceof ComplexNode cn && cn.getOrder() >= node.getOrder() ) {
            result.append("(").append(eval(rhs)).append(")");
        } else {
            result.append(eval(rhs));
        }
        return result.toString();
    }

    @Override
    protected String evalImpl(CaptureFunctionNode node) {
        String derivative = node.isDerivative() ? "'" :  "";
        return node.getPlaceholder() + derivative + "("+eval(node.getArgument())+")";
    }

    @Override
    protected String evalImpl(CaptureNode node) {
        return node.getPlaceholder();
    }
}
