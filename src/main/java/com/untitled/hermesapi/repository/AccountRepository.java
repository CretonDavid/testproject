package com.untitled.hermesapi.repository;

import com.untitled.hermesapi.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
