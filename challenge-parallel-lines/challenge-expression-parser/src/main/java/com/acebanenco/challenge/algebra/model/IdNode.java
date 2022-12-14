package com.acebanenco.challenge.algebra.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class IdNode extends AbstractNode {
    private final String variable;

    @Override
    public NodeType getNodeType() {
        return NodeType.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IdNode idNode = (IdNode) o;

        return variable.equals(idNode.variable);
    }

    @Override
    public int hashCode() {
        return variable.hashCode();
    }
}
