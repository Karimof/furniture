package uz.apex.furniture.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.apex.furniture.web.rest.TestUtil;

class TypesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypesDTO.class);
        TypesDTO typesDTO1 = new TypesDTO();
        typesDTO1.setId(1L);
        TypesDTO typesDTO2 = new TypesDTO();
        assertThat(typesDTO1).isNotEqualTo(typesDTO2);
        typesDTO2.setId(typesDTO1.getId());
        assertThat(typesDTO1).isEqualTo(typesDTO2);
        typesDTO2.setId(2L);
        assertThat(typesDTO1).isNotEqualTo(typesDTO2);
        typesDTO1.setId(null);
        assertThat(typesDTO1).isNotEqualTo(typesDTO2);
    }
}
