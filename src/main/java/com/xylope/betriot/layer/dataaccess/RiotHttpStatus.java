package com.xylope.betriot.layer.dataaccess;

import com.xylope.betriot.exception.StatusNotFountException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum RiotHttpStatus {
    BAD_REQUEST(400, "Bad request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    DATA_NOT_FOUND(404, "Data not found"),
    METHOD_NOT_ALLOWED(405, "Method not allowed"),
    UNSUPPORTED_MEDIA_TYPE(415, "Unsupported media type"),
    RATE_LIMIT_EXCEEDED(429, "Rate limit exceeded"),
    INTERNAL_SERVER_ERROR(500, "Internal server error"),
    BAD_GATEWAY(502, "Bad gateway"),
    SERVICE_UNAVAILABLE(503, "Service unavailable"),
    GATEWAY_TIMEOUT(504, "Gateway timeout");

    @Getter
    int httpStatusCode;
    @Getter
    String reason;

    public static RiotHttpStatus getByCode(int httpStatusCode) {
        for(RiotHttpStatus status : values()) {
            if(status.httpStatusCode == httpStatusCode)
                return status;
        }
        throw new StatusNotFountException("Status which httpStatusCode is " + httpStatusCode + " is not found!");
    }
}
