package project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.entities.SectionEntity;
import project.entities.UserEntity;

import java.util.Optional;

@Repository
public interface SectionRepository extends JpaRepository<SectionEntity, Integer> {
    Optional<SectionEntity> findByName(String name);
    @Query("select s from SectionEntity s where s.name=:name")
    SectionEntity getByName(String name);
}
