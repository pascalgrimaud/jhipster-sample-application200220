package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterSampleApplicationApp;
import com.mycompany.myapp.domain.Toto;
import com.mycompany.myapp.repository.TotoRepository;
import com.mycompany.myapp.repository.search.TotoSearchRepository;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;


import java.util.Collections;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link TotoResource} REST controller.
 */
@SpringBootTest(classes = JhipsterSampleApplicationApp.class)
public class TotoResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private TotoRepository totoRepository;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.TotoSearchRepositoryMockConfiguration
     */
    @Autowired
    private TotoSearchRepository mockTotoSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restTotoMockMvc;

    private Toto toto;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TotoResource totoResource = new TotoResource(totoRepository, mockTotoSearchRepository);
        this.restTotoMockMvc = MockMvcBuilders.standaloneSetup(totoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Toto createEntity() {
        Toto toto = new Toto()
            .name(DEFAULT_NAME);
        return toto;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Toto createUpdatedEntity() {
        Toto toto = new Toto()
            .name(UPDATED_NAME);
        return toto;
    }

    @BeforeEach
    public void initTest() {
        totoRepository.deleteAll();
        toto = createEntity();
    }

    @Test
    public void createToto() throws Exception {
        int databaseSizeBeforeCreate = totoRepository.findAll().size();

        // Create the Toto
        restTotoMockMvc.perform(post("/api/totos")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(toto)))
            .andExpect(status().isCreated());

        // Validate the Toto in the database
        List<Toto> totoList = totoRepository.findAll();
        assertThat(totoList).hasSize(databaseSizeBeforeCreate + 1);
        Toto testToto = totoList.get(totoList.size() - 1);
        assertThat(testToto.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the Toto in Elasticsearch
        verify(mockTotoSearchRepository, times(1)).save(testToto);
    }

    @Test
    public void createTotoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = totoRepository.findAll().size();

        // Create the Toto with an existing ID
        toto.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restTotoMockMvc.perform(post("/api/totos")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(toto)))
            .andExpect(status().isBadRequest());

        // Validate the Toto in the database
        List<Toto> totoList = totoRepository.findAll();
        assertThat(totoList).hasSize(databaseSizeBeforeCreate);

        // Validate the Toto in Elasticsearch
        verify(mockTotoSearchRepository, times(0)).save(toto);
    }


    @Test
    public void getAllTotos() throws Exception {
        // Initialize the database
        totoRepository.save(toto);

        // Get all the totoList
        restTotoMockMvc.perform(get("/api/totos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(toto.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    public void getToto() throws Exception {
        // Initialize the database
        totoRepository.save(toto);

        // Get the toto
        restTotoMockMvc.perform(get("/api/totos/{id}", toto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(toto.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    public void getNonExistingToto() throws Exception {
        // Get the toto
        restTotoMockMvc.perform(get("/api/totos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateToto() throws Exception {
        // Initialize the database
        totoRepository.save(toto);

        int databaseSizeBeforeUpdate = totoRepository.findAll().size();

        // Update the toto
        Toto updatedToto = totoRepository.findById(toto.getId()).get();
        updatedToto
            .name(UPDATED_NAME);

        restTotoMockMvc.perform(put("/api/totos")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedToto)))
            .andExpect(status().isOk());

        // Validate the Toto in the database
        List<Toto> totoList = totoRepository.findAll();
        assertThat(totoList).hasSize(databaseSizeBeforeUpdate);
        Toto testToto = totoList.get(totoList.size() - 1);
        assertThat(testToto.getName()).isEqualTo(UPDATED_NAME);

        // Validate the Toto in Elasticsearch
        verify(mockTotoSearchRepository, times(1)).save(testToto);
    }

    @Test
    public void updateNonExistingToto() throws Exception {
        int databaseSizeBeforeUpdate = totoRepository.findAll().size();

        // Create the Toto

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTotoMockMvc.perform(put("/api/totos")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(toto)))
            .andExpect(status().isBadRequest());

        // Validate the Toto in the database
        List<Toto> totoList = totoRepository.findAll();
        assertThat(totoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Toto in Elasticsearch
        verify(mockTotoSearchRepository, times(0)).save(toto);
    }

    @Test
    public void deleteToto() throws Exception {
        // Initialize the database
        totoRepository.save(toto);

        int databaseSizeBeforeDelete = totoRepository.findAll().size();

        // Delete the toto
        restTotoMockMvc.perform(delete("/api/totos/{id}", toto.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Toto> totoList = totoRepository.findAll();
        assertThat(totoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Toto in Elasticsearch
        verify(mockTotoSearchRepository, times(1)).deleteById(toto.getId());
    }

    @Test
    public void searchToto() throws Exception {
        // Initialize the database
        totoRepository.save(toto);
        when(mockTotoSearchRepository.search(queryStringQuery("id:" + toto.getId())))
            .thenReturn(Collections.singletonList(toto));
        // Search the toto
        restTotoMockMvc.perform(get("/api/_search/totos?query=id:" + toto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(toto.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
}
