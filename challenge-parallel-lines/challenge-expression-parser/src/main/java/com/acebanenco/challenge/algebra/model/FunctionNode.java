package com.acebanenco.challenge.algebra.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FunctionNode extends AbstractNode {
    private final String function;
    private final boolean derivative;
    private final Node argument;

    @Override
    public NodeType getNodeType() {
        return NodeType.function;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FunctionNode that = (FunctionNode) o;

        if (derivative != that.derivative) return false;
        if (!function.equals(that.function)) return false;
        return argument.equals(that.argument);
    }

    @Override
    public int hashCode() {
        int result = function.hashCode();
        result = 31 * result + (derivative ? 1 : 0);
        result = 31 * result + argument.hashCode();
        return result;
    }
}
