package kz.fintech.bank.common.exception;

import kz.fintech.bank.common.error.CommonErrorCode;
import kz.fintech.bank.common.error.ErrorCodeResponse;
import kz.fintech.bank.common.error.ErrorResponseFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE - 1)
@RequiredArgsConstructor
public class SecurityExceptionHandler {

    private final ErrorResponseFactory responseFactory;

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorCodeResponse> handleAuthentication(AuthenticationException e) {
        return responseFactory.build(CommonErrorCode.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorCodeResponse> handleAccessDenied(AccessDeniedException e) {
        return responseFactory.build(CommonErrorCode.FORBIDDEN);
    }
}
