package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.utils;

import java.io.FileNotFoundException;

public class EmailTemplateNotFoundException extends FileNotFoundException {
    public EmailTemplateNotFoundException(String message) {
        super(message);
    }
}
