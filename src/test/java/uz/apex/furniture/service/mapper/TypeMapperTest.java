package uz.apex.furniture.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeMapperTest {

    private TypeMapper typeMapper;

    @BeforeEach
    public void setUp() {
        typeMapper = new TypeMapperImpl();
    }
}
