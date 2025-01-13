package guru.springframework.spring_6_rest_api.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import guru.springframework.spring_6_rest_api.entities.Customer;
import guru.springframework.spring_6_rest_api.mappers.CustomerMapper;
import guru.springframework.spring_6_rest_api.model.CustomerDTO;
import guru.springframework.spring_6_rest_api.repositories.CustomerRepository;
import jakarta.transaction.Transactional;

@SpringBootTest
public class CustomerControllerIT {
    @Autowired
    CustomerController customerController;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerMapper customerMapper;

    @Test
    void testDeleteByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            customerController.deleteById(UUID.randomUUID());
        });
    }

    @Test
    @Transactional
    @Rollback
    void testDeleteById() {
        Customer customer = customerRepository.findAll().get(0);
        ResponseEntity responseEntity = customerController.deleteById(customer.getId());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        assertThat(customerRepository.findById(customer.getId())).isEmpty();
    }

    @Test
    void patchExistingCustomeNotFound() {
        assertThrows(NotFoundException.class,
            () -> customerController.patchById(UUID.randomUUID(), CustomerDTO.builder().build()));
    }

    @Test
    @Transactional
    @Rollback
    void patchExistingCustomer() {
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO customerDto = customerMapper.customerToCustomerDto(customer);
        
        customerDto.setId(null);
        customerDto.setVersion(null);

        final String customerName = "Larry Updated";
        customerDto.setName(customerName);

        ResponseEntity responseEntity = customerController.patchById(customer.getId(), customerDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Customer updatedCustomer = customerRepository.findById(customer.getId()).get();
        assertThat(updatedCustomer.getName()).isEqualTo(customerName);
    }

    @Test
    void updateExistingCustomeNotFound() {
        assertThrows(NotFoundException.class,
            () -> customerController.updateById(UUID.randomUUID(), CustomerDTO.builder().build()));
    }

    @Test
    @Transactional
    @Rollback
    void updateExistingCustomer() {
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO customerDto = customerMapper.customerToCustomerDto(customer);
        
        customerDto.setId(null);
        customerDto.setVersion(null);

        final String customerName = "Larry Updated";
        customerDto.setName(customerName);

        ResponseEntity responseEntity = customerController.updateById(customer.getId(), customerDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Customer updatedCustomer = customerRepository.findById(customer.getId()).get();
        assertThat(updatedCustomer.getName()).isEqualTo(customerName);

    }

    @Test
    @Transactional
    @Rollback
    void saveNewCustomerTest() {
        CustomerDTO customerDto = CustomerDTO.builder()
            .name("Jill Harpie")
            .build();

        ResponseEntity responseEntity = customerController.saveNew(customerDto);

        // Assert that the response is of the correct status and have a Location header set
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        // Ensure that the Location header will navigate to an existing Beer entity
        String[] urlSegments = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUuid = UUID.fromString(urlSegments[4]);

        Customer customer = customerRepository.findById(savedUuid).get();
        assertThat(customer).isNotNull();
    }

    @Test
    @Transactional
    @Rollback
    void testGetByCustomerIdNotFound() {
        customerRepository.deleteAll();
        assertThrows(NotFoundException.class, () -> customerController.getCustomerById(UUID.randomUUID()));
    }

    @Test
    void testGetByCustomerId() {
        Customer customer = customerRepository.findAll().get(1);
        CustomerDTO dto = customerController.getCustomerById(customer.getId());

        assertThat(dto).isNotNull();
    }

    @Test
    @Transactional
    @Rollback
    void testListCustomerEmptyList() {
        customerRepository.deleteAll();
        List<CustomerDTO> customers = customerController.listCustomers();
    
        assertThat(customers.size()).isEqualTo(0);
    } 

    @Test
    void testListCustomers() {
        List<CustomerDTO> customers = customerController.listCustomers();

        assertThat(customers.size()).isEqualTo(3);
    }
}
