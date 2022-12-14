package com.acebanenco.challenge.algebra.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CaptureNode extends AbstractNode {
    private final String placeholder;

    @Override
    public NodeType getNodeType() {
        return NodeType.capture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CaptureNode that = (CaptureNode) o;

        return placeholder.equals(that.placeholder);
    }

    @Override
    public int hashCode() {
        return placeholder.hashCode();
    }
}
