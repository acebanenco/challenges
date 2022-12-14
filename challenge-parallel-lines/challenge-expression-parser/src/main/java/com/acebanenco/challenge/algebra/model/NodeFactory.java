package com.acebanenco.challenge.algebra.model;

public interface NodeFactory {

    Node functionNode(String func, boolean derivative, Node argument);

    Node derivativeNode(String derivative, Node argument);

    Node idNode(String variable);

    Node constantNode(String value);

    Node exponentNode(Node base, Node exponent);

    Node negateNode(Node argument);

    Node multiplyNode(Node lhs, Node rhs);

    Node divideNode(Node dividend, Node divider);

    Node addNode(Node lhs, Node rhs);

    Node subtractNode(Node lhs, Node rhs);
}
