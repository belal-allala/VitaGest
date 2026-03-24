package com.example.demo.repository;

import com.example.demo.entity.Lot;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LotRepository extends JpaRepository<Lot, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT l FROM Lot l WHERE l.medicament.id = :medicamentId AND l.quantite > 0 ORDER BY l.expiration ASC")
    List<Lot> findLotsForDeduction(@Param("medicamentId") Long medicamentId);
}
