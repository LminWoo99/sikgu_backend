package com.example.plantpayservice.service;

import com.example.plantpayservice.domain.entity.Payment;
import com.example.plantpayservice.exception.CustomException;
import com.example.plantpayservice.exception.ErrorCode;
import com.example.plantpayservice.repository.PaymentRepository;
import com.example.plantpayservice.vo.request.PaymentRequestDto;
import com.example.plantpayservice.vo.response.PaymentResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentRepository paymentRepository;

    /**
     * 식구페이 머니 충전 메서드
     * iamport로 결제 완료 되면 페이 머니로 충전
     * @param : PaymentRequestDto paymentRequestDto
     */
    @Transactional
    public void chargePayMoney(PaymentRequestDto paymentRequestDto) {
        if (!paymentRepository.existsByMemberNo(paymentRequestDto.getMemberNo())) {
            Payment payment=Payment.builder()
                    .payMoney(paymentRequestDto.getPayMoney())
                    .memberNo(paymentRequestDto.getMemberNo())
                    .build();

            paymentRepository.save(payment);
        }
        else{
            paymentRepository.existsByMemberNoUpdatePayMoney(paymentRequestDto);

        }

    }
    /**
     * 식구페이 머니 환불 메서드
     * 원하는 금액 환불후 계좌 송금(실제로 계좌로 이체되진 않음)
     * 환불할 금액이 없을 경우 예외 처리
     * 사용자가 모르고 환불요청을 두번 연속 했을 경우를 대비해 페이머니가 음수가 될수있음에도 paymentRepository.updatePayMoney(paymentRequestDto);
     * 2번다 호출될수있으므로
     * synchronized를 통해 동시성 제어!
     * @param : UpdatePaymentRequestDto paymentRequestDto
     */
    @Transactional
    public synchronized void refundPayMoney(PaymentRequestDto paymentRequestDto) {
        // memberNo로 보유 페이머니 조회
        Payment payment = paymentRepository.findByMemberNo(paymentRequestDto.getMemberNo());

        //보유 페이 머니보다 입력한 환불할 금액이 많으면 예외 처리
        if (payment.getPayMoney()- paymentRequestDto.getPayMoney() <= 0) {
            throw new CustomException(ErrorCode.PAYMONEY_NOT_FOUND);
        }
        paymentRepository.updatePayMoney(paymentRequestDto);
    }
    /**
     * 식구페이 머니 조회 메서드
     * 조회용 메서드라 @Transactional(readOnly = true) 처리
     * @param : Integer memberNo
     */
    @Transactional(readOnly = true)
    public PaymentResponseDto getPayMoney(Integer memberNo) {
        Payment payment = paymentRepository.findByMemberNo(memberNo);
        PaymentResponseDto paymentResponseDto = PaymentResponseDto.builder()
                .payMoney(payment.getPayMoney())
                .memberNo(payment.getMemberNo())
                .build();
        return paymentResponseDto;

    }
    /**
     *
     * 식구페이 거래 메서드
     * 판매자 상대 멤버 번호를 통해 해당 조회 후
     * 판매자 paymoney += 거래할 금액
     * 구매자 Paymoney -= 거래할 금액
     * @param : PaymentRequestDto paymentRequestDto, Integer sellerNo
     */
    @Transactional
    public void tradePayMoney(PaymentRequestDto paymentRequestDto, Integer sellerNo) {
        Payment sellerPayment = paymentRepository.findByMemberNo(sellerNo);
        Payment buyerPayment = paymentRepository.findByMemberNo(paymentRequestDto.getMemberNo());
        //거레할 금액보다 구매자 보유 payMoney가 적으면 예외 처리
        if (buyerPayment.getPayMoney()< paymentRequestDto.getPayMoney()) {
            throw new CustomException(ErrorCode.PAYMONEY_NOT_FOUND);
        }
        paymentRepository.tradePayMoney(sellerPayment.getMemberNo(), buyerPayment.getMemberNo(), paymentRequestDto);
    }
}
