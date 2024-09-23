package com.example.plantpayservice.repository;

import com.example.plantpayservice.dto.request.PaymentRequestDto;

public interface CustomPaymentRepository {

    void tradePayMoney(Integer sellerNo, Integer buyerNo, PaymentRequestDto paymentRequestDto, Integer buyerPayMoney);

    void existsByMemberNoUpdatePayMoney(PaymentRequestDto paymentRequestDto);
}
