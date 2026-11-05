package kz.fintech.bank.common.error;

import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
public class ErrorResponseFactory {

    private final MessageSource messageSource;

    public ErrorCodeResponse create(ErrorCode errorCode, Object[] args, List<ErrorCodeResponse.FieldViolation> errors) {
        String message = messageSource.getMessage(
                errorCode.messageKey(), args, errorCode.code(), LocaleContextHolder.getLocale());

        return new ErrorCodeResponse(
                errorCode.code(),
                message,
                errorCode.status().value(),
                MDC.get("traceId"),
                Instant.now(),
                errors
        );
    }

    public ResponseEntity<ErrorCodeResponse> build(ErrorCode errorCode, Object[] args, List<ErrorCodeResponse.FieldViolation> errors) {
        return ResponseEntity.status(errorCode.status()).body(create(errorCode, args, errors));
    }

    public ResponseEntity<ErrorCodeResponse> build(ErrorCode errorCode) {
        return build(errorCode, null, List.of());
    }
}
