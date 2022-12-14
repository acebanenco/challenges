package com.acebanenco.challenge.algebra.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NegateNode extends AbstractNode {
    private final Node argument;

    @Override
    public NodeType getNodeType() {
        return NodeType.negate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NegateNode that = (NegateNode) o;

        return argument.equals(that.argument);
    }

    @Override
    public int hashCode() {
        return argument.hashCode();
    }
}
