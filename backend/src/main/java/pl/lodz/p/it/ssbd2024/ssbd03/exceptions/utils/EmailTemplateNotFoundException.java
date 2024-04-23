package pl.lodz.p.it.ssbd2024.ssbd03.exceptions.utils;

import java.io.FileNotFoundException;

/**
 * Used to specify an Exception related with finding an email template.
 */
public class EmailTemplateNotFoundException extends FileNotFoundException {
    public EmailTemplateNotFoundException(String message) {
        super(message);
    }
}
