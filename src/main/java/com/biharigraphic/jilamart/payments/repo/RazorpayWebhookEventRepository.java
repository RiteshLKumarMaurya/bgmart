package com.biharigraphic.jilamart.payments.repo;

import com.biharigraphic.jilamart.payments.model.entities.WebhookEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RazorpayWebhookEventRepository extends JpaRepository<WebhookEvent,Long> {

}
