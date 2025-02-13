package guru.springframework.spring_6_rest_api.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import guru.springframework.spring_6_rest_api.model.BeerStyle;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder

// Using @Data for an entity is not recommended.
// Generate getters and setters only.
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Beer {
    @Id
    @GeneratedValue(generator = "UUID")
    // Necessary to create a "sequential guid" which is more performant
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.CHAR)
    // "updatable" is false. This value is immutable.
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;

    @NotBlank
    @NotNull
    @Size(max = 50)
    @Column(length = 50)
    private String beerName;
    
    // Used as part of Hibernate's locking strategy. Every time this
    // entity is updated, this version property will be incremented by 1
    // Hibernate will check the version in the database, if they're different
    // an exception will be thrown as your entity is stale.
    @Version
    private Integer version;

    @NotNull
    @JdbcTypeCode(SqlTypes.SMALLINT)
    private BeerStyle beerStyle;

    @NotBlank
    @NotNull
    // The default is 255 but best practice to throw a validation error
    // rather than a database integrity error.
    @Size(max = 255)
    private String upc;
    private Integer quantityOnHand;

    @NotNull
    private BigDecimal price;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;

    // Have a look at the BeerOrderLine to see how this is "mapped"
    @Builder.Default
    @OneToMany(mappedBy = "beer")
    private Set<BeerOrderLine> beerOrderLines = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "beer_category", 
        joinColumns = @JoinColumn(name = "beer_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    public void addCategory(Category category) {
        this.categories.add(category);
        category.getBeers().add(this);
    }

    public void removeCategory(Category category) {
        this.categories.remove(category);
        category.getBeers().remove(this);
    }
}
