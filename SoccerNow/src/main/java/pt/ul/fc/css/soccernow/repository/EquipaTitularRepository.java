package pt.ul.fc.css.soccernow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pt.ul.fc.css.soccernow.model.EquipaTitular;

public interface EquipaTitularRepository extends JpaRepository<EquipaTitular, Long> {

    List<EquipaTitular> findByEquipaId(Long equipaId);
} 
