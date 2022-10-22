package uz.apex.furniture.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.apex.furniture.domain.Furniture;
import uz.apex.furniture.repository.FurnitureRepository;
import uz.apex.furniture.service.dto.FurnitureDTO;
import uz.apex.furniture.service.mapper.FurnitureMapper;

/**
 * Service Implementation for managing {@link Furniture}.
 */
@Service
@Transactional
public class FurnitureService {

    private final Logger log = LoggerFactory.getLogger(FurnitureService.class);

    private final FurnitureRepository furnitureRepository;

    private final FurnitureMapper furnitureMapper;

    public FurnitureService(FurnitureRepository furnitureRepository, FurnitureMapper furnitureMapper) {
        this.furnitureRepository = furnitureRepository;
        this.furnitureMapper = furnitureMapper;
    }

    /**
     * Save a furniture.
     *
     * @param furnitureDTO the entity to save.
     * @return the persisted entity.
     */
    public FurnitureDTO save(FurnitureDTO furnitureDTO) {
        log.debug("Request to save Furniture : {}", furnitureDTO);
        Furniture furniture = furnitureMapper.toEntity(furnitureDTO);
        furniture = furnitureRepository.save(furniture);
        return furnitureMapper.toDto(furniture);
    }

    /**
     * Update a furniture.
     *
     * @param furnitureDTO the entity to save.
     * @return the persisted entity.
     */
    public FurnitureDTO update(FurnitureDTO furnitureDTO) {
        log.debug("Request to update Furniture : {}", furnitureDTO);
        Furniture furniture = furnitureMapper.toEntity(furnitureDTO);
        furniture = furnitureRepository.save(furniture);
        return furnitureMapper.toDto(furniture);
    }

    /**
     * Partially update a furniture.
     *
     * @param furnitureDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FurnitureDTO> partialUpdate(FurnitureDTO furnitureDTO) {
        log.debug("Request to partially update Furniture : {}", furnitureDTO);

        return furnitureRepository
            .findById(furnitureDTO.getId())
            .map(existingFurniture -> {
                furnitureMapper.partialUpdate(existingFurniture, furnitureDTO);

                return existingFurniture;
            })
            .map(furnitureRepository::save)
            .map(furnitureMapper::toDto);
    }

    /**
     * Get all the furnitures.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FurnitureDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Furnitures");
        return furnitureRepository.findAll(pageable).map(furnitureMapper::toDto);
    }

    /**
     * Get one furniture by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FurnitureDTO> findOne(Long id) {
        log.debug("Request to get Furniture : {}", id);
        return furnitureRepository.findById(id).map(furnitureMapper::toDto);
    }

    /**
     * Delete the furniture by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Furniture : {}", id);
        furnitureRepository.deleteById(id);
    }
}
