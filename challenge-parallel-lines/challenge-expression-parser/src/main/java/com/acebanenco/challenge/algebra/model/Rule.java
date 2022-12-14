package com.acebanenco.challenge.algebra.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Rule {
    private final Node pattern;
    private final Node template;

    @Override
    public String toString() {
        return pattern + " = " + template;
    }
}
