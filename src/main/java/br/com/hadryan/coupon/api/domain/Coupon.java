package br.com.hadryan.coupon.api.domain;

import br.com.hadryan.coupon.api.domain.enums.CouponStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class Coupon {
    private UUID id;
    private String code;
    private String description;
    private BigDecimal discountValue;
    private LocalDateTime expirationDate;
    private CouponStatus status;
    private Boolean published;
    private Boolean redeemed;
}
