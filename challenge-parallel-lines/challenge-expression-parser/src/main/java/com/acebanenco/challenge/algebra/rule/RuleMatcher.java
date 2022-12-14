package com.acebanenco.challenge.algebra.rule;

import com.acebanenco.challenge.algebra.eval.Evaluator;
import com.acebanenco.challenge.algebra.eval.RuleReplacementEvaluator;
import com.acebanenco.challenge.algebra.model.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RuleMatcher implements Evaluator<Node> {

    private final Rule rule;

    @Override
    public Node eval(Node argument) {
        Node pattern = rule.getPattern();

        RuleReplacements replacements = getReplacements(argument, pattern);
        if (replacements == null) {
            return null;
        }

        RuleReplacementEvaluator evaluator = new RuleReplacementEvaluator(replacements);
        Node template = rule.getTemplate();
        return evaluator.eval(template);
    }

    private RuleReplacements getReplacements(Node argument, Node pattern) {
        RuleMatcherContext context = new RuleMatcherContext();
        context.ruleStack.add(pattern);
        context.argumentStack.add(argument);

        while ( !context.ruleStack.isEmpty() ) {
            Node ruleNode = context.ruleStack.removeFirst();
            Node argumentNode = context.argumentStack.removeFirst();

            if (!matchesNode(context, ruleNode, argumentNode)) {
                return null;
            }
        }
        return new RuleReplacements(context.replacements,
                context.functionAliases, context.derivativeAliases);
    }

    private boolean matchesNode(RuleMatcherContext context, Node ruleNode, Node argumentNode) {
        NodeType ruleNodeType = ruleNode.getNodeType();
        if (!matchNodeType(ruleNodeType, argumentNode.getNodeType())) {
            return false;
        }
        switch (ruleNodeType) {
            case capture -> {
                if (!captureNode(context, (CaptureNode) ruleNode, argumentNode)) {
                    return false;
                }
            }
            case captureFunction -> {
                if (!captureFunctionNode(context, (CaptureFunctionNode) ruleNode, (FunctionNode) argumentNode)) {
                    return false;
                }
            }

            case constant -> {
                if ( !matchConstants((ConstantNode) argumentNode, (ConstantNode) ruleNode)) {
                    return false;
                }
            }
            case id -> {
                if ( !matchIdNode((IdNode) ruleNode, (IdNode) argumentNode)) {
                    return false;
                }
            }
            case derivative -> {
                if (!matchDerivativeNode(context, (DerivativeNode) ruleNode, (DerivativeNode) argumentNode)) {
                    return false;
                }
            }
            case function -> {
                if (!matchFunctionNode(context, (FunctionNode) ruleNode, (FunctionNode) argumentNode)) {
                    return false;
                }
            }

            case divide -> processDivideNode(context, (DivideNode) ruleNode, (DivideNode) argumentNode);
            case multiply -> processMultiplyNode(context, (MultiplyNode) ruleNode, (MultiplyNode) argumentNode);
            case negate -> processNegateNode(context, (NegateNode) ruleNode, (NegateNode) argumentNode);
            case subtract -> processSubtractNode(context, (SubtractNode) ruleNode, (SubtractNode) argumentNode);
            case add -> processAddNode(context, (AddNode) ruleNode, (AddNode) argumentNode);
            case exponent -> processExponentNode(context, (ExponentNode) ruleNode, (ExponentNode) argumentNode);

            default -> throw new IllegalStateException("Branch not covered: " + ruleNodeType);
        }
        return true;
    }

    private boolean matchFunctionNode(RuleMatcherContext context, FunctionNode ruleNode, FunctionNode argumentNode) {
        if ( !ruleNode.getFunction().equals(argumentNode.getFunction()) ) {
            return false;
        }
        if (ruleNode.isDerivative() != argumentNode.isDerivative()) {
            return false;
        }
        context.ruleStack.add((ruleNode).getArgument());
        context.argumentStack.add((argumentNode).getArgument());
        return true;
    }

    private static void processExponentNode(RuleMatcherContext context, ExponentNode ruleNode, ExponentNode argumentNode) {
        context.ruleStack.add(ruleNode.getBase());
        context.argumentStack.add(argumentNode.getBase());
        context.ruleStack.add(ruleNode.getExponent());
        context.argumentStack.add(argumentNode.getExponent());
    }

    private static boolean matchDerivativeNode(RuleMatcherContext context, DerivativeNode ruleNode, DerivativeNode argumentNode) {
        Derivative ruleDerivative = ruleNode.getDerivative();
        Derivative previousDerivative = context.derivativeAliases.putIfAbsent(ruleDerivative, argumentNode.getDerivative());
        if ( previousDerivative != null && !previousDerivative.equals(argumentNode.getDerivative()) ) {
            return false;
        }
        context.ruleStack.add(ruleNode.getArgument());
        context.argumentStack.add(argumentNode.getArgument());
        return true;
    }

    private static void processAddNode(RuleMatcherContext context, AddNode ruleNode, AddNode argumentNode) {
        context.ruleStack.add(ruleNode.getLhs());
        context.argumentStack.add(argumentNode.getLhs());
        context.ruleStack.add(ruleNode.getRhs());
        context.argumentStack.add(argumentNode.getRhs());
    }

    private static void processSubtractNode(RuleMatcherContext context, SubtractNode ruleNode, SubtractNode argumentNode) {
        context.ruleStack.add(ruleNode.getLhs());
        context.argumentStack.add(argumentNode.getLhs());
        context.ruleStack.add(ruleNode.getRhs());
        context.argumentStack.add(argumentNode.getRhs());
    }

    private static void processNegateNode(RuleMatcherContext context, NegateNode ruleNode, NegateNode argumentNode) {
        context.ruleStack.add(ruleNode.getArgument());
        context.argumentStack.add(argumentNode.getArgument());
    }

    private static void processMultiplyNode(RuleMatcherContext context, MultiplyNode ruleNode, MultiplyNode argumentNode) {
        context.ruleStack.add(ruleNode.getLhs());
        context.argumentStack.add(argumentNode.getLhs());
        context.ruleStack.add(ruleNode.getRhs());
        context.argumentStack.add(argumentNode.getRhs());
    }

    private static void processDivideNode(RuleMatcherContext context, DivideNode ruleNode, DivideNode argumentNode) {
        context.ruleStack.add(ruleNode.getDividend());
        context.argumentStack.add(argumentNode.getDividend());
        context.ruleStack.add(ruleNode.getDivider());
        context.argumentStack.add(argumentNode.getDivider());
    }

    private static boolean captureFunctionNode(RuleMatcherContext context, CaptureFunctionNode ruleNode, FunctionNode argumentNode) {
        if ( ruleNode.isDerivative() != argumentNode.isDerivative()) {
            return false;
        }
        String placeholder = ruleNode.getPlaceholder();
        String function = argumentNode.getFunction();
        String previousFunction = context.functionAliases.putIfAbsent(placeholder, function);
        if ( ! (previousFunction == null || previousFunction.equals(function)) ) {
            return false;
        }
        context.ruleStack.add(ruleNode.getArgument());
        context.argumentStack.add(argumentNode.getArgument());
        return true;
    }

    private static boolean captureNode(RuleMatcherContext context, CaptureNode ruleNode, Node argumentNode) {
        String placeholder = ruleNode.getPlaceholder();
        Node previousNode = context.replacements.putIfAbsent(placeholder, argumentNode);
        return previousNode == null || previousNode.equals(argumentNode);
    }

    private boolean matchIdNode(IdNode ruleNode, IdNode argumentNode) {
        return ruleNode.getVariable().equals(argumentNode.getVariable());
    }

    private static boolean matchNodeType(NodeType ruleNodeNodeType, NodeType argumentNodeType) {
        if (ruleNodeNodeType == NodeType.capture) {
            return true;
        }
        if (ruleNodeNodeType == NodeType.captureFunction ) {
            return argumentNodeType == NodeType.function;
        }
        return ruleNodeNodeType == argumentNodeType;
    }

    private static boolean matchConstants(ConstantNode argument, ConstantNode ruleNode) {
        return ruleNode.getValue().equals(argument.getValue());
    }

    @Override
    public String toString() {
        return rule.toString();
    }
}
