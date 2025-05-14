package bo.com.ucb.psymanager.bl;


import bo.com.ucb.psymanager.dao.CaseFileDao;
import bo.com.ucb.psymanager.dao.TreatmentDao;
import bo.com.ucb.psymanager.dto.CaseFileDto;
import bo.com.ucb.psymanager.dto.CreateOrUpdateCaseFileRequestDto;
import bo.com.ucb.psymanager.entities.CaseFile;
import bo.com.ucb.psymanager.entities.Treatment;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * Logica de negocio para el manejo de expedientes de caso (case files) vinculados al cierre de tratamientos
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CaseFileBl {

    private final CaseFileDao caseFileDao;
    private final TreatmentDao treatmentDao;

    /**
     * Registra el expediente final de un tratamiento cerrado.
     * @param dto         Datos de fecha y conclusiones
     * @return DTO del expediente creado
     */
    @Transactional
    public CaseFileDto createOrUpdateCaseFile(CreateOrUpdateCaseFileRequestDto dto) {
        Treatment treatment = treatmentDao.findById(dto.getTreatmentId())
                .orElseThrow(() -> new IllegalArgumentException("Tratamiento no encontrado."));

        CaseFile caseFile = caseFileDao.findByTreatment_TreatmentId(dto.getTreatmentId())
                .orElse(new CaseFile());

        caseFile.setTreatment(treatment);
        caseFile.setDate(dto.getDate());
        caseFile.setSummary(dto.getSummary());
        caseFile.setRecommendations(dto.getRecommendations());

        caseFile = caseFileDao.save(caseFile);
        log.info("Ficha clínica registrada o actualizada para el tratamiento ID={}", dto.getTreatmentId());

        return new CaseFileDto(
                caseFile.getCaseFileId(),
                caseFile.getTreatment().getTreatmentId(),
                caseFile.getDate(),
                caseFile.getSummary(),
                caseFile.getRecommendations()
        );
    }

    /**
     * Metodo para obtener el case file de un tratamiento
     * @param treatmentId
     * @return
     */
    public Optional<CaseFileDto> getCaseFileByTreatment(Long treatmentId) {
        log.info("Consulando case files para tratemiento ID={}", treatmentId);
        return caseFileDao.findByTreatment_TreatmentId(treatmentId)
                .map(this::mapToDto);

    }

    private CaseFileDto mapToDto(CaseFile caseFile){
        return new CaseFileDto(
                caseFile.getCaseFileId(),
                caseFile.getTreatment().getTreatmentId(),
                caseFile.getDate(),
                caseFile.getSummary(),
                caseFile.getRecommendations()
        );
    }


}
