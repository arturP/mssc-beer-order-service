package io.artur.spring.webservices.beer.order.service.services.listeners;

import io.artur.spring.webservices.beer.order.service.config.JmsConfig;
import io.artur.spring.webservices.beer.order.service.services.BeerOrderManager;
import io.artur.spring.webservices.brewery.model.events.AllocateOrderResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 *
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class BeerOrderAllocationResultListener {

    private final BeerOrderManager beerOrderManager;

    @JmsListener(destination = JmsConfig.ALLOCATE_ORDER_RESPONSE_QUEUE)
    public void listen(AllocateOrderResult result){
        System.out.println("&&&&&&&&&&&&&&&&&&&&& Allocate order response received %%%%%%%%%%%%%%%%%%%");
        if(!result.getAllocationError() && !result.getPendingInventory()){
            //allocated normally
            System.out.println("allocated normally");
            beerOrderManager.beerOrderAllocationPassed(result.getBeerOrderDto());
        } else if(!result.getAllocationError() && result.getPendingInventory()) {
            //pending inventory
            System.out.println("pending inventory");
            beerOrderManager.beerOrderAllocationPendingInventory(result.getBeerOrderDto());
        } else if(result.getAllocationError()){
            //allocation error
            System.out.println("allocation error");
            beerOrderManager.beerOrderAllocationFailed(result.getBeerOrderDto());
        }
    }

}
