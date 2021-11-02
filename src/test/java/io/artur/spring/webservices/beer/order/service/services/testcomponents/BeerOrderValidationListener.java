package io.artur.spring.webservices.beer.order.service.services.testcomponents;

import io.artur.spring.webservices.beer.order.service.config.JmsConfig;
import io.artur.spring.webservices.brewery.model.events.ValidateOrderRequest;
import io.artur.spring.webservices.brewery.model.events.ValidateOrderResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;


/**
 *
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class BeerOrderValidationListener {

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.VALIDATE_ORDER_QUEUE)
    public void listen(Message message) {

        ValidateOrderRequest request = (ValidateOrderRequest)message.getPayload();

        jmsTemplate.convertAndSend(JmsConfig.VALIDATE_ORDER_RESPONSE_QUEUE,
                ValidateOrderResult.builder()
                        .isValid(true)
                        .orderId(request.getBeerOrder().getId())
                        .build());
    }
}
