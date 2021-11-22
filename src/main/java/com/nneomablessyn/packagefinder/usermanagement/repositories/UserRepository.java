package com.nneomablessyn.packagefinder.usermanagement.repositories;

import com.nneomablessyn.packagefinder.usermanagement.entities.PFUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<PFUser, Long>{
    Optional<PFUser> findByEmailIgnoreCase(String email);
}
