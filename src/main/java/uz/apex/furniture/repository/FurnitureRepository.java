package uz.apex.furniture.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import uz.apex.furniture.domain.Furniture;

/**
 * Spring Data JPA repository for the Furniture entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FurnitureRepository extends JpaRepository<Furniture, Long> {}
