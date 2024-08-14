package com.example.plantgatewayservice.fallback;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.cloud.gateway.support.TimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;

@RestController
@Slf4j
public class FallbackController {

    @RequestMapping("/fallback")
    public Mono<ResponseEntity<String>> fallback(ServerWebExchange exchange) {
        Throwable exception = exchange.getAttribute(ServerWebExchangeUtils.CIRCUITBREAKER_EXECUTION_EXCEPTION_ATTR);
        log.info("예외: {}", exception != null ? exception.getMessage() : "Unknown error");

        return Mono.just(exception)
                .map(ex -> {
                    if (ex instanceof NotFoundException) {
                        return new ResponseEntity<>("내부 서버 오류가 발생했습니다. 관리자에게 문의해 주세요.", HttpStatus.SERVICE_UNAVAILABLE);
                    } else if (ex instanceof TimeoutException) {
                        return new ResponseEntity<>("서비스 요청 시간이 초과되었습니다. 나중에 다시 시도해 주세요.", HttpStatus.GATEWAY_TIMEOUT);
                    } else if (ex instanceof CallNotPermittedException) {
                        return new ResponseEntity<>("서비스가 현재 사용 불가능합니다. 잠시 후 다시 시도해 주세요.", HttpStatus.SERVICE_UNAVAILABLE);
                    } else {
                        return new ResponseEntity<>("내부 서버 오류가 발생했습니다. 관리자에게 문의해 주세요.", HttpStatus.SERVICE_UNAVAILABLE);
                    }
                })
                .defaultIfEmpty(new ResponseEntity<>("내부 서버 오류가 발생했습니다. 관리자에게 문의해 주세요.", HttpStatus.SERVICE_UNAVAILABLE));
    }
}

