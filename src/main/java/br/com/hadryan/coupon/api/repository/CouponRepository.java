package br.com.hadryan.coupon.api.repository;

import br.com.hadryan.coupon.api.repository.data.CouponData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CouponRepository extends JpaRepository<CouponData, UUID> {

    Optional<CouponData> findByCode(String code);

}
