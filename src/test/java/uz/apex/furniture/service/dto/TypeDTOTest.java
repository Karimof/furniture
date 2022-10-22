package uz.apex.furniture.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.apex.furniture.web.rest.TestUtil;

class TypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeDTO.class);
        TypeDTO typeDTO1 = new TypeDTO();
        typeDTO1.setId(1L);
        TypeDTO typeDTO2 = new TypeDTO();
        assertThat(typeDTO1).isNotEqualTo(typeDTO2);
        typeDTO2.setId(typeDTO1.getId());
        assertThat(typeDTO1).isEqualTo(typeDTO2);
        typeDTO2.setId(2L);
        assertThat(typeDTO1).isNotEqualTo(typeDTO2);
        typeDTO1.setId(null);
        assertThat(typeDTO1).isNotEqualTo(typeDTO2);
    }
}
