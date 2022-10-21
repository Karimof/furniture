package uz.apex.furniture.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypesMapperTest {

    private TypesMapper typesMapper;

    @BeforeEach
    public void setUp() {
        typesMapper = new TypesMapperImpl();
    }
}
