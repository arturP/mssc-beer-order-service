package io.artur.spring.webservices.beer.order.service.services.beer;

import io.artur.spring.webservices.brewery.model.BeerDto;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 *
 */
@ConfigurationProperties(prefix = "io.artur.brewery", ignoreUnknownFields = false)
@Service
public class BeerServiceImpl implements BeerService {
    @Override
    public Optional<BeerDto> getBeerById(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public Optional<BeerDto> getBeerByUpc(String upc) {
        return Optional.empty();
    }
}
