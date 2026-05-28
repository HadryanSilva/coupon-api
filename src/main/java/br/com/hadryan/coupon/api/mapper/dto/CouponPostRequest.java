package br.com.hadryan.coupon.api.mapper.dto;

import br.com.hadryan.coupon.api.domain.enums.CouponStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CouponPostRequest(
        @NotBlank(message = "code could not be null or empty")
        String code,
        @NotBlank(message = "description could not be null or empty")
        String description,
        @NotNull(message = "discountValue could not be null")
        BigDecimal discountValue,
        @NotNull(message = "expirationDate could not be null")
        LocalDateTime expirationDate,
        Boolean published
) {
    public CouponPostRequest {
        if (published == null) {
            published = false;
        }
    }
}
