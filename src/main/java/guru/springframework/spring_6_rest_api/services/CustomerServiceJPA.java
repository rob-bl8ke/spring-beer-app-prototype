package guru.springframework.spring_6_rest_api.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import guru.springframework.spring_6_rest_api.entities.Customer;
import guru.springframework.spring_6_rest_api.mappers.CustomerMapper;
import guru.springframework.spring_6_rest_api.model.CustomerDTO;
import guru.springframework.spring_6_rest_api.repositories.CustomerRepository;
import lombok.AllArgsConstructor;

@Service
@Primary
@AllArgsConstructor
public class CustomerServiceJPA implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public List<CustomerDTO> listCustomers() {
        return customerRepository.findAll().stream()
            .map(customerMapper::customerToCustomerDto)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        return Optional.ofNullable(customerMapper.customerToCustomerDto(
           customerRepository.findById(id).orElse(null) 
        ));
    }

    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO customer) {
        Customer savedCustomer = customerRepository.save(customerMapper.customerDtoToCustomer(customer));
        return customerMapper.customerToCustomerDto(savedCustomer);
    }

    @Override
    public Optional<CustomerDTO> updateCustomerById(UUID customerId, CustomerDTO customer) {
        AtomicReference<Optional<CustomerDTO>> atomicReference = new AtomicReference<>();

        customerRepository.findById(customerId).ifPresentOrElse(foundCustomer -> {
            foundCustomer.setName(customer.getName());

            atomicReference.set(Optional.of(
                customerMapper.customerToCustomerDto(foundCustomer)
            ));

            customerRepository.save(foundCustomer);
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }

    @Override
    public Boolean deleteById(UUID customerId) {
        if (customerRepository.existsById(customerId)) {
            customerRepository.deleteById(customerId);

            return true;
        }

        return false;
    }

    @Override
    public Optional<CustomerDTO> patchById(UUID customerId, CustomerDTO customer) {
        AtomicReference<Optional<CustomerDTO>> atomicReference = new AtomicReference<>();

        customerRepository.findById(customerId).ifPresentOrElse(foundCustomer -> {
            if (StringUtils.hasText(customer.getName())) {
                foundCustomer.setName(customer.getName());
            }

            atomicReference.set(Optional.of(
                customerMapper.customerToCustomerDto(foundCustomer)
            ));

            customerRepository.save(foundCustomer);
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }
    
}
