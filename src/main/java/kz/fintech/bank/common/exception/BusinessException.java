package kz.fintech.bank.common.exception;

import kz.fintech.bank.common.error.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Object[] args;

    public BusinessException(ErrorCode errorCode, Object... args) {
        super(errorCode.code());
        this.errorCode = errorCode;
        this.args = args;
    }
}
