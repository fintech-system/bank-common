package kz.fintech.bank.common.error;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ErrorCodeResponse(
        String code,
        String message,
        int status,
        String traceId,
        Instant timestamp,
        List<FieldViolation> errors
) {
    public record FieldViolation(String field, String message) {
    }
}
