package bo.com.ucb.psymanager.controllers;

import bo.com.ucb.psymanager.bl.TreatmentBl;

import bo.com.ucb.psymanager.dao.UserDao;
import bo.com.ucb.psymanager.dao.UserPatientDao;
import bo.com.ucb.psymanager.dto.*;
import bo.com.ucb.psymanager.entities.User;
import bo.com.ucb.psymanager.entities.UserPatient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final UserDao userDao;
    private final UserPatientDao userPatientDao;

    /**
     * Crea un nuevo plan de tratamiento para un paciente.
     *
     * @param dto datos de entrada para la creación
     * @return DTO del tratamiento creado
     */
    @PostMapping
    @PreAuthorize("hasRole('THERAPIST')")
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
    @PreAuthorize("hasRole('THERAPIST')")
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
    @PreAuthorize("hasRole('THERAPIST')")
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
    @PreAuthorize("hasRole('THERAPIST')")
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
    @PreAuthorize("hasRole('THERAPIST')")
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
    @PreAuthorize("hasRole('THERAPIST')")
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
    @PreAuthorize("hasRole('THERAPIST')")
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
    @PreAuthorize("hasRole('THERAPIST')")
    public ResponseEntity<ClosedTreatmentDetailDto> getClosedTreatmentDetail(@PathVariable Long treatmentId) {
        log.info("GET /api/treatments/closed/{}/history → detalle histórico", treatmentId);
        ClosedTreatmentDetailDto dto = treatmentBl.getClosedTreatmentDetail(treatmentId);
        return ResponseEntity.ok(dto);
    }

    /**
     * Verifica si el paciente autenticado tiene un tratamiento activo.
     */
    @GetMapping("/my/active-status")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<TreatmentStatusDto> hasActiveTreatment(@AuthenticationPrincipal String email) {
        log.info("Verificar si el paciente tiene tratamiento activo");

        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        UserPatient userPatient = userPatientDao.findById(user.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado"));

        boolean hasTreatment = treatmentBl.hasActiveTreatment(userPatient.getUserPatientId());

        return ResponseEntity.ok(new TreatmentStatusDto(hasTreatment));
    }

}
