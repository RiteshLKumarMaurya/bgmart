package com.biharigraphic.jilamart.entity;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.Instant;

/*** DOCS:
 *
 * - these method get called automatically wherever used it to perform the set operation needed for
 * - jb hm apne se bhi save nahi krenge createdAt and updatedAt to ye autometically save kr dega
 * */
public class EntityListener {

    @PrePersist
    public void prePersist(BaseEntity entity) {
        if (entity.getCreatedAt() == null) {
            entity.setCreatedAt(Instant.now()); // UTC
        }
    }

    @PreUpdate
    public void preUpdate(BaseEntity entity) {
        entity.setUpdatedAt(Instant.now()); // UTC
    }
}
