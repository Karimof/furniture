package uz.apex.furniture.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import uz.apex.furniture.IntegrationTest;
import uz.apex.furniture.domain.Types;
import uz.apex.furniture.repository.TypesRepository;
import uz.apex.furniture.service.dto.TypesDTO;
import uz.apex.furniture.service.mapper.TypesMapper;

/**
 * Integration tests for the {@link TypesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypesResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TypesRepository typesRepository;

    @Autowired
    private TypesMapper typesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypesMockMvc;

    private Types types;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Types createEntity(EntityManager em) {
        Types types = new Types().name(DEFAULT_NAME);
        return types;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Types createUpdatedEntity(EntityManager em) {
        Types types = new Types().name(UPDATED_NAME);
        return types;
    }

    @BeforeEach
    public void initTest() {
        types = createEntity(em);
    }

    @Test
    @Transactional
    void createTypes() throws Exception {
        int databaseSizeBeforeCreate = typesRepository.findAll().size();
        // Create the Types
        TypesDTO typesDTO = typesMapper.toDto(types);
        restTypesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typesDTO)))
            .andExpect(status().isCreated());

        // Validate the Types in the database
        List<Types> typesList = typesRepository.findAll();
        assertThat(typesList).hasSize(databaseSizeBeforeCreate + 1);
        Types testTypes = typesList.get(typesList.size() - 1);
        assertThat(testTypes.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createTypesWithExistingId() throws Exception {
        // Create the Types with an existing ID
        types.setId(1L);
        TypesDTO typesDTO = typesMapper.toDto(types);

        int databaseSizeBeforeCreate = typesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Types in the database
        List<Types> typesList = typesRepository.findAll();
        assertThat(typesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = typesRepository.findAll().size();
        // set the field null
        types.setName(null);

        // Create the Types, which fails.
        TypesDTO typesDTO = typesMapper.toDto(types);

        restTypesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typesDTO)))
            .andExpect(status().isBadRequest());

        List<Types> typesList = typesRepository.findAll();
        assertThat(typesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTypes() throws Exception {
        // Initialize the database
        typesRepository.saveAndFlush(types);

        // Get all the typesList
        restTypesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(types.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getTypes() throws Exception {
        // Initialize the database
        typesRepository.saveAndFlush(types);

        // Get the types
        restTypesMockMvc
            .perform(get(ENTITY_API_URL_ID, types.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(types.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingTypes() throws Exception {
        // Get the types
        restTypesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTypes() throws Exception {
        // Initialize the database
        typesRepository.saveAndFlush(types);

        int databaseSizeBeforeUpdate = typesRepository.findAll().size();

        // Update the types
        Types updatedTypes = typesRepository.findById(types.getId()).get();
        // Disconnect from session so that the updates on updatedTypes are not directly saved in db
        em.detach(updatedTypes);
        updatedTypes.name(UPDATED_NAME);
        TypesDTO typesDTO = typesMapper.toDto(updatedTypes);

        restTypesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typesDTO))
            )
            .andExpect(status().isOk());

        // Validate the Types in the database
        List<Types> typesList = typesRepository.findAll();
        assertThat(typesList).hasSize(databaseSizeBeforeUpdate);
        Types testTypes = typesList.get(typesList.size() - 1);
        assertThat(testTypes.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingTypes() throws Exception {
        int databaseSizeBeforeUpdate = typesRepository.findAll().size();
        types.setId(count.incrementAndGet());

        // Create the Types
        TypesDTO typesDTO = typesMapper.toDto(types);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Types in the database
        List<Types> typesList = typesRepository.findAll();
        assertThat(typesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypes() throws Exception {
        int databaseSizeBeforeUpdate = typesRepository.findAll().size();
        types.setId(count.incrementAndGet());

        // Create the Types
        TypesDTO typesDTO = typesMapper.toDto(types);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Types in the database
        List<Types> typesList = typesRepository.findAll();
        assertThat(typesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypes() throws Exception {
        int databaseSizeBeforeUpdate = typesRepository.findAll().size();
        types.setId(count.incrementAndGet());

        // Create the Types
        TypesDTO typesDTO = typesMapper.toDto(types);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Types in the database
        List<Types> typesList = typesRepository.findAll();
        assertThat(typesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTypesWithPatch() throws Exception {
        // Initialize the database
        typesRepository.saveAndFlush(types);

        int databaseSizeBeforeUpdate = typesRepository.findAll().size();

        // Update the types using partial update
        Types partialUpdatedTypes = new Types();
        partialUpdatedTypes.setId(types.getId());

        restTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypes))
            )
            .andExpect(status().isOk());

        // Validate the Types in the database
        List<Types> typesList = typesRepository.findAll();
        assertThat(typesList).hasSize(databaseSizeBeforeUpdate);
        Types testTypes = typesList.get(typesList.size() - 1);
        assertThat(testTypes.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateTypesWithPatch() throws Exception {
        // Initialize the database
        typesRepository.saveAndFlush(types);

        int databaseSizeBeforeUpdate = typesRepository.findAll().size();

        // Update the types using partial update
        Types partialUpdatedTypes = new Types();
        partialUpdatedTypes.setId(types.getId());

        partialUpdatedTypes.name(UPDATED_NAME);

        restTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypes))
            )
            .andExpect(status().isOk());

        // Validate the Types in the database
        List<Types> typesList = typesRepository.findAll();
        assertThat(typesList).hasSize(databaseSizeBeforeUpdate);
        Types testTypes = typesList.get(typesList.size() - 1);
        assertThat(testTypes.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingTypes() throws Exception {
        int databaseSizeBeforeUpdate = typesRepository.findAll().size();
        types.setId(count.incrementAndGet());

        // Create the Types
        TypesDTO typesDTO = typesMapper.toDto(types);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Types in the database
        List<Types> typesList = typesRepository.findAll();
        assertThat(typesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypes() throws Exception {
        int databaseSizeBeforeUpdate = typesRepository.findAll().size();
        types.setId(count.incrementAndGet());

        // Create the Types
        TypesDTO typesDTO = typesMapper.toDto(types);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Types in the database
        List<Types> typesList = typesRepository.findAll();
        assertThat(typesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypes() throws Exception {
        int databaseSizeBeforeUpdate = typesRepository.findAll().size();
        types.setId(count.incrementAndGet());

        // Create the Types
        TypesDTO typesDTO = typesMapper.toDto(types);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(typesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Types in the database
        List<Types> typesList = typesRepository.findAll();
        assertThat(typesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTypes() throws Exception {
        // Initialize the database
        typesRepository.saveAndFlush(types);

        int databaseSizeBeforeDelete = typesRepository.findAll().size();

        // Delete the types
        restTypesMockMvc
            .perform(delete(ENTITY_API_URL_ID, types.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Types> typesList = typesRepository.findAll();
        assertThat(typesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
