package com.acebanenco.challenge.algebra.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MultiplyNode extends AbstractNode implements ComplexNode {
    private final Node lhs;
    private final Node rhs;

    @Override
    public NodeType getNodeType() {
        return NodeType.multiply;
    }

    @Override
    public int getOrder() {
        return 200;
    }

    public MultiplyNode swap() {
        return new MultiplyNode(rhs, lhs);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MultiplyNode that = (MultiplyNode) o;

        if (!lhs.equals(that.lhs)) return false;
        return rhs.equals(that.rhs);
    }

    @Override
    public int hashCode() {
        int result = lhs.hashCode();
        result = 31 * result + rhs.hashCode();
        return result;
    }
}
