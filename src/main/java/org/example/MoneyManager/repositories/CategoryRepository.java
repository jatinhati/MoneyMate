package org.example.MoneyManager.repositories;

import org.example.MoneyManager.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {


    List<CategoryEntity> findByProfileId(Long profileId);

    List<CategoryEntity> findByIdAndProfileId(long id, Long profileId);

    Boolean existsByNameAndProfileId(String name, Long profileId);

    List<CategoryEntity> findByTypeAndProfileId(String type, Long profileId);
}

