package com.acebanenco.challenge.algebra.eval;

import com.acebanenco.challenge.algebra.model.AbstractNode;
import com.acebanenco.challenge.algebra.model.ConstantNode;

public interface CalculationContext {

    ConstantNode negate(ConstantNode value);

    AbstractNode add(ConstantNode lhsCn, ConstantNode value);

    boolean equals(ConstantNode lhs, ConstantNode rhs);

    AbstractNode invokeFunction(String function, ConstantNode argument);
}
