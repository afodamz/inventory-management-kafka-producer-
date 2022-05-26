package com.mintyn.inventorymanagement.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mintyn.inventorymanagement.audit.DateAudit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "products")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Products extends DateAudit {

    private static final long serialVersionUID = 5504624538233192642L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", length = 36, columnDefinition = "nvarchar", insertable = false, updatable = false, nullable = false)
    private String id;

    @Column(name = "name", length = 256, nullable = false)
    private String name;

    @Column(name = "description", length = 1024)
    private String description;

    @Column(name = "total_in_stock", length = 1024)
    private Double totalInStock;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "is_deleted")
    private Boolean isDeleted = Boolean.FALSE;

//    @JsonIgnore
//    @Version
//    private long version;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Products product = (Products) o;
        return id.equals(product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
