package io.github.pascalgrimaud.toto.web.rest;

import io.github.pascalgrimaud.toto.service.AlphaService;
import io.github.pascalgrimaud.toto.web.rest.errors.BadRequestAlertException;
import io.github.pascalgrimaud.toto.service.dto.AlphaDTO;
import io.github.pascalgrimaud.toto.service.dto.AlphaCriteria;
import io.github.pascalgrimaud.toto.service.AlphaQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link io.github.pascalgrimaud.toto.domain.Alpha}.
 */
@RestController
@RequestMapping("/api")
public class AlphaResource {

    private final Logger log = LoggerFactory.getLogger(AlphaResource.class);

    private static final String ENTITY_NAME = "alpha";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlphaService alphaService;

    private final AlphaQueryService alphaQueryService;

    public AlphaResource(AlphaService alphaService, AlphaQueryService alphaQueryService) {
        this.alphaService = alphaService;
        this.alphaQueryService = alphaQueryService;
    }

    /**
     * {@code POST  /alphas} : Create a new alpha.
     *
     * @param alphaDTO the alphaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alphaDTO, or with status {@code 400 (Bad Request)} if the alpha has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/alphas")
    public ResponseEntity<AlphaDTO> createAlpha(@Valid @RequestBody AlphaDTO alphaDTO) throws URISyntaxException {
        log.debug("REST request to save Alpha : {}", alphaDTO);
        if (alphaDTO.getId() != null) {
            throw new BadRequestAlertException("A new alpha cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AlphaDTO result = alphaService.save(alphaDTO);
        return ResponseEntity.created(new URI("/api/alphas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /alphas} : Updates an existing alpha.
     *
     * @param alphaDTO the alphaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alphaDTO,
     * or with status {@code 400 (Bad Request)} if the alphaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alphaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/alphas")
    public ResponseEntity<AlphaDTO> updateAlpha(@Valid @RequestBody AlphaDTO alphaDTO) throws URISyntaxException {
        log.debug("REST request to update Alpha : {}", alphaDTO);
        if (alphaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AlphaDTO result = alphaService.save(alphaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alphaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /alphas} : get all the alphas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alphas in body.
     */
    @GetMapping("/alphas")
    public ResponseEntity<List<AlphaDTO>> getAllAlphas(AlphaCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Alphas by criteria: {}", criteria);
        Page<AlphaDTO> page = alphaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /alphas/count} : count all the alphas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/alphas/count")
    public ResponseEntity<Long> countAlphas(AlphaCriteria criteria) {
        log.debug("REST request to count Alphas by criteria: {}", criteria);
        return ResponseEntity.ok().body(alphaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /alphas/:id} : get the "id" alpha.
     *
     * @param id the id of the alphaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alphaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/alphas/{id}")
    public ResponseEntity<AlphaDTO> getAlpha(@PathVariable Long id) {
        log.debug("REST request to get Alpha : {}", id);
        Optional<AlphaDTO> alphaDTO = alphaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alphaDTO);
    }

    /**
     * {@code DELETE  /alphas/:id} : delete the "id" alpha.
     *
     * @param id the id of the alphaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/alphas/{id}")
    public ResponseEntity<Void> deleteAlpha(@PathVariable Long id) {
        log.debug("REST request to delete Alpha : {}", id);
        alphaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
