package br.com.hadryan.coupon.api.exception;

public class InvalidCouponCodeException extends RuntimeException {
    public InvalidCouponCodeException(String message) {
        super(message);
    }
}
