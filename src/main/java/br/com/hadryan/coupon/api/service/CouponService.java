package br.com.hadryan.coupon.api.service;

import br.com.hadryan.coupon.api.domain.Coupon;
import br.com.hadryan.coupon.api.domain.enums.CouponStatus;
import br.com.hadryan.coupon.api.exception.BusinessError;
import br.com.hadryan.coupon.api.exception.BusinessException;
import br.com.hadryan.coupon.api.exception.CouponNotFoundException;
import br.com.hadryan.coupon.api.exception.InvalidCouponCodeException;
import br.com.hadryan.coupon.api.mapper.CouponMapper;
import br.com.hadryan.coupon.api.mapper.dto.CouponResponse;
import br.com.hadryan.coupon.api.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CouponService {
    private final CouponRepository couponRepository;
    private final CouponMapper couponMapper;

    public Coupon getCouponById(UUID id) {
        return couponRepository.findById(id)
                .map(couponMapper::dataToDomain)
                .orElseThrow(() -> new CouponNotFoundException("Coupon not found with id: " + id));
    }

    public Coupon createCoupon(Coupon coupon) {
        coupon.setCode(sanitizeCode(coupon.getCode()));
        validateCouponData(coupon);
        coupon.setStatus(CouponStatus.ACTIVE);
        coupon.setRedeemed(false);
        var saved = couponRepository.save(couponMapper.domainToData(coupon));
        return couponMapper.dataToDomain(saved);
    }

    public void deleteCoupon(UUID id) {
        var couponToDelete = couponRepository.findById(id)
                .orElseThrow(() -> new CouponNotFoundException("Coupon not found with id: " + id));

        couponToDelete.setStatus(CouponStatus.DELETED);
        couponRepository.save(couponToDelete);
    }

    private void validateCouponData(Coupon coupon) {
        couponRepository.findByCode(coupon.getCode()).ifPresent(_ -> {
            throw new BusinessException(BusinessError.ERR001, 422);
        });

        if (coupon.getDiscountValue().compareTo(BigDecimal.valueOf(0.5)) < 0) {
            throw new BusinessException(BusinessError.ERR002, 422);
        }

        if (coupon.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new BusinessException(BusinessError.ERR003, 422);
        }
    }

    private String sanitizeCode(String code) {
        String sanitized = code.replaceAll("[^a-zA-Z0-9]", "").toUpperCase();

        if (sanitized.length() < 6) {
            throw new InvalidCouponCodeException("Code must have at least 6 alphanumeric characters after removing special characters. Result was: '" +
                    sanitized + "' (" + sanitized.length() + " characters)");
        }

        return sanitized.substring(0, 6);
    }
}
