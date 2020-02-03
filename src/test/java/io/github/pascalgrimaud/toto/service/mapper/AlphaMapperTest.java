package io.github.pascalgrimaud.toto.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class AlphaMapperTest {

    private AlphaMapper alphaMapper;

    @BeforeEach
    public void setUp() {
        alphaMapper = new AlphaMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(alphaMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(alphaMapper.fromId(null)).isNull();
    }
}
