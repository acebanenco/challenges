package com.acebanenco.challenge.algebra.rule;

import com.acebanenco.challenge.algebra.model.Derivative;
import com.acebanenco.challenge.algebra.model.Node;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class RuleReplacements {
    private final Map<String, Node> nodeAliases;
    private final Map<String, String> functionAliases;
    private final Map<Derivative, Derivative> derivativeAliases;
}
