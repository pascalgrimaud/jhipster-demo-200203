package io.github.pascalgrimaud.toto.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.pascalgrimaud.toto.web.rest.TestUtil;

public class AlphaDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlphaDTO.class);
        AlphaDTO alphaDTO1 = new AlphaDTO();
        alphaDTO1.setId(1L);
        AlphaDTO alphaDTO2 = new AlphaDTO();
        assertThat(alphaDTO1).isNotEqualTo(alphaDTO2);
        alphaDTO2.setId(alphaDTO1.getId());
        assertThat(alphaDTO1).isEqualTo(alphaDTO2);
        alphaDTO2.setId(2L);
        assertThat(alphaDTO1).isNotEqualTo(alphaDTO2);
        alphaDTO1.setId(null);
        assertThat(alphaDTO1).isNotEqualTo(alphaDTO2);
    }
}
