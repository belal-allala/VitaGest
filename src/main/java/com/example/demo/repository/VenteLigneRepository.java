package com.example.demo.repository;

import com.example.demo.entity.VenteLigne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VenteLigneRepository extends JpaRepository<VenteLigne, Long> {
}
