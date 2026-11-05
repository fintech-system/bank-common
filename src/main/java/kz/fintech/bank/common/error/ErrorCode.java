package kz.fintech.bank.common.error;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

    String code();

    HttpStatus status();

    default String messageKey() {
        return "error." + code();
    }
}
