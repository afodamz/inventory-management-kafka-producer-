package com.mintyn.inventorymanagement.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mintyn.inventorymanagement.audit.DateAudit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "order_item")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem extends DateAudit {

    private static final long serialVersionUID = 591505171819818841L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", length = 36, columnDefinition = "nvarchar", insertable = false, updatable = false, nullable = false)
    private String id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn (name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn (name = "product_id")
    private Products product;

    @Column (name = "quantity", nullable = false)
    private Integer quantity;

    @Column (name = "unit_price", nullable = false)
    private Double unitPrice;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderItem orderItem = (OrderItem) o;
        return order.equals(orderItem.order) &&
                product.equals(orderItem.product) &&
                quantity.equals(orderItem.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, product, quantity);
    }
}
