package com.acebanenco.challenge.algebra.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ConstantNode extends AbstractNode {

    public static final ConstantNode ZERO = new ConstantNode("0");
    public static final ConstantNode ONE = new ConstantNode("1");
    public static final ConstantNode TWO = new ConstantNode("2");

    private final String value;

    @Override
    public NodeType getNodeType() {
        return NodeType.constant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConstantNode that = (ConstantNode) o;

        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
