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
import uz.apex.furniture.domain.Furniture;
import uz.apex.furniture.domain.enumeration.Installation;
import uz.apex.furniture.repository.FurnitureRepository;
import uz.apex.furniture.service.dto.FurnitureDTO;
import uz.apex.furniture.service.mapper.FurnitureMapper;

/**
 * Integration tests for the {@link FurnitureResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FurnitureResourceIT {

    private static final String DEFAULT_PHOTO = "AAAAAAAAAA";
    private static final String UPDATED_PHOTO = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Installation DEFAULT_INSTALLATION = Installation.VERTICAL;
    private static final Installation UPDATED_INSTALLATION = Installation.HORIZONTAL;

    private static final String ENTITY_API_URL = "/api/furnitures";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FurnitureRepository furnitureRepository;

    @Autowired
    private FurnitureMapper furnitureMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFurnitureMockMvc;

    private Furniture furniture;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Furniture createEntity(EntityManager em) {
        Furniture furniture = new Furniture()
            .photo(DEFAULT_PHOTO)
            .image(DEFAULT_IMAGE)
            .name(DEFAULT_NAME)
            .installation(DEFAULT_INSTALLATION);
        return furniture;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Furniture createUpdatedEntity(EntityManager em) {
        Furniture furniture = new Furniture()
            .photo(UPDATED_PHOTO)
            .image(UPDATED_IMAGE)
            .name(UPDATED_NAME)
            .installation(UPDATED_INSTALLATION);
        return furniture;
    }

    @BeforeEach
    public void initTest() {
        furniture = createEntity(em);
    }

    @Test
    @Transactional
    void createFurniture() throws Exception {
        int databaseSizeBeforeCreate = furnitureRepository.findAll().size();
        // Create the Furniture
        FurnitureDTO furnitureDTO = furnitureMapper.toDto(furniture);
        restFurnitureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(furnitureDTO)))
            .andExpect(status().isCreated());

        // Validate the Furniture in the database
        List<Furniture> furnitureList = furnitureRepository.findAll();
        assertThat(furnitureList).hasSize(databaseSizeBeforeCreate + 1);
        Furniture testFurniture = furnitureList.get(furnitureList.size() - 1);
        assertThat(testFurniture.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testFurniture.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testFurniture.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFurniture.getInstallation()).isEqualTo(DEFAULT_INSTALLATION);
    }

    @Test
    @Transactional
    void createFurnitureWithExistingId() throws Exception {
        // Create the Furniture with an existing ID
        furniture.setId(1L);
        FurnitureDTO furnitureDTO = furnitureMapper.toDto(furniture);

        int databaseSizeBeforeCreate = furnitureRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFurnitureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(furnitureDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Furniture in the database
        List<Furniture> furnitureList = furnitureRepository.findAll();
        assertThat(furnitureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPhotoIsRequired() throws Exception {
        int databaseSizeBeforeTest = furnitureRepository.findAll().size();
        // set the field null
        furniture.setPhoto(null);

        // Create the Furniture, which fails.
        FurnitureDTO furnitureDTO = furnitureMapper.toDto(furniture);

        restFurnitureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(furnitureDTO)))
            .andExpect(status().isBadRequest());

        List<Furniture> furnitureList = furnitureRepository.findAll();
        assertThat(furnitureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkImageIsRequired() throws Exception {
        int databaseSizeBeforeTest = furnitureRepository.findAll().size();
        // set the field null
        furniture.setImage(null);

        // Create the Furniture, which fails.
        FurnitureDTO furnitureDTO = furnitureMapper.toDto(furniture);

        restFurnitureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(furnitureDTO)))
            .andExpect(status().isBadRequest());

        List<Furniture> furnitureList = furnitureRepository.findAll();
        assertThat(furnitureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = furnitureRepository.findAll().size();
        // set the field null
        furniture.setName(null);

        // Create the Furniture, which fails.
        FurnitureDTO furnitureDTO = furnitureMapper.toDto(furniture);

        restFurnitureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(furnitureDTO)))
            .andExpect(status().isBadRequest());

        List<Furniture> furnitureList = furnitureRepository.findAll();
        assertThat(furnitureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInstallationIsRequired() throws Exception {
        int databaseSizeBeforeTest = furnitureRepository.findAll().size();
        // set the field null
        furniture.setInstallation(null);

        // Create the Furniture, which fails.
        FurnitureDTO furnitureDTO = furnitureMapper.toDto(furniture);

        restFurnitureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(furnitureDTO)))
            .andExpect(status().isBadRequest());

        List<Furniture> furnitureList = furnitureRepository.findAll();
        assertThat(furnitureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFurnitures() throws Exception {
        // Initialize the database
        furnitureRepository.saveAndFlush(furniture);

        // Get all the furnitureList
        restFurnitureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(furniture.getId().intValue())))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(DEFAULT_PHOTO)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].installation").value(hasItem(DEFAULT_INSTALLATION.toString())));
    }

    @Test
    @Transactional
    void getFurniture() throws Exception {
        // Initialize the database
        furnitureRepository.saveAndFlush(furniture);

        // Get the furniture
        restFurnitureMockMvc
            .perform(get(ENTITY_API_URL_ID, furniture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(furniture.getId().intValue()))
            .andExpect(jsonPath("$.photo").value(DEFAULT_PHOTO))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.installation").value(DEFAULT_INSTALLATION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFurniture() throws Exception {
        // Get the furniture
        restFurnitureMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFurniture() throws Exception {
        // Initialize the database
        furnitureRepository.saveAndFlush(furniture);

        int databaseSizeBeforeUpdate = furnitureRepository.findAll().size();

        // Update the furniture
        Furniture updatedFurniture = furnitureRepository.findById(furniture.getId()).get();
        // Disconnect from session so that the updates on updatedFurniture are not directly saved in db
        em.detach(updatedFurniture);
        updatedFurniture.photo(UPDATED_PHOTO).image(UPDATED_IMAGE).name(UPDATED_NAME).installation(UPDATED_INSTALLATION);
        FurnitureDTO furnitureDTO = furnitureMapper.toDto(updatedFurniture);

        restFurnitureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, furnitureDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(furnitureDTO))
            )
            .andExpect(status().isOk());

        // Validate the Furniture in the database
        List<Furniture> furnitureList = furnitureRepository.findAll();
        assertThat(furnitureList).hasSize(databaseSizeBeforeUpdate);
        Furniture testFurniture = furnitureList.get(furnitureList.size() - 1);
        assertThat(testFurniture.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testFurniture.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testFurniture.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFurniture.getInstallation()).isEqualTo(UPDATED_INSTALLATION);
    }

    @Test
    @Transactional
    void putNonExistingFurniture() throws Exception {
        int databaseSizeBeforeUpdate = furnitureRepository.findAll().size();
        furniture.setId(count.incrementAndGet());

        // Create the Furniture
        FurnitureDTO furnitureDTO = furnitureMapper.toDto(furniture);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFurnitureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, furnitureDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(furnitureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Furniture in the database
        List<Furniture> furnitureList = furnitureRepository.findAll();
        assertThat(furnitureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFurniture() throws Exception {
        int databaseSizeBeforeUpdate = furnitureRepository.findAll().size();
        furniture.setId(count.incrementAndGet());

        // Create the Furniture
        FurnitureDTO furnitureDTO = furnitureMapper.toDto(furniture);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFurnitureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(furnitureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Furniture in the database
        List<Furniture> furnitureList = furnitureRepository.findAll();
        assertThat(furnitureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFurniture() throws Exception {
        int databaseSizeBeforeUpdate = furnitureRepository.findAll().size();
        furniture.setId(count.incrementAndGet());

        // Create the Furniture
        FurnitureDTO furnitureDTO = furnitureMapper.toDto(furniture);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFurnitureMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(furnitureDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Furniture in the database
        List<Furniture> furnitureList = furnitureRepository.findAll();
        assertThat(furnitureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFurnitureWithPatch() throws Exception {
        // Initialize the database
        furnitureRepository.saveAndFlush(furniture);

        int databaseSizeBeforeUpdate = furnitureRepository.findAll().size();

        // Update the furniture using partial update
        Furniture partialUpdatedFurniture = new Furniture();
        partialUpdatedFurniture.setId(furniture.getId());

        partialUpdatedFurniture.name(UPDATED_NAME).installation(UPDATED_INSTALLATION);

        restFurnitureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFurniture.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFurniture))
            )
            .andExpect(status().isOk());

        // Validate the Furniture in the database
        List<Furniture> furnitureList = furnitureRepository.findAll();
        assertThat(furnitureList).hasSize(databaseSizeBeforeUpdate);
        Furniture testFurniture = furnitureList.get(furnitureList.size() - 1);
        assertThat(testFurniture.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testFurniture.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testFurniture.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFurniture.getInstallation()).isEqualTo(UPDATED_INSTALLATION);
    }

    @Test
    @Transactional
    void fullUpdateFurnitureWithPatch() throws Exception {
        // Initialize the database
        furnitureRepository.saveAndFlush(furniture);

        int databaseSizeBeforeUpdate = furnitureRepository.findAll().size();

        // Update the furniture using partial update
        Furniture partialUpdatedFurniture = new Furniture();
        partialUpdatedFurniture.setId(furniture.getId());

        partialUpdatedFurniture.photo(UPDATED_PHOTO).image(UPDATED_IMAGE).name(UPDATED_NAME).installation(UPDATED_INSTALLATION);

        restFurnitureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFurniture.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFurniture))
            )
            .andExpect(status().isOk());

        // Validate the Furniture in the database
        List<Furniture> furnitureList = furnitureRepository.findAll();
        assertThat(furnitureList).hasSize(databaseSizeBeforeUpdate);
        Furniture testFurniture = furnitureList.get(furnitureList.size() - 1);
        assertThat(testFurniture.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testFurniture.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testFurniture.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFurniture.getInstallation()).isEqualTo(UPDATED_INSTALLATION);
    }

    @Test
    @Transactional
    void patchNonExistingFurniture() throws Exception {
        int databaseSizeBeforeUpdate = furnitureRepository.findAll().size();
        furniture.setId(count.incrementAndGet());

        // Create the Furniture
        FurnitureDTO furnitureDTO = furnitureMapper.toDto(furniture);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFurnitureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, furnitureDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(furnitureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Furniture in the database
        List<Furniture> furnitureList = furnitureRepository.findAll();
        assertThat(furnitureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFurniture() throws Exception {
        int databaseSizeBeforeUpdate = furnitureRepository.findAll().size();
        furniture.setId(count.incrementAndGet());

        // Create the Furniture
        FurnitureDTO furnitureDTO = furnitureMapper.toDto(furniture);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFurnitureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(furnitureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Furniture in the database
        List<Furniture> furnitureList = furnitureRepository.findAll();
        assertThat(furnitureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFurniture() throws Exception {
        int databaseSizeBeforeUpdate = furnitureRepository.findAll().size();
        furniture.setId(count.incrementAndGet());

        // Create the Furniture
        FurnitureDTO furnitureDTO = furnitureMapper.toDto(furniture);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFurnitureMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(furnitureDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Furniture in the database
        List<Furniture> furnitureList = furnitureRepository.findAll();
        assertThat(furnitureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFurniture() throws Exception {
        // Initialize the database
        furnitureRepository.saveAndFlush(furniture);

        int databaseSizeBeforeDelete = furnitureRepository.findAll().size();

        // Delete the furniture
        restFurnitureMockMvc
            .perform(delete(ENTITY_API_URL_ID, furniture.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Furniture> furnitureList = furnitureRepository.findAll();
        assertThat(furnitureList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
