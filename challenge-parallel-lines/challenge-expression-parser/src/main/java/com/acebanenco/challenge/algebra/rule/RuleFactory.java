package com.acebanenco.challenge.algebra.rule;

import com.acebanenco.challenge.algebra.model.*;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class RuleFactory {

    private final RulesBuilderListener listener;
    private final RulesLoader rulesLoader;

    public Map<NodeType, List<RuleMatcher>> getRules(String resourceName) {
        String rulesText = rulesLoader.loadRulesText(resourceName);
        List<Rule> rules = listener.parse(rulesText);
        return rules.stream()
                .map(RuleMatcher::new)
                .collect(Collectors.groupingBy(
                        ruleMatcher -> {
                            Rule rule = ruleMatcher.getRule();
                            Node pattern = rule.getPattern();
                            return pattern.getNodeType();
                        }
                ));
    }

}
