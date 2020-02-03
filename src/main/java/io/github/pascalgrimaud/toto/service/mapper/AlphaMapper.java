package io.github.pascalgrimaud.toto.service.mapper;

import io.github.pascalgrimaud.toto.domain.*;
import io.github.pascalgrimaud.toto.service.dto.AlphaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Alpha} and its DTO {@link AlphaDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AlphaMapper extends EntityMapper<AlphaDTO, Alpha> {



    default Alpha fromId(Long id) {
        if (id == null) {
            return null;
        }
        Alpha alpha = new Alpha();
        alpha.setId(id);
        return alpha;
    }
}
