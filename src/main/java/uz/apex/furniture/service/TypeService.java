package uz.apex.furniture.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.apex.furniture.domain.Type;
import uz.apex.furniture.repository.TypeRepository;
import uz.apex.furniture.service.dto.TypeDTO;
import uz.apex.furniture.service.mapper.TypeMapper;

/**
 * Service Implementation for managing {@link Type}.
 */
@Service
@Transactional
public class TypeService {

    private final Logger log = LoggerFactory.getLogger(TypeService.class);

    private final TypeRepository typeRepository;

    private final TypeMapper typeMapper;

    public TypeService(TypeRepository typeRepository, TypeMapper typeMapper) {
        this.typeRepository = typeRepository;
        this.typeMapper = typeMapper;
    }

    /**
     * Save a type.
     *
     * @param typeDTO the entity to save.
     * @return the persisted entity.
     */
    public TypeDTO save(TypeDTO typeDTO) {
        log.debug("Request to save Type : {}", typeDTO);
        Type type = typeMapper.toEntity(typeDTO);
        type = typeRepository.save(type);
        return typeMapper.toDto(type);
    }

    /**
     * Update a type.
     *
     * @param typeDTO the entity to save.
     * @return the persisted entity.
     */
    public TypeDTO update(TypeDTO typeDTO) {
        log.debug("Request to update Type : {}", typeDTO);
        Type type = typeMapper.toEntity(typeDTO);
        type = typeRepository.save(type);
        return typeMapper.toDto(type);
    }

    /**
     * Partially update a type.
     *
     * @param typeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TypeDTO> partialUpdate(TypeDTO typeDTO) {
        log.debug("Request to partially update Type : {}", typeDTO);

        return typeRepository
            .findById(typeDTO.getId())
            .map(existingType -> {
                typeMapper.partialUpdate(existingType, typeDTO);

                return existingType;
            })
            .map(typeRepository::save)
            .map(typeMapper::toDto);
    }

    /**
     * Get all the types.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Types");
        return typeRepository.findAll(pageable).map(typeMapper::toDto);
    }

    /**
     * Get one type by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TypeDTO> findOne(Long id) {
        log.debug("Request to get Type : {}", id);
        return typeRepository.findById(id).map(typeMapper::toDto);
    }

    /**
     * Delete the type by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Type : {}", id);
        typeRepository.deleteById(id);
    }
}
