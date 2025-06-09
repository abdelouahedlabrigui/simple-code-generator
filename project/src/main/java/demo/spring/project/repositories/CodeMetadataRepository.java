package demo.spring.project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import demo.spring.project.models.CodeMetadata;

@Repository
public interface CodeMetadataRepository extends JpaRepository<CodeMetadata, Long> {

    @Query("SELECT td FROM CodeMetadata td ORDER BY td.id DESC LIMIT 10")
    List<CodeMetadata> getLimitDesc();

    @Query("SELECT td FROM CodeMetadata td WHERE td.entity_name LIKE %:entity_name% ORDER BY td.id DESC")
    List<CodeMetadata> searchCodeMetadataByEntityNameLike(@Param("entity_name") String entity_name);
}
