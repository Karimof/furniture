package uz.apex.furniture.service.mapper;

import org.mapstruct.*;
import uz.apex.furniture.domain.Type;
import uz.apex.furniture.service.dto.TypeDTO;

/**
 * Mapper for the entity {@link Type} and its DTO {@link TypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface TypeMapper extends EntityMapper<TypeDTO, Type> {}
