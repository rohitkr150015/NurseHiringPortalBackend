package com.nuhi.Nuhi.repository;

import com.nuhi.Nuhi.model.AdminAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminActionRepository  extends JpaRepository<AdminAction, Long> {
}
