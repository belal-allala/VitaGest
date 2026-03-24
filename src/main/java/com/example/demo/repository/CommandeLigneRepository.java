package com.example.demo.repository;

import com.example.demo.entity.CommandeLigne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandeLigneRepository extends JpaRepository<CommandeLigne, Long> {
}
