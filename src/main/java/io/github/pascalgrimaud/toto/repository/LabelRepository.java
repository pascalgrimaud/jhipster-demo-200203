package io.github.pascalgrimaud.toto.repository;

import io.github.pascalgrimaud.toto.domain.Label;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Label entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LabelRepository extends JpaRepository<Label, Long> {

}
