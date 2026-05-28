package br.com.hadryan.coupon.api.controller;

import br.com.hadryan.coupon.api.mapper.CouponMapper;
import br.com.hadryan.coupon.api.mapper.dto.CouponPostRequest;
import br.com.hadryan.coupon.api.mapper.dto.CouponResponse;
import br.com.hadryan.coupon.api.service.CouponService;
import br.com.hadryan.coupon.api.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/coupons")
public class CouponController {

    private final CouponService couponService;
    private final CouponMapper couponMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get coupon by ID",
            description = "Retrieves a coupon by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Coupon found successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CouponResponse.class))),
            @ApiResponse(responseCode = "404",
                    description = "Coupon not found",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<CouponResponse> getCouponById(@PathVariable UUID id) {
        var couponFound = couponService.getCouponById(id);
        return ResponseEntity.ok(couponMapper.domainToResponse(couponFound));
    }

    @PostMapping
    @Operation(summary = "Create a new coupon",
            description = "Creates a new coupon with the provided details. " +
                    "Special characters in the code are automatically removed, " +
                    "and the code is truncated to 6 alphanumeric characters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Coupon created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CouponResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "Validation error or invalid coupon data",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "422",
                    description = "Business rule violation (e.g., discount below minimum or expiration date in the past)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<CouponResponse> createCoupon(@RequestBody @Valid CouponPostRequest request) {
        var coupon = couponMapper.postRequestToDomain(request);
        var createdCoupon = couponService.createCoupon(coupon);
        var response = couponMapper.domainToResponse(createdCoupon);
        return ResponseEntity.created(URI.create("/api/v1/coupons/" + response.id()))
                .body(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a coupon",
            description = "Deletes an existing coupon by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Coupon deleted successfully",
                    content = @Content()),
            @ApiResponse(responseCode = "404",
                    description = "Coupon not found",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Void> deleteCoupon(@PathVariable UUID id) {
        couponService.deleteCoupon(id);
        return ResponseEntity.noContent().build();
    }

}
