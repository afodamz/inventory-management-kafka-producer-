package com.mintyn.inventorymanagement.models;

import com.mintyn.inventorymanagement.audit.DateAudit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table (name = "orders")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order extends DateAudit {

    private static final long serialVersionUID = -8832862623919132232L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", length = 36, columnDefinition = "nvarchar", insertable = false, updatable = false, nullable = false)
    private String id;

    @Column (name = "name", nullable = false)
    private String name;

    @Column (name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "is_deleted")
    private Boolean isDeleted = Boolean.FALSE;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> items;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        return id.equals(order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Double getTotalPrice() {
        return items.stream().mapToDouble(item -> item.getUnitPrice() * item.getQuantity()).sum();
    }
}
