package br.com.hadryan.coupon.api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BusinessError {
    ERR001("ERR001", "Coupon code is already created"),
    ERR002("ERR002", "Discount value is below minimum allowed"),
    ERR003("ERR003", "Expiration date cannot be in the past");

    private final String code;
    private final String message;
}
