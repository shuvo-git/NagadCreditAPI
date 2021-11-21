package com.pbl.nagadapi.repository;

import com.pbl.nagadapi.entity.ResponseTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionResponseRepo extends JpaRepository<ResponseTransaction,Long> {
    public ResponseTransaction findDistinctByRefId(String refId);
}
