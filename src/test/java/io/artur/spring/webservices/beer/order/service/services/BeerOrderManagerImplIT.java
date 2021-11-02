package io.artur.spring.webservices.beer.order.service.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jenspiegsa.wiremockextension.WireMockExtension;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.artur.spring.webservices.beer.order.service.domain.BeerOrder;
import io.artur.spring.webservices.beer.order.service.domain.BeerOrderLine;
import io.artur.spring.webservices.beer.order.service.domain.BeerOrderStatusEnum;
import io.artur.spring.webservices.beer.order.service.domain.Customer;
import io.artur.spring.webservices.beer.order.service.repositories.BeerOrderRepository;
import io.artur.spring.webservices.beer.order.service.repositories.CustomerRepository;
import io.artur.spring.webservices.beer.order.service.services.beer.BeerServiceImpl;
import io.artur.spring.webservices.brewery.model.BeerDto;
import io.artur.spring.webservices.brewery.model.BeerOrderDto;
import io.artur.spring.webservices.brewery.model.BeerOrderPagedList;
import io.artur.spring.webservices.brewery.model.BeerPagedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.github.jenspiegsa.wiremockextension.ManagedWireMockServer.with;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 */
@ExtendWith(WireMockExtension.class)
@SpringBootTest
class BeerOrderManagerImplIT {

    @Autowired
    BeerOrderManager beerOrderManager;

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    WireMockServer wireMockServer;

    Customer testCustomer;

    UUID beerId = UUID.randomUUID();

    @TestConfiguration
    static class RestTemplateBuilderProvider {

        @Bean(destroyMethod = "stop")
        public WireMockServer wireMockServer() {

            WireMockServer wireMockServer = with(wireMockConfig().port(8084));
            wireMockServer.start();
            return wireMockServer;
        }
    }

    @BeforeEach
    public void setUp() {
        testCustomer = customerRepository.save(Customer.builder()
                        .customerName("MyTest CUSTOMER")
                .build());
    }

    private BeerOrder createBeerOrder() {
        BeerOrder beerOrder = BeerOrder.builder()
                .customer(testCustomer)
                .build();

        Set<BeerOrderLine> orderLines = new HashSet<>();
        orderLines.add(BeerOrderLine.builder()
                        .beerId(beerId)
                        .orderQuantity(1)
                        .upc("121222")
                        .beerOrder(beerOrder)
                        .build());

        beerOrder.setBeerOrderLines(orderLines);

        return beerOrder;
    }

    //@Transactional
    @Test
    void newOrderToAllocateTest() throws JsonProcessingException, InterruptedException {
        BeerDto beerDto = BeerDto.builder().id(beerId).upc("121222").build();

        wireMockServer.stubFor(get(BeerServiceImpl.BEER_UPC_PATH_V1 + "121222")
                .willReturn(okJson(objectMapper.writeValueAsString(beerDto))));
        BeerOrder beerOrder = createBeerOrder();

        BeerOrder savedBeerOrder = beerOrderManager.newBeerOrder(beerOrder);

        await().untilAsserted(() -> {
            BeerOrder foundOrder = beerOrderRepository.findById(beerOrder.getId()).get();
            assertEquals(BeerOrderStatusEnum.ALLOCATED, foundOrder.getOrderStatus());
        });

        savedBeerOrder = beerOrderRepository.findById(savedBeerOrder.getId()).get();

        assertNotNull(savedBeerOrder);
        assertEquals(BeerOrderStatusEnum.ALLOCATED, savedBeerOrder.getOrderStatus());

        await().untilAsserted(() -> {
            BeerOrder foundOrder = beerOrderRepository.findById(beerOrder.getId()).get();
            BeerOrderLine line = foundOrder.getBeerOrderLines().iterator().next();
            assertEquals(line.getOrderQuantity(), line.getQuantityAllocated());
        });

        
    }
}