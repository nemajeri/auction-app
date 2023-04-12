package com.atlantbh.auctionappbackend.repository;

import com.atlantbh.auctionappbackend.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> getByEmail(String email);

    boolean existsByEmail(String email);
}
