package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Toto;
import com.mycompany.myapp.repository.TotoRepository;
import com.mycompany.myapp.repository.search.TotoSearchRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Toto}.
 */
@RestController
@RequestMapping("/api")
public class TotoResource {

    private final Logger log = LoggerFactory.getLogger(TotoResource.class);

    private static final String ENTITY_NAME = "toto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TotoRepository totoRepository;

    private final TotoSearchRepository totoSearchRepository;

    public TotoResource(TotoRepository totoRepository, TotoSearchRepository totoSearchRepository) {
        this.totoRepository = totoRepository;
        this.totoSearchRepository = totoSearchRepository;
    }

    /**
     * {@code POST  /totos} : Create a new toto.
     *
     * @param toto the toto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new toto, or with status {@code 400 (Bad Request)} if the toto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/totos")
    public ResponseEntity<Toto> createToto(@RequestBody Toto toto) throws URISyntaxException {
        log.debug("REST request to save Toto : {}", toto);
        if (toto.getId() != null) {
            throw new BadRequestAlertException("A new toto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Toto result = totoRepository.save(toto);
        totoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/totos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /totos} : Updates an existing toto.
     *
     * @param toto the toto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated toto,
     * or with status {@code 400 (Bad Request)} if the toto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the toto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/totos")
    public ResponseEntity<Toto> updateToto(@RequestBody Toto toto) throws URISyntaxException {
        log.debug("REST request to update Toto : {}", toto);
        if (toto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Toto result = totoRepository.save(toto);
        totoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, toto.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /totos} : get all the totos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of totos in body.
     */
    @GetMapping("/totos")
    public List<Toto> getAllTotos() {
        log.debug("REST request to get all Totos");
        return totoRepository.findAll();
    }

    /**
     * {@code GET  /totos/:id} : get the "id" toto.
     *
     * @param id the id of the toto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the toto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/totos/{id}")
    public ResponseEntity<Toto> getToto(@PathVariable String id) {
        log.debug("REST request to get Toto : {}", id);
        Optional<Toto> toto = totoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(toto);
    }

    /**
     * {@code DELETE  /totos/:id} : delete the "id" toto.
     *
     * @param id the id of the toto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/totos/{id}")
    public ResponseEntity<Void> deleteToto(@PathVariable String id) {
        log.debug("REST request to delete Toto : {}", id);
        totoRepository.deleteById(id);
        totoSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /_search/totos?query=:query} : search for the toto corresponding
     * to the query.
     *
     * @param query the query of the toto search.
     * @return the result of the search.
     */
    @GetMapping("/_search/totos")
    public List<Toto> searchTotos(@RequestParam String query) {
        log.debug("REST request to search Totos for query {}", query);
        return StreamSupport
            .stream(totoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
