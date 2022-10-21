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
import uz.apex.furniture.repository.TypesRepository;
import uz.apex.furniture.service.TypesService;
import uz.apex.furniture.service.dto.TypesDTO;
import uz.apex.furniture.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link uz.apex.furniture.domain.Types}.
 */
@RestController
@RequestMapping("/api")
public class TypesResource {

    private final Logger log = LoggerFactory.getLogger(TypesResource.class);

    private static final String ENTITY_NAME = "types";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypesService typesService;

    private final TypesRepository typesRepository;

    public TypesResource(TypesService typesService, TypesRepository typesRepository) {
        this.typesService = typesService;
        this.typesRepository = typesRepository;
    }

    /**
     * {@code POST  /types} : Create a new types.
     *
     * @param typesDTO the typesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typesDTO, or with status {@code 400 (Bad Request)} if the types has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/types")
    public ResponseEntity<TypesDTO> createTypes(@Valid @RequestBody TypesDTO typesDTO) throws URISyntaxException {
        log.debug("REST request to save Types : {}", typesDTO);
        if (typesDTO.getId() != null) {
            throw new BadRequestAlertException("A new types cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypesDTO result = typesService.save(typesDTO);
        return ResponseEntity
            .created(new URI("/api/types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /types/:id} : Updates an existing types.
     *
     * @param id the id of the typesDTO to save.
     * @param typesDTO the typesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typesDTO,
     * or with status {@code 400 (Bad Request)} if the typesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/types/{id}")
    public ResponseEntity<TypesDTO> updateTypes(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TypesDTO typesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Types : {}, {}", id, typesDTO);
        if (typesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TypesDTO result = typesService.update(typesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /types/:id} : Partial updates given fields of an existing types, field will ignore if it is null
     *
     * @param id the id of the typesDTO to save.
     * @param typesDTO the typesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typesDTO,
     * or with status {@code 400 (Bad Request)} if the typesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the typesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the typesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TypesDTO> partialUpdateTypes(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TypesDTO typesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Types partially : {}, {}", id, typesDTO);
        if (typesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TypesDTO> result = typesService.partialUpdate(typesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /types} : get all the types.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of types in body.
     */
    @GetMapping("/types")
    public ResponseEntity<List<TypesDTO>> getAllTypes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Types");
        Page<TypesDTO> page = typesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /types/:id} : get the "id" types.
     *
     * @param id the id of the typesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/types/{id}")
    public ResponseEntity<TypesDTO> getTypes(@PathVariable Long id) {
        log.debug("REST request to get Types : {}", id);
        Optional<TypesDTO> typesDTO = typesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typesDTO);
    }

    /**
     * {@code DELETE  /types/:id} : delete the "id" types.
     *
     * @param id the id of the typesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/types/{id}")
    public ResponseEntity<Void> deleteTypes(@PathVariable Long id) {
        log.debug("REST request to delete Types : {}", id);
        typesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
