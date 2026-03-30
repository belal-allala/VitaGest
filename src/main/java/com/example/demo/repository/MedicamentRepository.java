package com.example.demo.repository;

import com.example.demo.entity.Medicament;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface MedicamentRepository extends JpaRepository<Medicament, Long> {
    @Query("SELECT m FROM Medicament m WHERE " +
           "LOWER(m.nom) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(m.dci) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(m.classe) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Medicament> search(@Param("query") String query);
}
