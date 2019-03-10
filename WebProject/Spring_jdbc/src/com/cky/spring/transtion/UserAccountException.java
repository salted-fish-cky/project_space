package com.cky.spring.transtion;

public class UserAccountException extends RuntimeException {
    public UserAccountException() {
    }

    public UserAccountException(String s) {
        super(s);
    }

    public UserAccountException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public UserAccountException(Throwable throwable) {
        super(throwable);
    }

    public UserAccountException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
