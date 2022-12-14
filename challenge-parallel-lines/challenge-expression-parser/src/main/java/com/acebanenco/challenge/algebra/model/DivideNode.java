package com.acebanenco.challenge.algebra.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DivideNode extends AbstractNode implements ComplexNode {
    private final Node dividend;
    private final Node divider;

    @Override
    public NodeType getNodeType() {
        return NodeType.divide;
    }

    @Override
    public int getOrder() {
        return 200;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DivideNode that = (DivideNode) o;

        if (!dividend.equals(that.dividend)) return false;
        return divider.equals(that.divider);
    }

    @Override
    public int hashCode() {
        int result = dividend.hashCode();
        result = 31 * result + divider.hashCode();
        return result;
    }
}
