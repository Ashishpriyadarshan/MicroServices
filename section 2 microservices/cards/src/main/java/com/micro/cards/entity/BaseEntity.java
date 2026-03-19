package com.micro.cards.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter@Setter@MappedSuperclass
@ToString
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @Column(name = "created_at",updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "created_by",updatable = false)
    @CreatedBy
    private String createdBy;

    @Column(name = "updated_at",insertable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @LastModifiedBy
    @Column(name = "updated_by",insertable = false)
     private String updatedBy;
}
