package com.acebanenco.challenge.algebra.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SubtractNode extends AbstractNode implements ComplexNode {
    private final Node lhs;
    private final Node rhs;

    @Override
    public NodeType getNodeType() {
        return NodeType.subtract;
    }

    @Override
    public int getOrder() {
        return 300;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubtractNode that = (SubtractNode) o;

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
