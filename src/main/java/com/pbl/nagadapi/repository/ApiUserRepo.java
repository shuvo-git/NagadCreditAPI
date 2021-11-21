package com.pbl.nagadapi.repository;

import com.pbl.nagadapi.entity.ApiUsers;
import com.pbl.nagadapi.entity.ResponseTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiUserRepo extends JpaRepository<ApiUsers,Long>
{
    public ApiUsers findByUsername(String username);
}
