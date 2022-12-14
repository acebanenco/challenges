package com.acebanenco.challenge.crypto_old.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HexFormat;

@Setter
public class HexFormatBuilder {

    private String delimiter = "";
    private String prefix = "";
    private String suffix = "";

    public HexFormat build() {
        return HexFormat.ofDelimiter(delimiter)
                .withPrefix(prefix)
                .withSuffix(suffix);
    }
}
