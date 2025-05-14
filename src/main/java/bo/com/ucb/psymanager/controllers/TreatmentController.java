package bo.com.ucb.psymanager.controllers;

import bo.com.ucb.psymanager.bl.TreatmentBl;
import bo.com.ucb.psymanager.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar planes de tratamiento y su historial.
 */
@RestController
@RequestMapping("/api/treatments")
@RequiredArgsConstructor
@Slf4j
public class TreatmentController {

    private final TreatmentBl treatmentBl;

    /**
     * Crea un nuevo plan de tratamiento para un paciente.
     *
     * @param dto datos de entrada para la creación
     * @return DTO del tratamiento creado
     */
    @PostMapping
    public ResponseEntity<TreatmentPlanDto> createTreatmentPlan(@RequestBody CreateTreatmentPlanRequestDto dto) {
        log.info("POST /api/treatments → crear tratamiento para paciente={} terapeuta={}", dto.getPatientId(), dto.getTherapistId());
        TreatmentPlanDto created = treatmentBl.createTreatmentPlan(dto);
        return ResponseEntity.ok(created);
    }

    /**
     * Obtiene el plan de tratamiento activo de un paciente, si existe.
     *
     * @param patientId ID del paciente
     * @return tratamiento activo o 204 si no existe
     */
    @GetMapping("/patient/{patientId}/active")
    public ResponseEntity<TreatmentPlanDto> getActivePlanByPatient(@PathVariable Long patientId) {
        log.info("GET /api/treatments/patient/{}/active → tratamiento activo", patientId);
        return treatmentBl.getActivePlanByPatient(patientId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    /**
     * Obtiene todos los planes de tratamiento asociados a un terapeuta.
     *
     * @param therapistId ID del terapeuta
     * @return lista de planes de tratamiento
     */
    @GetMapping("/therapist/{therapistId}")
    public ResponseEntity<List<TreatmentPlanDto>> getPlansByTherapist(@PathVariable Long therapistId) {
        log.info("GET /api/treatments/therapist/{} → todos los planes", therapistId);
        List<TreatmentPlanDto> plans = treatmentBl.getPlansByTherapist(therapistId);
        return ResponseEntity.ok(plans);
    }

    /**
     * Obtiene los pacientes actualmente en tratamiento con un terapeuta.
     *
     * @param therapistId ID del terapeuta
     * @return lista de pacientes con tratamiento activo
     */
    @GetMapping("/therapist/{therapistId}/active")
    public ResponseEntity<List<ActiveTreatmentStudentDto>> getActivePatientsByTherapist(@PathVariable Long therapistId) {
        log.info("GET /api/treatments/therapist/{}/active → estudiantes activos", therapistId);
        List<ActiveTreatmentStudentDto> students = treatmentBl.getActivePatientsByTherapist(therapistId);
        return ResponseEntity.ok(students);
    }

    /**
     * Obtiene todos los tratamientos activos asociados a un terapeuta.
     *
     * @param therapistId ID del terapeuta
     * @return lista de pacientes asignados
     */
    @GetMapping("/therapist/{therapistId}/patients")
    public ResponseEntity<List<AssignedPatientDto>> getAllPatientsByTherapist(@PathVariable Long therapistId) {
        log.info("GET /api/treatments/therapist/{}/patients → tratamientos activos", therapistId);
        List<AssignedPatientDto> students = treatmentBl.getAllPatientsByTherapist(therapistId);
        return ResponseEntity.ok(students);
    }

    /**
     * Obtiene los detalles completos de un tratamiento activo.
     *
     * @param treatmentId ID del tratamiento
     * @return DTO con sesiones y metadata
     */
    @GetMapping("/{treatmentId}")
    public ResponseEntity<TreatmentDetailDto> getTreatmentDetail(@PathVariable Long treatmentId) {
        log.info("GET /api/treatments/{} → detalle de tratamiento", treatmentId);
        TreatmentDetailDto dto = treatmentBl.getTreatmentDetail(treatmentId);
        return ResponseEntity.ok(dto);
    }

    /**
     * Lista los tratamientos cerrados realizados por un terapeuta.
     *
     * @param therapistId ID del terapeuta
     * @return lista de tratamientos cerrados
     */
    @GetMapping("/therapist/{therapistId}/closed")
    public ResponseEntity<List<ClosedTreatmentSummaryDto>> getClosedTreatmentsByTherapist(@PathVariable Long therapistId) {
        log.info("GET /api/treatments/therapist/{}/closed → resumen de tratamientos cerrados", therapistId);
        List<ClosedTreatmentSummaryDto> summaries = treatmentBl.getClosedTreatmentsByTherapist(therapistId);
        return ResponseEntity.ok(summaries);
    }

    /**
     * Devuelve los detalles históricos completos de un tratamiento cerrado.
     *
     * @param treatmentId ID del tratamiento cerrado
     * @return historial detallado con notas y ficha clínica
     */
    @GetMapping("/closed/{treatmentId}/history")
    public ResponseEntity<ClosedTreatmentDetailDto> getClosedTreatmentDetail(@PathVariable Long treatmentId) {
        log.info("GET /api/treatments/closed/{}/history → detalle histórico", treatmentId);
        ClosedTreatmentDetailDto dto = treatmentBl.getClosedTreatmentDetail(treatmentId);
        return ResponseEntity.ok(dto);
    }
}
