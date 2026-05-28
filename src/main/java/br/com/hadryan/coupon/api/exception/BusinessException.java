package br.com.hadryan.coupon.api.exception;

import lombok.Getter;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class BusinessException extends RuntimeException {
    private final int statusCode;
    private final Map<String, String> response;

    public BusinessException(final BusinessError error,
                             final int statusCode
    ) {
        super(String.format("%s: %s", error.getCode(), error.getMessage()));
        this.statusCode = statusCode;
        Map<String, String> map = new LinkedHashMap<>();
        map.put("error_code", error.getCode());
        map.put("error_message", error.getMessage());
        this.response = Collections.unmodifiableMap(map);
    }
}
