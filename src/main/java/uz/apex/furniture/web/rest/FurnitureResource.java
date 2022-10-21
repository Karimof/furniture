package uz.apex.furniture.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import uz.apex.furniture.repository.FurnitureRepository;
import uz.apex.furniture.service.FurnitureService;
import uz.apex.furniture.service.dto.BrandDTO;
import uz.apex.furniture.service.dto.FurnitureDTO;
import uz.apex.furniture.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link uz.apex.furniture.domain.Furniture}.
 */
@RestController
@RequestMapping("/api")
public class FurnitureResource {

    private final Logger log = LoggerFactory.getLogger(FurnitureResource.class);

    private static final String ENTITY_NAME = "furniture";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FurnitureService furnitureService;

    private final FurnitureRepository furnitureRepository;

    public FurnitureResource(FurnitureService furnitureService, FurnitureRepository furnitureRepository) {
        this.furnitureService = furnitureService;
        this.furnitureRepository = furnitureRepository;
    }

    /**
     * {@code POST  /furnitures} : Create a new furniture.
     *
     * @param furnitureDTO the furnitureDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new furnitureDTO, or with status {@code 400 (Bad Request)} if the furniture has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/furnitures")
    public ResponseEntity<FurnitureDTO> createFurniture(@Valid @RequestBody FurnitureDTO furnitureDTO) throws URISyntaxException {
        log.debug("REST request to save Furniture : {}", furnitureDTO);
        if (furnitureDTO.getId() != null) {
            throw new BadRequestAlertException("A new furniture cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FurnitureDTO result = furnitureService.save(furnitureDTO);
        return ResponseEntity
            .created(new URI("/api/furnitures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /furnitures/:id} : Updates an existing furniture.
     *
     * @param id the id of the furnitureDTO to save.
     * @param furnitureDTO the furnitureDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated furnitureDTO,
     * or with status {@code 400 (Bad Request)} if the furnitureDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the furnitureDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/furnitures/{id}")
    public ResponseEntity<FurnitureDTO> updateFurniture(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FurnitureDTO furnitureDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Furniture : {}, {}", id, furnitureDTO);
        if (furnitureDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, furnitureDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!furnitureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FurnitureDTO result = furnitureService.update(furnitureDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, furnitureDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /furnitures/:id} : Partial updates given fields of an existing furniture, field will ignore if it is null
     *
     * @param id the id of the furnitureDTO to save.
     * @param furnitureDTO the furnitureDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated furnitureDTO,
     * or with status {@code 400 (Bad Request)} if the furnitureDTO is not valid,
     * or with status {@code 404 (Not Found)} if the furnitureDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the furnitureDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/furnitures/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FurnitureDTO> partialUpdateFurniture(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FurnitureDTO furnitureDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Furniture partially : {}, {}", id, furnitureDTO);
        if (furnitureDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, furnitureDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!furnitureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FurnitureDTO> result = furnitureService.partialUpdate(furnitureDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, furnitureDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /furnitures} : get all the furnitures.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of furnitures in body.
     */
    @GetMapping("/furnitures")
    public ResponseEntity<List<FurnitureDTO>> getAllFurnitures(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Furnitures");
        Page<FurnitureDTO> page = furnitureService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /furnitures/:id} : get the "id" furniture.
     *
     * @param id the id of the furnitureDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the furnitureDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/furnitures/{id}")
    public ResponseEntity<FurnitureDTO> getFurniture(@PathVariable Long id) {
        log.debug("REST request to get Furniture : {}", id);
        Optional<FurnitureDTO> furnitureDTO = furnitureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(furnitureDTO);
    }

    /**
     * {@code DELETE  /furnitures/:id} : delete the "id" furniture.
     *
     * @param id the id of the furnitureDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/furnitures/{id}")
    public ResponseEntity<Void> deleteFurniture(@PathVariable Long id) {
        log.debug("REST request to delete Furniture : {}", id);
        furnitureService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
