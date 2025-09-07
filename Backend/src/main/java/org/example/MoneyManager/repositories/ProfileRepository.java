package org.example.MoneyManager.repositories;



import org.example.MoneyManager.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {

    Optional<ProfileEntity> findByEmail(String email);

    Optional<ProfileEntity> findByActivationToken(String token);
}