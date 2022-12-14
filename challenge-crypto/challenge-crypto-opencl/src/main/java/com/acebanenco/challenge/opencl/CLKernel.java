package com.acebanenco.challenge.opencl;

import org.jocl.CL;
import org.jocl.Pointer;
import org.jocl.Sizeof;
import org.jocl.cl_kernel;

import static org.jocl.CL.clEnqueueNDRangeKernel;
import static org.jocl.CL.clSetKernelArg;

public class CLKernel extends CLObject<cl_kernel> implements AutoCloseable {

    final CLProgram program;

    CLKernel(cl_kernel peer, CLProgram program) {
        super(peer, CL::clReleaseKernel);
        this.program = program;
    }

    public void executeNDRange(
            CLDevice device,
            CLNDRange ndRange,
            CLProgramArgument... arguments) {

        CLContext context = program.getContext();

        try (CLMemList buffers = toBuffers(context, arguments);
             CLCommandQueue commandQueue = device.getQueue(context)) {

            executeNDRange(commandQueue, ndRange);

            for (CLMem buffer : buffers.getBuffers()) {
                if (buffer.getFactory().isWriteable()) {
                    commandQueue.readBlocking(buffer);
                }
            }
        }
    }

    private CLMemList toBuffers(CLContext context, CLProgramArgument[] arguments) {
        CLMem[] buffers = new CLMem[arguments.length];

        int argIndex = 0;
        for (CLProgramArgument argument : arguments) {
            CLMemFlag[] flags = argument.getFlags();
            CLMemFactory bufferFactory = context.getBufferFactory(flags);

            CLMem buffer = bufferFactory.getBuffer(argument.getArgument());
            Pointer pointer = Pointer.to(buffer.getPeer());

            clSetKernelArg(getPeer(), argIndex, Sizeof.cl_mem, pointer);
            buffers[argIndex] = buffer;
            argIndex++;
        }
        return new CLMemList(buffers);
    }

    private void executeNDRange(CLCommandQueue queue, CLNDRange ndRange) {
        // Execute the kernel
        long[] global_work_size = {ndRange.getGlobalWorkSize()};
        clEnqueueNDRangeKernel(queue.getPeer(), getPeer(), 1, null,
                global_work_size, null, 0, null, null);
    }

}
