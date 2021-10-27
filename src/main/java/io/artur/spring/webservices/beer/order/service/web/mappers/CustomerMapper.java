package io.artur.spring.webservices.beer.order.service.web.mappers;

import io.artur.spring.webservices.beer.order.service.domain.Customer;
import io.artur.spring.webservices.brewery.model.CustomerDto;
import org.mapstruct.Mapper;

/**
 *
 */
@Mapper(uses = {DateMapper.class})
public interface CustomerMapper {
    CustomerDto customerToDto(Customer customer);

    Customer dtoToCustomer(Customer dto);
}
