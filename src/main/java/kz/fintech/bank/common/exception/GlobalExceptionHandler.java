package kz.fintech.bank.common.exception;

import kz.fintech.bank.common.error.CommonErrorCode;
import kz.fintech.bank.common.error.ErrorCodeResponse;
import kz.fintech.bank.common.error.ErrorResponseFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@Slf4j
@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ErrorResponseFactory responseFactory;
    private final MessageSource messageSource;

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorCodeResponse> handleBusiness(BusinessException e) {
        log.warn("Business error: {}", e.getErrorCode().code());
        return responseFactory.build(e.getErrorCode(), e.getArgs(), List.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorCodeResponse> handleValidation(MethodArgumentNotValidException e) {
        List<ErrorCodeResponse.FieldViolation> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new ErrorCodeResponse.FieldViolation(
                        error.getField(),
                        messageSource.getMessage(error, LocaleContextHolder.getLocale())))
                .toList();
        return responseFactory.build(CommonErrorCode.VALIDATION_FAILED, null, violations);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorCodeResponse> handleNotReadable(HttpMessageNotReadableException e) {
        return responseFactory.build(CommonErrorCode.MALFORMED_REQUEST);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorCodeResponse> handleNotFound(NoResourceFoundException e) {
        return responseFactory.build(CommonErrorCode.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorCodeResponse> handleUnexpected(Exception e) {
        log.error("Unexpected error", e);
        return responseFactory.build(CommonErrorCode.INTERNAL_ERROR);
    }
}
