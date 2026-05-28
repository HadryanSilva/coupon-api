package br.com.hadryan.coupon.api.mapper;

import br.com.hadryan.coupon.api.domain.Coupon;
import br.com.hadryan.coupon.api.mapper.dto.CouponPostRequest;
import br.com.hadryan.coupon.api.mapper.dto.CouponResponse;
import br.com.hadryan.coupon.api.repository.data.CouponData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CouponMapper {

    Coupon dataToDomain(CouponData couponData);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "redeemed", ignore = true)
    Coupon postRequestToDomain(CouponPostRequest couponPostRequest);

    CouponData domainToData(Coupon coupon);

    CouponResponse domainToResponse(Coupon coupon);

}
