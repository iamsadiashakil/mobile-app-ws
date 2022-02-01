package com.appsdeveloperblog.app.ws.mobileappws.repo;

import com.appsdeveloperblog.app.ws.mobileappws.entity.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);

    boolean existsByEmail(String email);

    UserEntity findByUserId(String userId);
}
