package com.pbl.nagadapi.repository;

import com.pbl.nagadapi.entity.ResponseAccCheck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResponseRepository extends JpaRepository<ResponseAccCheck,Long> {
}
