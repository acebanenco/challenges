package com.acebanenco.challenge.opencl;

import org.jocl.CL;
import org.jocl.cl_context;
import org.jocl.cl_program;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.jocl.CL.clBuildProgram;
import static org.jocl.CL.clCreateProgramWithSource;

public class CLContext extends CLObject<cl_context> {

    final CLPlatform platform;
    final List<CLDevice> devices;

    public CLContext(cl_context peer, CLPlatform platform, List<CLDevice> devices) {
        super(peer, CL::clReleaseContext);
        this.platform = platform;
        this.devices = new ArrayList<>(devices);
    }

    public CLPlatform getPlatform() {
        return platform;
    }

    public List<CLDevice> getDevices() {
        return Collections.unmodifiableList(devices);
    }

    ////////////// TODO TODO TODO TODO TODO TODO /////////////////
    /*
        Retrieving the binary:
        // Create and compile program
        program = clCreateProgramWithSource(context, 1, &kernel_source, NULL, NULL);
        clBuildProgram(program, 0, NULL, NULL, NULL, NULL);

        // Get compiled binary from runtime
        size_t size;
        clGetProgramInfo(program, CL_PROGRAM_BINARY_SIZES, sizeof(size_t), &size, NULL);
        unsigned char *binaries = malloc(sizeof(unsigned char) * size);
        clGetProgramInfo(program, CL_PROGRAM_BINARIES, size, &binaries, NULL);
        // Then write binary to file

        • Loading the binary
        // Load compiled program binary from file
        …
        // Create program using binary
        program = clCreateProgramWithBinary(context, 1, devices, &size, &binaries,NULL,NULL);
        clBuildProgram(program, 0, NULL, NULL, NULL, NULL);
     */
    public CLProgram getProgram(String...sources) {
        // Create and build the program and the kernel
        cl_program program = clCreateProgramWithSource(getPeer(),
                1, sources, null, null);
        clBuildProgram(program, 0, null, null, null, null);
        return new CLProgram(program, this);
    }

    public CLMemFactory getBufferFactory(CLMemFlag... flags) {
        return new CLMemFactory(this, flags);
    }

}
