package uz.apex.furniture.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.apex.furniture.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
