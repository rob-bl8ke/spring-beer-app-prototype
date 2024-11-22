package guru.springframework.spring_6_rest_api.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import guru.springframework.spring_6_rest_api.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    
}
