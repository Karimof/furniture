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
import uz.apex.furniture.domain.Type;
import uz.apex.furniture.repository.TypeRepository;
import uz.apex.furniture.service.dto.TypeDTO;
import uz.apex.furniture.service.mapper.TypeMapper;

/**
 * Integration tests for the {@link TypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TypeRepository typeRepository;

    @Autowired
    private TypeMapper typeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypeMockMvc;

    private Type type;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Type createEntity(EntityManager em) {
        Type type = new Type().name(DEFAULT_NAME);
        return type;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Type createUpdatedEntity(EntityManager em) {
        Type type = new Type().name(UPDATED_NAME);
        return type;
    }

    @BeforeEach
    public void initTest() {
        type = createEntity(em);
    }

    @Test
    @Transactional
    void createType() throws Exception {
        int databaseSizeBeforeCreate = typeRepository.findAll().size();
        // Create the Type
        TypeDTO typeDTO = typeMapper.toDto(type);
        restTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeDTO)))
            .andExpect(status().isCreated());

        // Validate the Type in the database
        List<Type> typeList = typeRepository.findAll();
        assertThat(typeList).hasSize(databaseSizeBeforeCreate + 1);
        Type testType = typeList.get(typeList.size() - 1);
        assertThat(testType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createTypeWithExistingId() throws Exception {
        // Create the Type with an existing ID
        type.setId(1L);
        TypeDTO typeDTO = typeMapper.toDto(type);

        int databaseSizeBeforeCreate = typeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Type in the database
        List<Type> typeList = typeRepository.findAll();
        assertThat(typeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTypes() throws Exception {
        // Initialize the database
        typeRepository.saveAndFlush(type);

        // Get all the typeList
        restTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(type.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getType() throws Exception {
        // Initialize the database
        typeRepository.saveAndFlush(type);

        // Get the type
        restTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, type.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(type.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingType() throws Exception {
        // Get the type
        restTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingType() throws Exception {
        // Initialize the database
        typeRepository.saveAndFlush(type);

        int databaseSizeBeforeUpdate = typeRepository.findAll().size();

        // Update the type
        Type updatedType = typeRepository.findById(type.getId()).get();
        // Disconnect from session so that the updates on updatedType are not directly saved in db
        em.detach(updatedType);
        updatedType.name(UPDATED_NAME);
        TypeDTO typeDTO = typeMapper.toDto(updatedType);

        restTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Type in the database
        List<Type> typeList = typeRepository.findAll();
        assertThat(typeList).hasSize(databaseSizeBeforeUpdate);
        Type testType = typeList.get(typeList.size() - 1);
        assertThat(testType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingType() throws Exception {
        int databaseSizeBeforeUpdate = typeRepository.findAll().size();
        type.setId(count.incrementAndGet());

        // Create the Type
        TypeDTO typeDTO = typeMapper.toDto(type);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Type in the database
        List<Type> typeList = typeRepository.findAll();
        assertThat(typeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchType() throws Exception {
        int databaseSizeBeforeUpdate = typeRepository.findAll().size();
        type.setId(count.incrementAndGet());

        // Create the Type
        TypeDTO typeDTO = typeMapper.toDto(type);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Type in the database
        List<Type> typeList = typeRepository.findAll();
        assertThat(typeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamType() throws Exception {
        int databaseSizeBeforeUpdate = typeRepository.findAll().size();
        type.setId(count.incrementAndGet());

        // Create the Type
        TypeDTO typeDTO = typeMapper.toDto(type);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Type in the database
        List<Type> typeList = typeRepository.findAll();
        assertThat(typeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTypeWithPatch() throws Exception {
        // Initialize the database
        typeRepository.saveAndFlush(type);

        int databaseSizeBeforeUpdate = typeRepository.findAll().size();

        // Update the type using partial update
        Type partialUpdatedType = new Type();
        partialUpdatedType.setId(type.getId());

        partialUpdatedType.name(UPDATED_NAME);

        restTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedType))
            )
            .andExpect(status().isOk());

        // Validate the Type in the database
        List<Type> typeList = typeRepository.findAll();
        assertThat(typeList).hasSize(databaseSizeBeforeUpdate);
        Type testType = typeList.get(typeList.size() - 1);
        assertThat(testType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateTypeWithPatch() throws Exception {
        // Initialize the database
        typeRepository.saveAndFlush(type);

        int databaseSizeBeforeUpdate = typeRepository.findAll().size();

        // Update the type using partial update
        Type partialUpdatedType = new Type();
        partialUpdatedType.setId(type.getId());

        partialUpdatedType.name(UPDATED_NAME);

        restTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedType))
            )
            .andExpect(status().isOk());

        // Validate the Type in the database
        List<Type> typeList = typeRepository.findAll();
        assertThat(typeList).hasSize(databaseSizeBeforeUpdate);
        Type testType = typeList.get(typeList.size() - 1);
        assertThat(testType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingType() throws Exception {
        int databaseSizeBeforeUpdate = typeRepository.findAll().size();
        type.setId(count.incrementAndGet());

        // Create the Type
        TypeDTO typeDTO = typeMapper.toDto(type);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Type in the database
        List<Type> typeList = typeRepository.findAll();
        assertThat(typeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchType() throws Exception {
        int databaseSizeBeforeUpdate = typeRepository.findAll().size();
        type.setId(count.incrementAndGet());

        // Create the Type
        TypeDTO typeDTO = typeMapper.toDto(type);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Type in the database
        List<Type> typeList = typeRepository.findAll();
        assertThat(typeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamType() throws Exception {
        int databaseSizeBeforeUpdate = typeRepository.findAll().size();
        type.setId(count.incrementAndGet());

        // Create the Type
        TypeDTO typeDTO = typeMapper.toDto(type);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(typeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Type in the database
        List<Type> typeList = typeRepository.findAll();
        assertThat(typeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteType() throws Exception {
        // Initialize the database
        typeRepository.saveAndFlush(type);

        int databaseSizeBeforeDelete = typeRepository.findAll().size();

        // Delete the type
        restTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, type.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Type> typeList = typeRepository.findAll();
        assertThat(typeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
