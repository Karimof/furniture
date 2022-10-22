package uz.apex.furniture.service.mapper;

import org.mapstruct.*;
import uz.apex.furniture.domain.Brand;
import uz.apex.furniture.domain.Type;
import uz.apex.furniture.service.dto.BrandDTO;
import uz.apex.furniture.service.dto.TypeDTO;

/**
 * Mapper for the entity {@link Brand} and its DTO {@link BrandDTO}.
 */
@Mapper(componentModel = "spring")
public interface BrandMapper extends EntityMapper<BrandDTO, Brand> {
    @Mapping(target = "type", source = "type", qualifiedByName = "typeId")
    BrandDTO toDto(Brand s);

    @Named("typeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TypeDTO toDtoTypeId(Type type);
}
