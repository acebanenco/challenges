package com.acebanenco.challenge.algebra;

import com.acebanenco.challenge.algebra.model.*;
import com.acebanenco.challenge.algebra.parser.FormulaLexer;
import com.acebanenco.challenge.algebra.parser.FormulaParser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.LinkedList;

@RequiredArgsConstructor
class FormulaBuilderListener extends com.acebanenco.challenge.algebra.parser.FormulaBaseListener {
    private final NodeFactory nodeFactory;
    private final ParseTreeWalker treeWalker;
    @Getter
    private Node result;
    private final LinkedList<Node> nodeStack = new LinkedList<>();

    @Override
    public void exitFormula(FormulaParser.FormulaContext ctx) {
        result = nodeStack.removeFirst();
    }

    @Override
    public void exitFunction(FormulaParser.FunctionContext ctx) {
        Node node = nodeStack.removeFirst();
        Token name = ctx.func;
        Token derivative = ctx.derivative;
        Node functionNode = nodeFactory.functionNode(
                name.getText(),
                derivative != null,
                node);
        nodeStack.addFirst(functionNode);
    }

    @Override
    public void exitGroup(FormulaParser.GroupContext ctx) {
        Token derivative = ctx.derivative;
        if ( derivative == null ) {
            return;
        }
        Node argument = nodeStack.removeFirst();
        Node derivativeNode = nodeFactory.derivativeNode(derivative.getText(), argument);
        nodeStack.addFirst(derivativeNode);
    }

    @Override
    public void exitId(FormulaParser.IdContext ctx) {
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
    public void exitConstant(FormulaParser.ConstantContext ctx) {
        Token constant = ctx.value;
        Node constantNode = nodeFactory.constantNode(constant.getText());
        nodeStack.addFirst(constantNode);
    }

    @Override
    public void exitExponent(FormulaParser.ExponentContext ctx) {
        Node exponent = nodeStack.removeFirst();
        Node base = nodeStack.removeFirst();
        Node exponentNode = nodeFactory.exponentNode(base, exponent);
        nodeStack.addFirst(exponentNode);
    }

    @Override
    public void exitUnary(FormulaParser.UnaryContext ctx) {
        if (ctx.unaryMinusOp != null) {
            nodeStack.addFirst(nodeFactory.negateNode(nodeStack.removeFirst()));
        }
    }

    @Override
    public void exitMultiplyOrDivide(FormulaParser.MultiplyOrDivideContext ctx) {
        Node rhs = nodeStack.removeFirst();
        Node lhs = nodeStack.removeFirst();
        if (ctx.multiplyOp != null) {
            nodeStack.addFirst(nodeFactory.multiplyNode(lhs, rhs));
        } else if (ctx.divideOp != null) {
            nodeStack.addFirst(nodeFactory.divideNode(lhs, rhs));
        }
    }

    @Override
    public void exitAddOrSubtract(FormulaParser.AddOrSubtractContext ctx) {
        Node rhs = nodeStack.removeFirst();
        Node lhs = nodeStack.removeFirst();
        if (ctx.plusOp != null) {
            nodeStack.addFirst(nodeFactory.addNode(lhs, rhs));
        } else if (ctx.minusOp != null) {
            nodeStack.addFirst(nodeFactory.subtractNode(lhs, rhs));
        }
    }

    FormulaParser parserForInput(String formula) {
        CharStream input = CharStreams.fromString(formula);
        TokenSource tokens = new FormulaLexer(input);
        TokenStream stream = new BufferedTokenStream(tokens);
        return new FormulaParser(stream);
    }

    Node parse(String formula) {
        return parse(parserForInput(formula));
    }

    Node parse(FormulaParser parser) {
        FormulaParser.FormulaContext program = parser.formula();
        treeWalker.walk(this, program);
        return result;
    }

}
