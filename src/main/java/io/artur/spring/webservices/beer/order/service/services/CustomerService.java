package io.artur.spring.webservices.beer.order.service.services;

import io.artur.spring.webservices.brewery.model.CustomerPagedList;
import org.springframework.data.domain.Pageable;

/**
 *
 */
public interface CustomerService {
    CustomerPagedList listCustomers(Pageable pageable);
}
