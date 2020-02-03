package io.github.pascalgrimaud.toto.repository;

import io.github.pascalgrimaud.toto.domain.Alpha;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Alpha entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlphaRepository extends JpaRepository<Alpha, Long>, JpaSpecificationExecutor<Alpha> {

}
