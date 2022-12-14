package com.acebanenco.challenge.crypto_simple;

import com.acebanenco.challenge.crypto_simple.model.CalculateHashCountRequest;
import com.acebanenco.challenge.crypto_simple.service.CalculateHashCountService;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;

import java.io.IOException;

public class CalculateHashesSimpleApp {


    public static void main(String[] args) throws IOException {
        org.openjdk.jmh.Main.main(args);
    }

    @Fork(value = 1, warmups = 1)
    @Benchmark()
    @BenchmarkMode({Mode.SingleShotTime})
    public void runBenchMark(CalculateHashesExecutionPlan executionPlan) {
        CalculateHashCountRequest request = CalculateHashCountRequest.builder()
                .rangeStart(executionPlan.rangeStart)
                .rangeEnd(executionPlan.rangeEnd)
                .itemsPerTask(executionPlan.itemsPerTask)
                .message(executionPlan.message)
                .build();
        CalculateHashCountService service = executionPlan.service;
        service.getHashCount(request);
    }


}
