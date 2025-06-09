package demo.spring.project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import demo.spring.project.models.SimpleCode;

@Repository
public interface SimpleCodeRepository extends JpaRepository<SimpleCode, Long> {
    @Query("SELECT stg FROM SimpleCode stg WHERE " +
           "stg.local_path = :local_path AND " +
           "stg.classname = :classname AND " +
           "stg.packages_str = :packages_str AND " +
           "stg.addresses_str = :addresses_str AND " +
           "stg.tablename = :tablename")
    List<SimpleCode> checkSimpleCodeDuplicate(
            @Param("local_path") String local_path, 
            @Param("classname") String classname, 
            @Param("packages_str") String packages_str, 
            @Param("addresses_str") String addresses_str, 
            @Param("tablename") String tablename
    );

    @Query("SELECT td FROM SimpleCode td ORDER BY td.id DESC LIMIT 10")
    List<SimpleCode> getLimitDesc();

    @Query("SELECT td FROM SimpleCode td WHERE td.classname LIKE %:classname% ORDER BY td.id DESC")
    List<SimpleCode> searchCodeByClassNameLike(@Param("classname") String classname);
}
