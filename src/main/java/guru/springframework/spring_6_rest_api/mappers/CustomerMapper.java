package guru.springframework.spring_6_rest_api.mappers;

import org.mapstruct.Mapper;

import guru.springframework.spring_6_rest_api.entities.Customer;
import guru.springframework.spring_6_rest_api.model.CustomerDTO;

// Note: the config as provided on course does not work, this is how you got it to work:
// https://www.youtube.com/watch?app=desktop&v=7UC3ZjQnric
@Mapper(componentModel = "spring")
public interface CustomerMapper {
    
    Customer customerDtoToCustomer(CustomerDTO dto);
    CustomerDTO customerToCustomerDto(Customer customer);
}
