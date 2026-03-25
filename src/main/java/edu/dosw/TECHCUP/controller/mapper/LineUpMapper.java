package edu.dosw.TECHCUP.controller.mapper;

import edu.dosw.TECHCUP.controller.dto.request.LineUpRequestDTO;
import edu.dosw.TECHCUP.controller.dto.response.LineUpResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LineUpMapper {

    LineUpMapper toEntity(LineUpRequestDTO dto);

    LineUpResponseDTO toDto(LineUpMapper entity);

}
