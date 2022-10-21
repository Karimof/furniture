package uz.apex.furniture.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.apex.furniture.web.rest.TestUtil;

class FurnitureTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Furniture.class);
        Furniture furniture1 = new Furniture();
        furniture1.setId(1L);
        Furniture furniture2 = new Furniture();
        furniture2.setId(furniture1.getId());
        assertThat(furniture1).isEqualTo(furniture2);
        furniture2.setId(2L);
        assertThat(furniture1).isNotEqualTo(furniture2);
        furniture1.setId(null);
        assertThat(furniture1).isNotEqualTo(furniture2);
    }
}
