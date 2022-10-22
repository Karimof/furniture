package uz.apex.furniture.service.dto;

import org.junit.jupiter.api.Test;
import uz.apex.furniture.web.rest.TestUtil;

import static org.assertj.core.api.Assertions.assertThat;

class BrandDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BrandDTO.class);
        BrandDTO brandDTO1 = new BrandDTO();
        brandDTO1.setId(1L);
        BrandDTO brandDTO2 = new BrandDTO();
        assertThat(brandDTO1).isNotEqualTo(brandDTO2);
        brandDTO2.setId(brandDTO1.getId());
        assertThat(brandDTO1).isEqualTo(brandDTO2);
        brandDTO2.setId(2L);
        assertThat(brandDTO1).isNotEqualTo(brandDTO2);
        brandDTO1.setId(null);
        assertThat(brandDTO1).isNotEqualTo(brandDTO2);
    }
}
