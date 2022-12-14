package com.acebanenco.challenge.opencl;

import org.jocl.cl_platform_id;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.jocl.CL.clGetPlatformIDs;

public class CLPlatformFactory {

    final CLPlatformSelector platformSelector;

    public CLPlatformFactory(CLPlatformSelector platformSelector) {
        this.platformSelector = platformSelector;
    }

    public CLPlatform getPlatform() {
        // Obtain the number of platforms
        int[] numPlatformsArray = new int[1];
        clGetPlatformIDs(0, null, numPlatformsArray);
        int numPlatforms = numPlatformsArray[0];

        // Obtain a platform ID
        cl_platform_id[] platformIds = new cl_platform_id[numPlatforms];
        clGetPlatformIDs(platformIds.length, platformIds, null);

        List<CLPlatform> platforms = Arrays.stream(platformIds)
                .map(CLPlatform::new)
                .collect(Collectors.toList());
        return platformSelector.getPlatform(platforms);
    }

}
