package uz.apex.furniture.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.apex.furniture.domain.Types;
import uz.apex.furniture.repository.TypesRepository;
import uz.apex.furniture.service.dto.TypesDTO;
import uz.apex.furniture.service.mapper.TypesMapper;

/**
 * Service Implementation for managing {@link Types}.
 */
@Service
@Transactional
public class TypesService {

    private final Logger log = LoggerFactory.getLogger(TypesService.class);

    private final TypesRepository typesRepository;

    private final TypesMapper typesMapper;

    public TypesService(TypesRepository typesRepository, TypesMapper typesMapper) {
        this.typesRepository = typesRepository;
        this.typesMapper = typesMapper;
    }

    /**
     * Save a types.
     *
     * @param typesDTO the entity to save.
     * @return the persisted entity.
     */
    public TypesDTO save(TypesDTO typesDTO) {
        log.debug("Request to save Types : {}", typesDTO);
        Types types = typesMapper.toEntity(typesDTO);
        types = typesRepository.save(types);
        return typesMapper.toDto(types);
    }

    /**
     * Update a types.
     *
     * @param typesDTO the entity to save.
     * @return the persisted entity.
     */
    public TypesDTO update(TypesDTO typesDTO) {
        log.debug("Request to update Types : {}", typesDTO);
        Types types = typesMapper.toEntity(typesDTO);
        types = typesRepository.save(types);
        return typesMapper.toDto(types);
    }

    /**
     * Partially update a types.
     *
     * @param typesDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TypesDTO> partialUpdate(TypesDTO typesDTO) {
        log.debug("Request to partially update Types : {}", typesDTO);

        return typesRepository
            .findById(typesDTO.getId())
            .map(existingTypes -> {
                typesMapper.partialUpdate(existingTypes, typesDTO);

                return existingTypes;
            })
            .map(typesRepository::save)
            .map(typesMapper::toDto);
    }

    /**
     * Get all the types.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TypesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Types");
        return typesRepository.findAll(pageable).map(typesMapper::toDto);
    }

    /**
     * Get one types by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TypesDTO> findOne(Long id) {
        log.debug("Request to get Types : {}", id);
        return typesRepository.findById(id).map(typesMapper::toDto);
    }

    /**
     * Delete the types by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Types : {}", id);
        typesRepository.deleteById(id);
    }
}
