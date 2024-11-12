package guru.springframework.spring_6_rest_api.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import guru.springframework.spring_6_rest_api.entities.Customer;

// Spring data will provide implementations for thhis interface for use at runtime.
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    
}
