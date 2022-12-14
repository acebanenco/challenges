package com.acebanenco.challenge.algebra.model;

import com.acebanenco.challenge.algebra.eval.ToStringEvaluator;

public abstract class AbstractNode implements Node {

    private String toString = null;

    @Override
    public String toString() {
        if ( toString == null ) {
            // suppose node is immutable
            toString = ToStringEvaluator.shared.eval(this);
        }
        return toString;
    }

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object obj);
}
