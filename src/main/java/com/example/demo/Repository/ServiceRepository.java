package com.example.demo.Repository;

import com.example.demo.Model.Category;
import com.example.demo.Model.ServiceEntity;
import com.example.demo.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

    // All services by a provider
    List<ServiceEntity> findByProvider(User provider);

    // All available services
    List<ServiceEntity> findByIsAvailableTrue();

    // Filter by category
    List<ServiceEntity> findByCategoryAndIsAvailableTrue(Category category);

    // Search by title keyword
    List<ServiceEntity> searchByTitle(String keyword);
}
