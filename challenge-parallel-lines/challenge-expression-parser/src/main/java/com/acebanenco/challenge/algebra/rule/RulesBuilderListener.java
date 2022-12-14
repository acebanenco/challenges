package com.acebanenco.challenge.algebra.rule;

import com.acebanenco.challenge.algebra.model.*;
import com.acebanenco.challenge.algebra.parser.RulesBaseListener;
import com.acebanenco.challenge.algebra.parser.RulesLexer;
import com.acebanenco.challenge.algebra.parser.RulesParser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
public class RulesBuilderListener extends RulesBaseListener {
    private final NodeFactory nodeFactory;
    private final ParseTreeWalker treeWalker;
    @Getter
    private final List<Rule> result = new ArrayList<>();
    private final LinkedList<Node> nodeStack = new LinkedList<>();

    @Override
    public void exitSubst(RulesParser.SubstContext ctx) {
        Node replacement = nodeStack.removeFirst();
        Node pattern = nodeStack.removeFirst();
        Rule rule = new Rule(pattern, replacement);
        result.add(rule);
        if ( pattern instanceof AddNode addNode ) {
            result.add(new Rule(addNode.swap(), replacement));
        } else if ( pattern instanceof MultiplyNode multiplyNode ) {
            result.add(new Rule(multiplyNode.swap(), replacement));
        }
    }

    @Override
    public void exitFunction(RulesParser.FunctionContext ctx) {
        Node node = nodeStack.removeFirst();
        Token name = ctx.func;
        Token derivative = ctx.derivative;
        Node functionNode = nodeFactory.functionNode(
                name.getText(),
                derivative != null && !derivative.getText().isEmpty(),
                node);
        nodeStack.addFirst(functionNode);
    }

    @Override
    public void exitGroup(RulesParser.GroupContext ctx) {
        Token derivative = ctx.derivative;
        if ( derivative == null ) {
            return;
        }
        Node argument = nodeStack.removeFirst();
        Node derivativeNode = nodeFactory.derivativeNode(derivative.getText(), argument);
        nodeStack.addFirst(derivativeNode);
    }

    @Override
    public void exitId(RulesParser.IdContext ctx) {
        Token identifier = ctx.name;
        Token derivative = ctx.derivative;
        Node idNode = nodeFactory.idNode(identifier.getText());
        if (derivative == null) {
            nodeStack.addFirst(idNode);
        } else {
            Node derivativeNode = nodeFactory.derivativeNode(derivative.getText(), idNode);
            nodeStack.addFirst(derivativeNode);
        }
    }

    @Override
    public void exitConstant(RulesParser.ConstantContext ctx) {
        Token constant = ctx.value;
        Node constantNode = nodeFactory.constantNode(constant.getText());
        nodeStack.addFirst(constantNode);
    }

    @Override
    public void exitExponent(RulesParser.ExponentContext ctx) {
        Node exponent = nodeStack.removeFirst();
        Node base = nodeStack.removeFirst();
        Node exponentNode = nodeFactory.exponentNode(base, exponent);
        nodeStack.addFirst(exponentNode);
    }

    @Override
    public void exitUnary(RulesParser.UnaryContext ctx) {
        if (ctx.unaryMinusOp != null) {
            nodeStack.addFirst(nodeFactory.negateNode(nodeStack.removeFirst()));
        }
    }

    @Override
    public void exitMultiplyOrDivide(RulesParser.MultiplyOrDivideContext ctx) {
        Node rhs = nodeStack.removeFirst();
        Node lhs = nodeStack.removeFirst();
        if (ctx.multiplyOp != null) {
            nodeStack.addFirst(nodeFactory.multiplyNode(lhs, rhs));
        } else if (ctx.divideOp != null) {
            nodeStack.addFirst(nodeFactory.divideNode(lhs, rhs));
        }
    }

    @Override
    public void exitAddOrSubtract(RulesParser.AddOrSubtractContext ctx) {
        Node rhs = nodeStack.removeFirst();
        Node lhs = nodeStack.removeFirst();
        if (ctx.plusOp != null) {
            nodeStack.addFirst(nodeFactory.addNode(lhs, rhs));
        } else if (ctx.minusOp != null) {
            nodeStack.addFirst(nodeFactory.subtractNode(lhs, rhs));
        }
    }

    RulesParser parserForInput(String formula) {
        CharStream input = CharStreams.fromString(formula);
        TokenSource tokens = new RulesLexer(input);
        TokenStream stream = new BufferedTokenStream(tokens);
        return new RulesParser(stream);
    }

    List<Rule> parse(String formula) {
        return parse(parserForInput(formula));
    }

    List<Rule> parse(RulesParser parser) {
        RulesParser.RulesContext program = parser.rules();
        treeWalker.walk(this, program);

        List<Rule> copy = new ArrayList<>(result);
        result.clear();
        return copy;
    }
}
