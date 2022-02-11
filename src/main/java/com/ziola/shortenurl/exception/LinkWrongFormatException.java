package com.ziola.shortenurl.exception;

public class LinkWrongFormatException extends RuntimeException {
    public LinkWrongFormatException(String s) {
        super(s);
    }
}
