package com.mintyn.inventorymanagement.audit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@Data
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
        value = { "createdAt", "updatedAt" },
        allowGetters = true
)
public abstract class DateAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    @CreationTimestamp
    @Column (nullable = false, updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column (nullable = false, updatable = false, name = "updated_at")
    private Date updatedAt;

}