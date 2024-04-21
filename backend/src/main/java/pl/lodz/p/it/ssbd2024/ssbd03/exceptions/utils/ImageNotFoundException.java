package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.utils;

import java.io.FileNotFoundException;

public class ImageNotFoundException extends FileNotFoundException {

    public ImageNotFoundException(String s) {
        super(s);
    }
}
