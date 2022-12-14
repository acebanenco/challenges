package com.acebanenco.challenge.algebra.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DerivativeNode extends AbstractNode {
    private final Derivative derivative;
    private final Node argument;

    @Override
    public NodeType getNodeType() {
        return NodeType.derivative;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DerivativeNode that = (DerivativeNode) o;

        if (!derivative.equals(that.derivative)) return false;
        return argument.equals(that.argument);
    }

    @Override
    public int hashCode() {
        int result = derivative.hashCode();
        result = 31 * result + argument.hashCode();
        return result;
    }
}
