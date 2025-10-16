package com.example.rbac.repository;

import com.example.rbac.model.UiComponent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UiComponentRepository extends JpaRepository<UiComponent, Long> {
    List<UiComponent> findByPageId(Long pageId);
}
