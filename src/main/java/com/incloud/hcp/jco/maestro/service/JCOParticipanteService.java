package com.incloud.hcp.jco.maestro.service;


import com.incloud.hcp.dto.ParticipanteDto;

import java.util.List;

public interface JCOParticipanteService {
    List<ParticipanteDto> listaParticipanteSap() throws Exception;
    ParticipanteDto grabarParticipante(ParticipanteDto dto) throws Exception;
}
