package io.github.pascalgrimaud.toto.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.pascalgrimaud.toto.web.rest.TestUtil;

public class AlphaTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Alpha.class);
        Alpha alpha1 = new Alpha();
        alpha1.setId(1L);
        Alpha alpha2 = new Alpha();
        alpha2.setId(alpha1.getId());
        assertThat(alpha1).isEqualTo(alpha2);
        alpha2.setId(2L);
        assertThat(alpha1).isNotEqualTo(alpha2);
        alpha1.setId(null);
        assertThat(alpha1).isNotEqualTo(alpha2);
    }
}
