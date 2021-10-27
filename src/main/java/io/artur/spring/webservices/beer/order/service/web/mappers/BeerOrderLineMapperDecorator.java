package io.artur.spring.webservices.beer.order.service.web.mappers;

import io.artur.spring.webservices.beer.order.service.services.beer.BeerService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
public abstract class BeerOrderLineMapperDecorator implements BeerOrderLineMapper{
    private BeerService beerService;
    private BeerOrderLineMapper beerOrderLineMapper;

    @Autowired
    public void setBeerService(BeerService beerService) {
        this.beerService = beerService;
    }

    @Autowired
    @Qualifier("delegate")
    public void setBeerOrderLineMapper(BeerOrderLineMapper beerOrderLineMapper) {
        this.beerOrderLineMapper = beerOrderLineMapper;
    }
}
