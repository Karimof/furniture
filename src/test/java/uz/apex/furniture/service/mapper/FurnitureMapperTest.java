package uz.apex.furniture.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FurnitureMapperTest {

    private FurnitureMapper furnitureMapper;

    @BeforeEach
    public void setUp() {
        furnitureMapper = new FurnitureMapperImpl();
    }
}
