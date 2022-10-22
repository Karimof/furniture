package uz.apex.furniture.service.mapper;

import org.mapstruct.*;
import uz.apex.furniture.domain.Brand;
import uz.apex.furniture.domain.Furniture;
import uz.apex.furniture.service.dto.BrandDTO;
import uz.apex.furniture.service.dto.FurnitureDTO;

/**
 * Mapper for the entity {@link Furniture} and its DTO {@link FurnitureDTO}.
 */
@Mapper(componentModel = "spring")
public interface FurnitureMapper extends EntityMapper<FurnitureDTO, Furniture> {
    @Mapping(target = "brand", source = "brand", qualifiedByName = "brandId")
    FurnitureDTO toDto(Furniture s);

    @Named("brandId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BrandDTO toDtoBrandId(Brand brand);
}
