package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.utils;

import java.io.FileNotFoundException;

/**
 * Used to specify an Exception related with finding an image attached to an email.
 */
public class ImageNotFoundException extends FileNotFoundException {

    public ImageNotFoundException(String s) {
        super(s);
    }
}
