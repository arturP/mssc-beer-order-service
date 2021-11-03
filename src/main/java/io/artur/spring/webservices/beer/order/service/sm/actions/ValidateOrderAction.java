package io.artur.spring.webservices.beer.order.service.sm.actions;

import io.artur.spring.webservices.beer.order.service.config.JmsConfig;
import io.artur.spring.webservices.beer.order.service.domain.BeerOrder;
import io.artur.spring.webservices.beer.order.service.domain.BeerOrderEventEnum;
import io.artur.spring.webservices.beer.order.service.domain.BeerOrderStatusEnum;
import io.artur.spring.webservices.beer.order.service.repositories.BeerOrderRepository;
import io.artur.spring.webservices.beer.order.service.services.BeerOrderManagerImpl;
import io.artur.spring.webservices.beer.order.service.web.mappers.BeerOrderMapper;
import io.artur.spring.webservices.brewery.model.events.ValidateOrderRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 *
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ValidateOrderAction implements Action<BeerOrderStatusEnum, BeerOrderEventEnum> {

    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderMapper beerOrderMapper;
    private final JmsTemplate jmsTemplate;

    @Override
    public void execute(StateContext<BeerOrderStatusEnum, BeerOrderEventEnum> context) {
        String beerOrderId = (String) context.getMessage().getHeaders().get(BeerOrderManagerImpl.ORDER_ID_HEADER);
        Optional<BeerOrder> beerOrderOptional = beerOrderRepository.findById(UUID.fromString(beerOrderId));

        System.out.println("2) order retrieved from repository in validateOrderAction - id: " + beerOrderOptional.get().getId());
        System.out.println("2a) beerOrderId is " + beerOrderId);
        System.out.println("2b) Repo count = " + beerOrderRepository.findAll().size());
        beerOrderOptional.ifPresentOrElse(beerOrder -> {
            jmsTemplate.convertAndSend(JmsConfig.VALIDATE_ORDER_QUEUE, ValidateOrderRequest.builder()
                    .beerOrder(beerOrderMapper.beerOrderToDto(beerOrder))
                    .build());
        }, () -> log.error("Order Not Found. Id: " + beerOrderId));

        log.debug("Sent Validation request to queue for order id " + beerOrderId);
    }
}
