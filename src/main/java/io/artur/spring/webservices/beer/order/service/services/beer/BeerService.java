package io.artur.spring.webservices.beer.order.service.services.beer;

import io.artur.spring.webservices.brewery.model.BeerDto;

import java.util.Optional;
import java.util.UUID;

/**
 *
 */
public interface BeerService {

    Optional<BeerDto> getBeerById(UUID uuid);

    Optional<BeerDto> getBeerByUpc(String upc);
}
