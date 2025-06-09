package demo.spring.project.repositories;
import demo.spring.project.models.CountryCode;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryCodeRepository extends JpaRepository<CountryCode, Long> {

}