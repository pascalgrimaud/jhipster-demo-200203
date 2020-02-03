package io.github.pascalgrimaud.toto.service;

import io.github.pascalgrimaud.toto.domain.Alpha;
import io.github.pascalgrimaud.toto.repository.AlphaRepository;
import io.github.pascalgrimaud.toto.service.dto.AlphaDTO;
import io.github.pascalgrimaud.toto.service.mapper.AlphaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Alpha}.
 */
@Service
@Transactional
public class AlphaService {

    private final Logger log = LoggerFactory.getLogger(AlphaService.class);

    private final AlphaRepository alphaRepository;

    private final AlphaMapper alphaMapper;

    public AlphaService(AlphaRepository alphaRepository, AlphaMapper alphaMapper) {
        this.alphaRepository = alphaRepository;
        this.alphaMapper = alphaMapper;
    }

    /**
     * Save a alpha.
     *
     * @param alphaDTO the entity to save.
     * @return the persisted entity.
     */
    public AlphaDTO save(AlphaDTO alphaDTO) {
        log.debug("Request to save Alpha : {}", alphaDTO);
        Alpha alpha = alphaMapper.toEntity(alphaDTO);
        alpha = alphaRepository.save(alpha);
        return alphaMapper.toDto(alpha);
    }

    /**
     * Get all the alphas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AlphaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Alphas");
        return alphaRepository.findAll(pageable)
            .map(alphaMapper::toDto);
    }


    /**
     * Get one alpha by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlphaDTO> findOne(Long id) {
        log.debug("Request to get Alpha : {}", id);
        return alphaRepository.findById(id)
            .map(alphaMapper::toDto);
    }

    /**
     * Delete the alpha by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Alpha : {}", id);
        alphaRepository.deleteById(id);
    }
}
