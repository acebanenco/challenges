package com.acebanenco.challenge.algebra.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ExponentNode extends AbstractNode implements ComplexNode {
    private final Node base;
    private final Node exponent;

    @Override
    public NodeType getNodeType() {
        return NodeType.exponent;
    }

    @Override
    public int getOrder() {
        return 100;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExponentNode that = (ExponentNode) o;

        if (!base.equals(that.base)) return false;
        return exponent.equals(that.exponent);
    }

    @Override
    public int hashCode() {
        int result = base.hashCode();
        result = 31 * result + exponent.hashCode();
        return result;
    }
}
