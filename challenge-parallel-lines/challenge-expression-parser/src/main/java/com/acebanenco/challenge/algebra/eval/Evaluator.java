package com.acebanenco.challenge.algebra.eval;

import com.acebanenco.challenge.algebra.model.Node;

public interface Evaluator<T> {
    T eval(Node node);
}
