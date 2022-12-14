package com.acebanenco.challenge.algebra.eval;

import com.acebanenco.challenge.algebra.model.*;

public abstract class AbstractEvaluator<T> implements Evaluator<T> {

    @Override
    public T eval(Node node) {
        return switch (node.getNodeType()) {
            case constant -> evalImpl((ConstantNode) node);
            case exponent -> evalImpl((ExponentNode) node);
            case function -> evalImpl((FunctionNode) node);
            case id -> evalImpl((IdNode) node);
            case multiply -> evalImpl((MultiplyNode) node);
            case divide -> evalImpl((DivideNode) node);
            case add -> evalImpl((AddNode) node);
            case subtract -> evalImpl((SubtractNode) node);
            case negate -> evalImpl((NegateNode) node);
            case derivative -> evalImpl((DerivativeNode) node);
            case capture -> evalImpl((CaptureNode)node);
            case captureFunction -> evalImpl((CaptureFunctionNode)node);
        };
    }

    protected abstract T evalImpl(CaptureFunctionNode node);

    protected abstract T evalImpl(CaptureNode node);

    protected abstract T evalImpl(DerivativeNode node);

    protected abstract T evalImpl(SubtractNode node);

    protected abstract T evalImpl(DivideNode node);

    protected abstract T evalImpl(ConstantNode node);

    protected abstract T evalImpl(ExponentNode node);

    protected abstract T evalImpl(FunctionNode node);

    protected abstract T evalImpl(IdNode node);

    protected abstract T evalImpl(MultiplyNode node);

    protected abstract T evalImpl(AddNode node);

    protected abstract T evalImpl(NegateNode node);
}
