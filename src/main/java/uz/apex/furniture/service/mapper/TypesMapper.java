package uz.apex.furniture.service.mapper;

import org.mapstruct.*;
import uz.apex.furniture.domain.Types;
import uz.apex.furniture.service.dto.TypesDTO;

/**
 * Mapper for the entity {@link Types} and its DTO {@link TypesDTO}.
 */
@Mapper(componentModel = "spring")
public interface TypesMapper extends EntityMapper<TypesDTO, Types> {}
