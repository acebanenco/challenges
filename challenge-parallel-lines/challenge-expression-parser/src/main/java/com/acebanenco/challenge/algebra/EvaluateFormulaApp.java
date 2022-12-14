package com.acebanenco.challenge.algebra;

import com.acebanenco.challenge.algebra.eval.RulesEvaluator;
import com.acebanenco.challenge.algebra.model.Node;
import com.acebanenco.challenge.algebra.model.NodeFactoryImpl;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class EvaluateFormulaApp {

    public static void main(String[] args) {
        String formula = "( ln(x**-1) )'x"; // derivative by x

        NodeFactoryImpl nodeFactory = new NodeFactoryImpl();
        ParseTreeWalker treeWalker = new ParseTreeWalker();
        FormulaBuilderListener builderListener = new FormulaBuilderListener(nodeFactory, treeWalker);
        Node parsedTree = builderListener.parse(formula);
        System.out.println(parsedTree);

        Node derivative = RulesEvaluator.evaluate(parsedTree);
        System.out.println(derivative);
    }


}
