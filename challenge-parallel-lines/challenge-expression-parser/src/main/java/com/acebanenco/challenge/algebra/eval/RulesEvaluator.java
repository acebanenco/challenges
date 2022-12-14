package com.acebanenco.challenge.algebra.eval;

import com.acebanenco.challenge.algebra.ObjectFactory;
import com.acebanenco.challenge.algebra.model.*;
import com.acebanenco.challenge.algebra.rule.RuleMatcher;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class RulesEvaluator extends CopyEvaluator {

    private final Map<NodeType, List<RuleMatcher>> rules;
    private final Arithmetics arithmetics;

    public static Node evaluate(Node node) {
        RulesEvaluator evaluator = ObjectFactory.getInstance().getDerivativeEvaluator();
        return evaluator.eval(node);
    }

    @Override
    public Node eval(Node node) {
        do {
            Node result = doReplace(node);
            if ( result == null ) {
                Node eval = super.eval(node);
                if ( eval == node ) {
                    return eval;
                }
                node = eval;
            } else {
                node = result;
            }
        } while (true);
    }

    private Node doReplace(Node node) {
        List<RuleMatcher> matchers = rules.get(node.getNodeType());
        if ( matchers == null ) {
            return null;
        }
        for (RuleMatcher matcher : matchers) {
            Node eval = matcher.eval(node);
            if (eval != null) {
                return eval;
            }
        }
        return null;
    }

    @Override
    protected Node evalImpl(DerivativeNode node) {
        Node argument = node.getArgument();
        // TODO Candidate for rules
        if ( argument instanceof ConstantNode ) {
            return ConstantNode.ZERO;
        }
        if ( !(argument instanceof IdNode idNode) ) {
            return super.evalImpl(node);
        }
        // TODO Candidate for rules
        Derivative derivative = node.getDerivative();
        if ( derivative.getArgument().equals(idNode.getVariable()) ) {
            return ConstantNode.ONE;
        }
        return ConstantNode.ZERO;
    }


    @Override
    protected Node evalImpl(SubtractNode node) {
        if (node.getLhs().getNodeType() != NodeType.constant) {
            return super.evalImpl(node);
        }
        if (node.getRhs().getNodeType() != NodeType.constant) {
            return super.evalImpl(node);
        }
        return arithmetics.subtract(
                (ConstantNode)node.getLhs(),
                (ConstantNode)node.getRhs()
        );
    }

    @Override
    protected Node evalImpl(DivideNode node) {
        if (node.getDividend().getNodeType() != NodeType.constant) {
            return super.evalImpl(node);
        }
        if (node.getDivider().getNodeType() != NodeType.constant) {
            return super.evalImpl(node);
        }
        return arithmetics.divide(
                (ConstantNode)node.getDividend(),
                (ConstantNode)node.getDivider()
        );
    }

    @Override
    protected Node evalImpl(ExponentNode node) {
        if (node.getBase().getNodeType() != NodeType.constant) {
            return super.evalImpl(node);
        }
        if (node.getExponent().getNodeType() != NodeType.constant) {
            return super.evalImpl(node);
        }
        return arithmetics.power(
                (ConstantNode)node.getBase(),
                (ConstantNode)node.getExponent()
        );
    }

    @Override
    protected Node evalImpl(FunctionNode node) {
        if (node.getArgument().getNodeType() != NodeType.constant) {
            return super.evalImpl(node);
        }
        if ( node.isDerivative() ) {
            return super.evalImpl(node);
        }
        return arithmetics.eval(
                node.getFunction(),
                (ConstantNode)node.getArgument()
        );
    }

    @Override
    protected Node evalImpl(MultiplyNode node) {
        if (node.getLhs().getNodeType() != NodeType.constant) {
            return super.evalImpl(node);
        }
        if (node.getRhs().getNodeType() != NodeType.constant) {
            return super.evalImpl(node);
        }
        return arithmetics.multiply(
                (ConstantNode)node.getLhs(),
                (ConstantNode)node.getRhs()
        );
    }

    @Override
    protected Node evalImpl(AddNode node) {
        if (node.getLhs().getNodeType() != NodeType.constant) {
            return super.evalImpl(node);
        }
        if (node.getRhs().getNodeType() != NodeType.constant) {
            return super.evalImpl(node);
        }
        return arithmetics.add(
                (ConstantNode)node.getLhs(),
                (ConstantNode)node.getRhs()
        );
    }

    @Override
    protected Node evalImpl(NegateNode node) {
        if (node.getArgument().getNodeType() != NodeType.constant) {
            return super.evalImpl(node);
        }
        return arithmetics.minus(
                (ConstantNode)node.getArgument()
        );
    }
}
