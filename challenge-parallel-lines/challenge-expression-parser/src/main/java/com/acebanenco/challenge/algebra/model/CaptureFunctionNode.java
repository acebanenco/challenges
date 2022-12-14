package com.acebanenco.challenge.algebra.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CaptureFunctionNode extends AbstractNode {
    private final String placeholder;
    private final boolean derivative;
    private final Node argument;

    @Override
    public NodeType getNodeType() {
        return NodeType.captureFunction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CaptureFunctionNode that = (CaptureFunctionNode) o;

        if (derivative != that.derivative) return false;
        if (!placeholder.equals(that.placeholder)) return false;
        return argument.equals(that.argument);
    }

    @Override
    public int hashCode() {
        int result = placeholder.hashCode();
        result = 31 * result + (derivative ? 1 : 0);
        result = 31 * result + argument.hashCode();
        return result;
    }
}
