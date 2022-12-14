package com.acebanenco.challenge.crypto_simple.service;

import com.acebanenco.challenge.crypto_simple.model.CalculateHashCountRequest;
import com.acebanenco.challenge.opencl.CLContextNDRangeAction;
import com.acebanenco.challenge.opencl.CLExecution;
import com.acebanenco.challenge.opencl.CLProxyHandlerFactory;
import com.acebanenco.challenge.opencl.tutorial.Sha256Prog;
import com.acebanenco.challenge.opencl.tutorial.Sha256Sample;

class OpenCLCalculateHashCountService implements CalculateHashCountService, AutoCloseable {

    private final Sha256Prog program;
    private final CLProxyHandlerFactory<Sha256Prog> handlerFactory;

    public OpenCLCalculateHashCountService() {
        CLExecution execution = new CLExecution();
        CLContextNDRangeAction contextAction = new CLContextNDRangeAction();
        handlerFactory = execution.getHandlerFactory(Sha256Prog.class);
        program = handlerFactory.getImplementation(contextAction);
    }

    @Override
    public int getHashCount(CalculateHashCountRequest request) {
        int itemsPerTask = request.getItemsPerTask();
        int rangeStart = request.getRangeStart();
        int rangeEnd = request.getRangeEnd();

        Sha256Sample sample = new Sha256Sample(program, itemsPerTask);
        return sample.findMatchingHashCount(itemsPerTask, rangeStart, rangeEnd);
    }

    @Override
    public void close() {
        handlerFactory.close();
    }
}
