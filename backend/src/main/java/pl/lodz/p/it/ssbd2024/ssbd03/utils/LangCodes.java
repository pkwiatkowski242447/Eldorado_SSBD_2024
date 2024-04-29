package pl.lodz.p.it.ssbd2024.ssbd03.utils;

import lombok.Getter;

@Getter
public enum LangCodes {
    PL("pl"), EN("en");

    private final String code;

    LangCodes(String code) {
        this.code = code;
    }
}
