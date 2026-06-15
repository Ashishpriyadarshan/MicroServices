package com.micro.accounts.repository;


import com.micro.accounts.entity.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface AccountsRepository extends JpaRepository<Accounts,Long> {

    Optional<Accounts> findByCustomerId(Long customerId);

    Optional<Accounts> findByAccountNumber(Long accountNumber);

    //Any custom functions which are going to cause the data in the DB to get changed or deleted need two important annotations
    // @Transactional @Modifying
    //  @Modifying tells the framework that this function is going to do data modifications in the DB
    // @Transactional tells the framework that during any kind of modifying operation if anything happens or goes wrong or any partial data change has happend
    //Then it will roll back the entire operation , It means either all will happen or not a single thing will happen.
    // This transactional and modifying is not written on top of other JPA provided functions because JPA will take care of its own provided functions easily
    @Transactional
    @Modifying
    void deleteByCustomerId(Long customerId);
}
