package com.acebanenco.challenge.opencl;

import java.util.Arrays;
import java.util.Objects;

public class CLProgramArgument {

    final Object argument;
    final CLMemFlag[] flags;

    public CLProgramArgument(Object argument, CLMemFlag...flags) {
        this.argument = Objects.requireNonNull(argument);
        this.flags = Arrays.copyOf(flags, flags.length);
    }

    Object getArgument() {
        return argument;
    }

    CLMemFlag[] getFlags() {
        return flags;
    }
}
