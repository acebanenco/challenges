package com.acebanenco.challenge.algebra.eval;

import com.acebanenco.challenge.algebra.model.*;
import com.acebanenco.challenge.algebra.rule.RuleReplacements;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class RuleReplacementEvaluator extends CopyEvaluator {
    private final RuleReplacements replacements;

    @Override
    protected Node evalImpl(CaptureNode node) {
        return replacements.getNodeAliases().getOrDefault(node.getPlaceholder(), node);
    }

    @Override
    protected Node evalImpl(CaptureFunctionNode node) {
        Map<String, String> functionAliases = replacements.getFunctionAliases();
        String nodeFunction = node.getPlaceholder();
        String function = functionAliases.get(nodeFunction);
        if (function == null) {
            return super.evalImpl(node);
        }
        return new FunctionNode(function, node.isDerivative(), eval(node.getArgument()));
    }

    @Override
    protected Node evalImpl(DerivativeNode node) {
        Map<Derivative, Derivative> derivativeAliases = replacements.getDerivativeAliases();
        Derivative nodeDerivative = node.getDerivative();
        Derivative derivative = derivativeAliases.get(nodeDerivative);
        if ( derivative == null ) {
            return super.evalImpl(node);
        }
        return new DerivativeNode(derivative, eval(node.getArgument()));
    }
}
