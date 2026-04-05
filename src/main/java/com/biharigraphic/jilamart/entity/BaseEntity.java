package com.biharigraphic.jilamart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

/***
 * DOC:
 * - NOTE: @MappedSuperClass means we donot need to create a table in the db for this class
 * - any table can inherit and automatically the fields come with in that table class or entity
 * **/

@MappedSuperclass
@Getter
@Setter
@EntityListeners(EntityListener.class)
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;  // stored in UTC

    @UpdateTimestamp
    private Instant updatedAt;  // stored in UTC

    private Instant deletedAt;  // stored in UTC

    @Version
    private Long version;

    public boolean isDeleted() {
        return deletedAt != null;
    }
}
