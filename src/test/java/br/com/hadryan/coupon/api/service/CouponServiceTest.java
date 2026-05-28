package br.com.hadryan.coupon.api.service;

import static org.junit.jupiter.api.Assertions.*;

import br.com.hadryan.coupon.api.domain.Coupon;
import br.com.hadryan.coupon.api.domain.enums.CouponStatus;
import br.com.hadryan.coupon.api.exception.BusinessException;
import br.com.hadryan.coupon.api.exception.CouponNotFoundException;
import br.com.hadryan.coupon.api.exception.InvalidCouponCodeException;
import br.com.hadryan.coupon.api.mapper.CouponMapper;
import br.com.hadryan.coupon.api.repository.CouponRepository;
import br.com.hadryan.coupon.api.repository.data.CouponData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private CouponMapper couponMapper;

    @InjectMocks
    private CouponService couponService;

    private Coupon coupon;

    @BeforeEach
    void setUp() {
        coupon = new Coupon();
        coupon.setCode("ab-12@cd");
        coupon.setDescription("Cupom de teste");
        coupon.setDiscountValue(BigDecimal.valueOf(0.80));
        coupon.setExpirationDate(LocalDateTime.now().plusDays(10));
        coupon.setPublished(true);
    }

    @Test
    void shouldCreateCouponAndSanitizeCode() {
        when(couponRepository.findByCode("AB12CD")).thenReturn(Optional.empty());

        CouponData mappedData = new CouponData();
        mappedData.setCode("AB12CD");
        when(couponMapper.domainToData(any(Coupon.class))).thenReturn(mappedData);

        CouponData savedData = new CouponData();
        savedData.setCode("AB12CD");
        savedData.setStatus(CouponStatus.ACTIVE);
        savedData.setRedeemed(false);
        when(couponRepository.save(any(CouponData.class))).thenReturn(savedData);

        Coupon mappedDomain = new Coupon();
        mappedDomain.setCode("AB12CD");
        mappedDomain.setStatus(CouponStatus.ACTIVE);
        mappedDomain.setRedeemed(false);
        when(couponMapper.dataToDomain(savedData)).thenReturn(mappedDomain);

        Coupon result = couponService.createCoupon(coupon);

        assertEquals("AB12CD", result.getCode());
        assertEquals(CouponStatus.ACTIVE, result.getStatus());
        assertFalse(result.getRedeemed());

        ArgumentCaptor<Coupon> couponCaptor = ArgumentCaptor.forClass(Coupon.class);
        verify(couponMapper).domainToData(couponCaptor.capture());
        assertEquals("AB12CD", couponCaptor.getValue().getCode());
        assertEquals(CouponStatus.ACTIVE, couponCaptor.getValue().getStatus());
        assertFalse(couponCaptor.getValue().getRedeemed());
    }

    @Test
    void shouldThrowWhenCodeAlreadyExists() {
        when(couponRepository.findByCode("AB12CD")).thenReturn(Optional.of(new CouponData()));

        assertThrows(BusinessException.class, () -> couponService.createCoupon(coupon));
        verify(couponRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenDiscountBelowMinimum() {
        coupon.setDiscountValue(BigDecimal.valueOf(0.49));
        when(couponRepository.findByCode("AB12CD")).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> couponService.createCoupon(coupon));
        verify(couponRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenExpirationDateIsInPast() {
        coupon.setExpirationDate(LocalDateTime.now().minusMinutes(1));
        when(couponRepository.findByCode("AB12CD")).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> couponService.createCoupon(coupon));
        verify(couponRepository, never()).save(any());
    }

    @Test
    void shouldThrowInvalidCouponCodeWhenSanitizedLengthLessThanSix() {
        coupon.setCode("a@1");
        assertThrows(InvalidCouponCodeException.class, () -> couponService.createCoupon(coupon));
    }

    @Test
    void shouldGetCouponById() {
        UUID id = UUID.randomUUID();
        CouponData data = new CouponData();
        Coupon expected = new Coupon();
        expected.setCode("ABC123");

        when(couponRepository.findById(id)).thenReturn(Optional.of(data));
        when(couponMapper.dataToDomain(data)).thenReturn(expected);

        Coupon result = couponService.getCouponById(id);

        assertEquals("ABC123", result.getCode());
    }

    @Test
    void shouldThrowWhenCouponNotFoundById() {
        UUID id = UUID.randomUUID();
        when(couponRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CouponNotFoundException.class, () -> couponService.getCouponById(id));
    }
}