package io.github.pascalgrimaud.toto.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import io.github.pascalgrimaud.toto.domain.Alpha;
import io.github.pascalgrimaud.toto.domain.*; // for static metamodels
import io.github.pascalgrimaud.toto.repository.AlphaRepository;
import io.github.pascalgrimaud.toto.service.dto.AlphaCriteria;
import io.github.pascalgrimaud.toto.service.dto.AlphaDTO;
import io.github.pascalgrimaud.toto.service.mapper.AlphaMapper;

/**
 * Service for executing complex queries for {@link Alpha} entities in the database.
 * The main input is a {@link AlphaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AlphaDTO} or a {@link Page} of {@link AlphaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlphaQueryService extends QueryService<Alpha> {

    private final Logger log = LoggerFactory.getLogger(AlphaQueryService.class);

    private final AlphaRepository alphaRepository;

    private final AlphaMapper alphaMapper;

    public AlphaQueryService(AlphaRepository alphaRepository, AlphaMapper alphaMapper) {
        this.alphaRepository = alphaRepository;
        this.alphaMapper = alphaMapper;
    }

    /**
     * Return a {@link List} of {@link AlphaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AlphaDTO> findByCriteria(AlphaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Alpha> specification = createSpecification(criteria);
        return alphaMapper.toDto(alphaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AlphaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlphaDTO> findByCriteria(AlphaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Alpha> specification = createSpecification(criteria);
        return alphaRepository.findAll(specification, page)
            .map(alphaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlphaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Alpha> specification = createSpecification(criteria);
        return alphaRepository.count(specification);
    }

    /**
     * Function to convert {@link AlphaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Alpha> createSpecification(AlphaCriteria criteria) {
        Specification<Alpha> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Alpha_.id));
            }
            if (criteria.getFirstname() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstname(), Alpha_.firstname));
            }
            if (criteria.getLastname() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastname(), Alpha_.lastname));
            }
            if (criteria.getBirthday() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBirthday(), Alpha_.birthday));
            }
        }
        return specification;
    }
}
