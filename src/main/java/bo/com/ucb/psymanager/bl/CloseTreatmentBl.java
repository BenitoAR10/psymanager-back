package bo.com.ucb.psymanager.bl;

import bo.com.ucb.psymanager.dao.CaseFileDao;
import bo.com.ucb.psymanager.dao.CloseTreatmentDao;
import bo.com.ucb.psymanager.dao.TreatmentDao;
import bo.com.ucb.psymanager.dao.TreatmentSessionDao;
import bo.com.ucb.psymanager.dto.CloseTreatmentDto;
import bo.com.ucb.psymanager.dto.CreateCloseTreatmentRequestDto;
import bo.com.ucb.psymanager.entities.CloseTreatment;
import bo.com.ucb.psymanager.entities.Treatment;
import bo.com.ucb.psymanager.entities.TreatmentSession;
import jakarta.transaction.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Logica de negocio para el cierre de tratamientos
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CloseTreatmentBl {

    private final TreatmentDao treatmentDao;
    private final TreatmentSessionDao treatmentSessionDao;
    private final CloseTreatmentDao closeTreatmentDao;
    private final CaseFileDao caseFileDao;

    /**
     * Cierra un tratamiento activo, registrando su motivo y fecha de cierre.
     *
     * @param treatmentId ID del plan de tratamiento a cerrar
     * @param dto         DTO con los datos del cierre (motivo y fecha)
     * @return DTO del cierre registrado
     */
    @Transactional
    public CloseTreatmentDto closeTreatment(Long treatmentId, CreateCloseTreatmentRequestDto dto) {
        log.info("Solicitando cierre del tratamiento ID={}", treatmentId);

        // 1. Verificar existencia del tratamiento
        Treatment treatment = treatmentDao.findById(treatmentId)
                .orElseThrow(() -> new IllegalArgumentException("Tratamiento no encontrado: " + treatmentId));

        // 2. Verificar si ya está cerrado
        boolean yaCerrado = closeTreatmentDao.existsByTreatment_TreatmentId(treatmentId);
        if (yaCerrado) {
            throw new IllegalStateException("El tratamiento ID=" + treatmentId + " ya fue cerrado anteriormente.");
        }

        // 3. Obtener sesiones del tratamiento
        List<TreatmentSession> sesiones = treatmentSessionDao.findByTreatment_TreatmentId(treatmentId);

        // 4. Validar que no haya sesiones futuras (si hay sesiones)
        if (!sesiones.isEmpty()) {
            LocalDate hoy = LocalDate.now();
            boolean hayFuturas = sesiones.stream()
                    .map(s -> s.getScheduleSession().getTherapistScheduled().getDate())
                    .anyMatch(fecha -> fecha.isAfter(hoy));

            if (hayFuturas) {
                throw new IllegalStateException("No se puede cerrar el tratamiento porque existen sesiones futuras asignadas.");
            }
        } else {
            // 5. Si no hay sesiones, validar que exista una ficha clínica (case file)
            boolean tieneCaseFile = caseFileDao.existsByTreatment_TreatmentId(treatmentId);
            if (!tieneCaseFile) {
                throw new IllegalStateException("No se puede cerrar el tratamiento sin sesiones ni ficha clínica registrada.");
            }
        }

        // 6. Crear y guardar el registro de cierre
        CloseTreatment cierre = new CloseTreatment();
        cierre.setTreatment(treatment);
        cierre.setClosingDate(dto.getClosingDate());
        cierre.setReasonForClosure(dto.getReason());

        closeTreatmentDao.save(cierre);

        log.info("Tratamiento ID={} cerrado exitosamente en fecha {}.", treatmentId, dto.getClosingDate());

        // 7. Devolver DTO
        return new CloseTreatmentDto(
                cierre.getClosedTreatmentId(),
                treatmentId,
                cierre.getReasonForClosure(),
                cierre.getClosingDate()
        );
    }

}
