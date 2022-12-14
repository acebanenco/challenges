package com.acebanenco.challenge.algebra.rule;

import com.acebanenco.challenge.algebra.model.Derivative;
import com.acebanenco.challenge.algebra.model.DerivativeNode;
import com.acebanenco.challenge.algebra.model.Node;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class RuleMatcherContext {
    final LinkedList<Node> ruleStack = new LinkedList<>();
    final LinkedList<Node> argumentStack = new LinkedList<>();
    final Map<String, Node> replacements = new HashMap<>();
    final Map<String, String> functionAliases = new HashMap<>();
    final Map<Derivative, Derivative> derivativeAliases = new HashMap<>();
}
