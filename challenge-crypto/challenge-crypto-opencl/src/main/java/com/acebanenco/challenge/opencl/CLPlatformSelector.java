package com.acebanenco.challenge.opencl;

import java.util.List;
import java.util.NoSuchElementException;

public class CLPlatformSelector {

    public CLPlatform getPlatform(List<CLPlatform> platforms) {
        if ( platforms.isEmpty() ) {
            throw new NoSuchElementException();
        }
        return platforms.get(0);
    }
}
