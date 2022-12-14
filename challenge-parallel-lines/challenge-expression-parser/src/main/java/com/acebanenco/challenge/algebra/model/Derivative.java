package com.acebanenco.challenge.algebra.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
public final class Derivative {
    private final String argument;

    private Derivative(String argument) {
        this.argument = Objects.requireNonNull(argument);
    }

    public static List<Derivative> parse(String input) {
        if ( input.isEmpty() ) {
            return List.of();
        }
        if ( input.equals("'") ) {
            return List.of(new Derivative(""));
        }
        String[] components = input.split("'");
        return Arrays.stream(components, 1, components.length)
                .map(Derivative::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Derivative that = (Derivative) o;
        return argument.equals(that.argument);
    }

    @Override
    public int hashCode() {
        return argument.hashCode();
    }

    @Override
    public String toString() {
        return "'" + argument;
    }
}
