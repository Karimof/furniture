package uz.apex.furniture.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.apex.furniture.web.rest.TestUtil;

class FurnitureDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FurnitureDTO.class);
        FurnitureDTO furnitureDTO1 = new FurnitureDTO();
        furnitureDTO1.setId(1L);
        FurnitureDTO furnitureDTO2 = new FurnitureDTO();
        assertThat(furnitureDTO1).isNotEqualTo(furnitureDTO2);
        furnitureDTO2.setId(furnitureDTO1.getId());
        assertThat(furnitureDTO1).isEqualTo(furnitureDTO2);
        furnitureDTO2.setId(2L);
        assertThat(furnitureDTO1).isNotEqualTo(furnitureDTO2);
        furnitureDTO1.setId(null);
        assertThat(furnitureDTO1).isNotEqualTo(furnitureDTO2);
    }
}
