package com.acebanenco.challenge.opencl;

import org.jocl.CL;
import org.jocl.NativePointerObject;

import java.util.Objects;
import java.util.function.Consumer;

public abstract class CLObject <P extends NativePointerObject> implements AutoCloseable {

    static {
        CL.setExceptionsEnabled(true);
    }

    private final P peer;

    private final Consumer<P> closeFn;

    protected CLObject(P peer, Consumer<P> closeFn) {
        this.peer = Objects.requireNonNull(peer);
        this.closeFn = Objects.requireNonNull(closeFn);
    }

    P getPeer() {
        return peer;
    }

    @Override
    public void close() {
        closeFn.accept(peer);
    }
}
