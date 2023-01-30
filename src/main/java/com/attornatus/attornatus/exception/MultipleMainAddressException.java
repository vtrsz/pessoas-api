package com.attornatus.attornatus.exception;

import java.io.Serial;

public class MultipleMainAddressException extends Exception {
    @Serial
    private static final long serialVersionUID = 5841829696836632879L;

    public MultipleMainAddressException(String message) {
        super(message);
    }
}
