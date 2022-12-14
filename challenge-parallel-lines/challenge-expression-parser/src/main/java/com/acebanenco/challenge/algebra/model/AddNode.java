package com.acebanenco.challenge.algebra.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AddNode extends AbstractNode implements ComplexNode {
    private final Node lhs;
    private final Node rhs;

    @Override
    public NodeType getNodeType() {
        return NodeType.add;
    }

    @Override
    public int getOrder() {
        return 300;
    }

    public AddNode swap() {
        return new AddNode(rhs, lhs);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AddNode addNode = (AddNode) o;

        if (!lhs.equals(addNode.lhs)) return false;
        return rhs.equals(addNode.rhs);
    }

    @Override
    public int hashCode() {
        int result = lhs.hashCode();
        result = 31 * result + rhs.hashCode();
        return result;
    }
}
