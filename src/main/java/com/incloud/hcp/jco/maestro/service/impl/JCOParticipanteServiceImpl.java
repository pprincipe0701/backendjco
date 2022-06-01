package com.incloud.hcp.jco.maestro.service.impl;

import com.incloud.hcp.dto.ParticipanteDto;
import com.incloud.hcp.jco.maestro.service.JCOParticipanteService;
import com.sap.conn.jco.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
//@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
public class JCOParticipanteServiceImpl implements JCOParticipanteService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public List<ParticipanteDto> listaParticipanteSap() throws Exception {
        logger.error("listaParticipanteSap__");
       //Acceduendo a SAP

        List<ParticipanteDto> listaParticipante = new ArrayList<ParticipanteDto>();
        logger.error("listaParticipanteSap_1");

        //Obtencion del objeto de conexion (Destination)
        JCoDestination destination = JCoDestinationManager.getDestination("AIPSA_DEST_RFC");
        //JCo
        logger.error("listaParticipanteSap_2");
        //Repositorio
        JCoRepository repo = destination.getRepository();
        logger.error("listaParticipanteSapn_3");

        //Obtengo la funcion RFC SAP
        JCoFunction stfcConnection = repo.getFunction("ZRFC_DEMO_BTP");

        //Obtengo los parametros de importacion de la RFC
        JCoParameterList importParameter = stfcConnection.getImportParameterList();
        //Pasando los parametros de entrada de la RFC
        importParameter.setValue("I_OPERACION", "L");


        //Ejecutar Funcion
        stfcConnection.execute(destination);
        logger.error("listaEmbarcacion_6");

        //Obteniendo parametros de salida de la RFC (export)
        JCoParameterList export = stfcConnection.getExportParameterList();

        //Recuperando tablas Export
        JCoTable tableExport = export.getTable("ET_PARTICIPANTE");


        for (int i = 0; i < tableExport.getNumRows(); i++) {
            tableExport.setRow(i);
            ParticipanteDto dto = new ParticipanteDto();

            dto.setNombre(tableExport.getString("NOMBRE"));
            dto.setApellido(tableExport.getString("APELLIDO"));
            dto.setEdad(tableExport.getString("EDAD"));
            dto.setSexo(tableExport.getString("SEXO"));
            //lista.add(param);
            listaParticipante.add(dto);
        }

        return listaParticipante;


    }

    public ParticipanteDto grabarParticipante(ParticipanteDto dto) throws Exception {
        ParticipanteDto dtoOut = new ParticipanteDto();
        logger.error("grabarParticipante :: " + dto );

        //Obtencion del objeto de conexion (Destination)
        JCoDestination destination = JCoDestinationManager.getDestination("AIPSA_DEST_RFC");

        //Obtencion Repositorio SAP
        JCoRepository repo = destination.getRepository();

        //Obtengo la funcion RFC SAPp
        JCoFunction stfcConnection = repo.getFunction("ZRFC_DEMO_BTP");

        //Obtengo los parametros de importacion de la RFC
        JCoParameterList importParameter = stfcConnection.getImportParameterList();
        JCoStructure importStrParticipante = importParameter.getStructure("I_PARTICIPANTE");
        //llenar operacion
        importParameter.setValue("I_OPERACION", "N");
        //Llenando los datos de la estrucutura.
        importStrParticipante.setValue("NOMBRE", dto.getNombre());
        importStrParticipante.setValue("APELLIDO", dto.getApellido());
        importStrParticipante.setValue("EDAD", dto.getEdad());
        importStrParticipante.setValue("SEXO", dto.getSexo());

        //Ejecutar Funcion
        stfcConnection.execute(destination);

        //Obteniendo parametros de salida de la RFC (export)
        JCoParameterList export = stfcConnection.getExportParameterList();

        //Recuperando tablas Export
        JCoTable tableExport = export.getTable("ET_PARTICIPANTE");


        for (int i = 0; i < tableExport.getNumRows(); i++) {
            tableExport.setRow(i);
            dtoOut.setNombre(tableExport.getString("NOMBRE"));
            dtoOut.setApellido(tableExport.getString("APELLIDO"));
            dtoOut.setEdad(tableExport.getString("EDAD"));
            dtoOut.setSexo(tableExport.getString("SEXO"));
        }

        logger.error("grabarParticipante :: out  " + dtoOut );
        return dtoOut;
    }

}
