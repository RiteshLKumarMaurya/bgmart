package com.biharigraphic.jilamart.user.repository;

import com.biharigraphic.jilamart.user.entity.DeleteAccountRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeleteAccountRequestRepository extends JpaRepository<DeleteAccountRequest,Long> {

//    public boolean existsByUsernameAndPassword(String username,String password);
    public boolean existsByPhone(String phone);

}
