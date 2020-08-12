package com.example.Practice_Project.OnBoarding;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, Integer> {
    public List<Transaction> findByUserMobileNo(String mobileNo);
}
