package io.artur.spring.webservices.beer.order.service.services.listeners;

import io.artur.spring.webservices.beer.order.service.config.JmsConfig;
import io.artur.spring.webservices.beer.order.service.services.BeerOrderManager;
import io.artur.spring.webservices.brewery.model.events.ValidateOrderResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 *
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class ValidationResultListener {
    private final BeerOrderManager beerOrderManager;

    @JmsListener(destination = JmsConfig.VALIDATE_ORDER_RESPONSE_QUEUE)
    public void listen(ValidateOrderResult result){

        final UUID beerOrderId = result.getOrderId();
        System.out.println("4) orderId from Queue VALIDATE_ORDER_RESPONSE_QUEUE id: " + beerOrderId);
        beerOrderManager.processValidationResult(beerOrderId, result.getIsValid());
    }
}
