package com.acebanenco.challenge.crypto.openssl;

import java.security.Provider;
import java.util.List;
import java.util.Map;

public class OpenSslProvider extends Provider {

    public OpenSslProvider() {
        super("OpenSSL", "1.0.0-SNAPSHOT", "");
        putService(new Service(this, "MessageDigest", "SHA-256",
                "com.acebanenco.challenge.crypto.openssl.OpenSslEvpSpi", List.of(), Map.of()));
    }
}
