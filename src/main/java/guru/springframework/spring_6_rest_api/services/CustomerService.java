package guru.springframework.spring_6_rest_api.services;

import guru.springframework.spring_6_rest_api.model.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    List<Customer> listCustomers();
    Customer getCustomerById(UUID id);
    Customer saveNewCustomer(Customer customer);
    void updateCustomerById(UUID customerId, Customer customer);
    void deleteById(UUID customerId);
    void patchById(UUID customerId, Customer customer);
}
