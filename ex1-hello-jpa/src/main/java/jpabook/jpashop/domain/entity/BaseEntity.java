package jpabook.jpashop.domain.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.math.BigInteger;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseEntity {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Id @GeneratedValue
    private Long id;

}
