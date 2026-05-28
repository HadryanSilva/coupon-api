package br.com.hadryan.coupon.api.mapper.dto;

import br.com.hadryan.coupon.api.domain.enums.CouponStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CouponResponse(
        UUID id,
        String code,
        String description,
        BigDecimal discountValue,
        LocalDateTime expirationDate,
        CouponStatus status,
        Boolean published,
        Boolean redeemed
) {
}
