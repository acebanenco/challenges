package com.acebanenco.challenge.algebra;

import com.acebanenco.challenge.algebra.eval.Arithmetics;
import com.acebanenco.challenge.algebra.eval.RulesEvaluator;
import com.acebanenco.challenge.algebra.model.NodeFactory;
import com.acebanenco.challenge.algebra.model.NodeFactoryImpl;
import com.acebanenco.challenge.algebra.model.NodeType;
import com.acebanenco.challenge.algebra.rule.RuleFactory;
import com.acebanenco.challenge.algebra.rule.RuleMatcher;
import com.acebanenco.challenge.algebra.rule.RulesBuilderListener;
import com.acebanenco.challenge.algebra.rule.RulesLoader;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.List;
import java.util.Map;

public class ObjectFactory {

    private static final ObjectFactory instance = new ObjectFactory();

    private final Map<NodeType, List<RuleMatcher>> rules;

    public ObjectFactory() {
        NodeFactory nodeFactory = new NodeFactoryImpl();
        ParseTreeWalker treeWalker = new ParseTreeWalker();
        RulesBuilderListener builderListener = new RulesBuilderListener(nodeFactory, treeWalker);
        RulesLoader rulesLoader = new RulesLoader();
        RuleFactory ruleFactory = new RuleFactory(builderListener, rulesLoader);
        this.rules = ruleFactory.getRules("rules.txt");
    }

    public static ObjectFactory getInstance() {
        return instance;
    }

    public RulesEvaluator getDerivativeEvaluator() {
        return new RulesEvaluator(rules, new Arithmetics());
    }
}
