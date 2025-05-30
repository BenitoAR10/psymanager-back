package bo.com.ucb.psymanager.bl;

import bo.com.ucb.psymanager.dao.*;
import bo.com.ucb.psymanager.dto.*;
import bo.com.ucb.psymanager.entities.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Lógica de negocio para planes de tratamiento y sus sesiones recurrentes.
 * Controla creación, lectura y resumen de tratamientos activos y cerrados.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TreatmentBl {

    private final TreatmentDao treatmentDao;
    private final CloseTreatmentDao closeTreatmentDao;
    private final ScheduledSessionDao scheduleSessionDao;
    private final UserPatientDao userPatientDao;
    private final UserTherapistDao userTherapistDao;
    private final CaseFileDao caseFileDao;

    /**
     * Crea un nuevo plan de tratamiento (sin sesiones).
     *
     * @param dto datos de creación del plan
     * @return DTO con los datos del plan creado
     */
    @Transactional
    public TreatmentPlanDto createTreatmentPlan(CreateTreatmentPlanRequestDto dto) {
        log.info("Creando TreatmentPlan para paciente={} terapeuta={}", dto.getPatientId(), dto.getTherapistId());

        // Validación de sesión previa aceptada
        boolean hasAccepted = scheduleSessionDao.existsByUserPatient_UserPatientIdAndStateIn(
                dto.getPatientId().intValue(), List.of(SessionState.ACCEPTED)
        );
        if (!hasAccepted) {
            throw new IllegalStateException("El paciente " + dto.getPatientId() + " no tiene ninguna sesión aceptada previa");
        }

        // Verificar existencia de tratamiento activo
        if (treatmentDao.existsByUserPatient_UserPatientIdAndEndDateGreaterThanEqual(dto.getPatientId(), LocalDate.now())) {
            throw new IllegalStateException("El paciente ya tiene un tratamiento activo.");
        }

        // Obtener entidades necesarias
        UserPatient paciente = userPatientDao.findById(dto.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado: " + dto.getPatientId()));
        UserTherapist terapeuta = userTherapistDao.findById(dto.getTherapistId())
                .orElseThrow(() -> new IllegalArgumentException("Terapeuta no encontrado: " + dto.getTherapistId()));

        // Crear y configurar entidad Treatment
        Treatment plan = new Treatment();
        plan.setPreviousTreatment(null);
        plan.setUserPatient(paciente);
        plan.setUserTherapist(terapeuta);
        plan.setReason(dto.getReason());
        plan.setSemester(dto.getSemester());

        LocalDateTime firstDt = dto.getFirstSessionDateTime();
        plan.setStartDate(firstDt.toLocalDate());

        if (dto.isRecurrent()
                && dto.getNumberOfSessions() != null
                && dto.getIntervalWeeks() != null
                && dto.getNumberOfSessions() > 1) {
            LocalDate last = firstDt.plusWeeks((long) (dto.getNumberOfSessions() - 1) * dto.getIntervalWeeks()).toLocalDate();
            plan.setEndDate(last);
        } else {
            plan.setEndDate(firstDt.toLocalDate());
        }

        // Guardar y mapear resultado
        plan = treatmentDao.save(plan);
        log.info("TreatmentPlan creado: id={}", plan.getTreatmentId());

        TreatmentPlanDto result = new TreatmentPlanDto();
        result.setId(plan.getTreatmentId());
        result.setPatientId(paciente.getUserPatientId());
        result.setTherapistId(terapeuta.getUserTherapistId());
        result.setFirstSessionDateTime(firstDt);
        result.setRecurrent(dto.isRecurrent());
        result.setStartDate(plan.getStartDate());

        if (dto.isRecurrent() && dto.getNumberOfSessions() != null && dto.getIntervalWeeks() != null && dto.getNumberOfSessions() > 1) {
            result.setNumberOfSessions(dto.getNumberOfSessions());
            result.setIntervalWeeks(dto.getIntervalWeeks());
        } else {
            result.setNumberOfSessions(1);
            result.setIntervalWeeks(0);
        }

        return result;
    }

    /**
     * Obtiene el tratamiento activo de un paciente, si existe.
     *
     * @param patientId ID del paciente
     * @return tratamiento en forma DTO
     */
    @Transactional
    public Optional<TreatmentPlanDto> getActivePlanByPatient(Long patientId) {
        log.info("Buscando tratamiento activo para paciente={}", patientId);

        return treatmentDao.findByUserPatient_UserPatientId(patientId).stream()
                .filter(t -> !closeTreatmentDao.existsByTreatment_TreatmentId(t.getTreatmentId()))
                .findFirst()
                .map(this::mapToDto);
    }

    /**
     * Obtiene todos los planes de tratamiento asignados a un terapeuta.
     */
    @Transactional
    public List<TreatmentPlanDto> getPlansByTherapist(Long therapistId) {
        log.info("Obteniendo planes de tratamiento para terapeuta={}", therapistId);
        return treatmentDao.findByUserTherapist_UserTherapistId(therapistId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    /**
     * Mapea una entidad Treatment a su DTO correspondiente.
     */
    private TreatmentPlanDto mapToDto(Treatment plan) {
        TreatmentPlanDto dto = new TreatmentPlanDto();
        dto.setId(plan.getTreatmentId());
        dto.setPatientId(plan.getUserPatient().getUserPatientId());
        dto.setTherapistId(plan.getUserTherapist().getUserTherapistId());
        dto.setFirstSessionDateTime(plan.getStartDate().atStartOfDay());
        dto.setRecurrent(plan.getEndDate().isAfter(plan.getStartDate()));
        dto.setStartDate(plan.getStartDate());
        dto.setNumberOfSessions(plan.getSessions().size());

        long weeksBetween = ChronoUnit.WEEKS.between(plan.getStartDate(), plan.getEndDate());
        dto.setIntervalWeeks(dto.getNumberOfSessions() > 1
                ? (int) (weeksBetween / (dto.getNumberOfSessions() - 1))
                : 0);

        return dto;
    }

    /**
     * Obtiene los estudiantes actualmente en tratamiento activo con un terapeuta.
     *
     * @param therapistId ID del terapeuta
     * @return lista de estudiantes con tratamiento activo
     */
    @Transactional
    public List<ActiveTreatmentStudentDto> getActivePatientsByTherapist(Long therapistId) {
        log.info("Buscando estudiantes en tratamiento activo para terapeuta ID={}", therapistId);

        LocalDate today = LocalDate.now();
        List<Treatment> activeTreatments = treatmentDao
                .findByUserTherapist_UserTherapistIdAndEndDateGreaterThanEqual(therapistId, today);

        return activeTreatments.stream()
                .map(t -> {
                    UserPatient patient = t.getUserPatient();
                    String fullName = patient.getUser().getFirstName() + " " + patient.getUser().getLastName();

                    List<TreatmentSession> sessions = t.getSessions();
                    int total = sessions.size();
                    int completed = (int) sessions.stream().filter(TreatmentSession::getCompleted).count();

                    return new ActiveTreatmentStudentDto(
                            t.getTreatmentId(),
                            patient.getUserPatientId(),
                            fullName,
                            t.getStartDate(),
                            t.getEndDate(),
                            total,
                            completed
                    );
                })
                .toList();
    }

    /**
     * Retorna todos los tratamientos activos asociados a un terapeuta.
     *
     * @param therapistId ID del terapeuta
     * @return lista de pacientes asignados con sus datos
     */
    @Transactional
    public List<AssignedPatientDto> getAllPatientsByTherapist(Long therapistId) {
        log.info("Obteniendo solo tratamientos activos para terapeuta ID={}", therapistId);

        List<Treatment> allPlans = treatmentDao.findByUserTherapist_UserTherapistId(therapistId);
        List<Treatment> activePlans = allPlans.stream()
                .filter(t -> !closeTreatmentDao.existsByTreatment_TreatmentId(t.getTreatmentId()))
                .toList();

        LocalDate today = LocalDate.now();

        return activePlans.stream().map(t -> {
            UserPatient userPatient = t.getUserPatient();
            String name = userPatient.getUser().getFirstName() + " " + userPatient.getUser().getLastName();
            List<TreatmentSession> sessions = t.getSessions();
            int completed = (int) sessions.stream().filter(TreatmentSession::getCompleted).count();

            // Buscar la carrera (puedes definir un método en el DAO si lo prefieres)
            String careerName = userPatient.getCareerDepartments().stream()
                    .findFirst()
                    .map(cd -> cd.getCareer().getCareerName())
                    .orElse("Sin carrera");

            return new AssignedPatientDto(
                    t.getTreatmentId(),
                    userPatient.getUserPatientId(),
                    name,
                    t.getStartDate(),
                    t.getEndDate(),
                    sessions.size(),
                    completed,
                    !t.getEndDate().isBefore(today),
                    careerName
            );
        }).toList();
    }


    /**
     * Obtiene el detalle completo de un tratamiento activo.
     *
     * @param treatmentId ID del tratamiento
     * @return DTO con todos los datos y sesiones
     */
    @Transactional
    public TreatmentDetailDto getTreatmentDetail(Long treatmentId) {
        log.info("Obteniendo detalle del tratamiento ID={}", treatmentId);

        Treatment treatment = treatmentDao.findById(treatmentId)
                .orElseThrow(() -> new IllegalArgumentException("Tratamiento no encontrado: " + treatmentId));

        if (closeTreatmentDao.existsByTreatment_TreatmentId(treatmentId)) {
            throw new IllegalStateException("El tratamiento ya fue cerrado y no puede ser modificado.");
        }

        List<TreatmentSessionDetailDto> sessionDtos = treatment.getSessions().stream()
                .sorted(Comparator.comparing(TreatmentSession::getSessionOrder))
                .map(session -> {
                    ScheduleSession schedule = session.getScheduleSession();
                    TherapistScheduled scheduled = schedule.getTherapistScheduled();

                    return new TreatmentSessionDetailDto(
                            session.getTreatmentSessionId(),
                            scheduled.getDate(),
                            scheduled.getStartTime(),
                            scheduled.getEndTime(),
                            schedule.getState().name(),
                            session.getCompleted(),
                            Optional.ofNullable(session.getNotes())
                                    .map(SessionNote::getSessionSummary)
                                    .orElse(null),
                            session.getSessionOrder()
                    );
                })
                .toList();

        return new TreatmentDetailDto(
                treatment.getTreatmentId(),
                treatment.getStartDate(),
                treatment.getEndDate(),
                treatment.getReason(),
                treatment.getSemester(),
                sessionDtos
        );
    }

    /**
     * Lista todos los tratamientos cerrados para un terapeuta.
     *
     * @param therapistId ID del terapeuta
     * @return lista de resúmenes de tratamientos cerrados
     */
    @Transactional
    public List<ClosedTreatmentSummaryDto> getClosedTreatmentsByTherapist(Long therapistId) {
        log.info("Listando tratamientos cerrados para terapeuta ID={}", therapistId);

        List<CloseTreatment> cierres = closeTreatmentDao
                .findByTreatment_UserTherapist_UserTherapistId(therapistId);

        return cierres.stream()
                .map(cierre -> {
                    Treatment treatment = cierre.getTreatment();
                    UserPatient patient = treatment.getUserPatient();
                    String fullName = patient.getUser().getFirstName() + " " + patient.getUser().getLastName();

                    int completed = (int) treatment.getSessions().stream()
                            .filter(TreatmentSession::getCompleted).count();

                    return new ClosedTreatmentSummaryDto(
                            treatment.getTreatmentId(),
                            fullName,
                            treatment.getStartDate(),
                            cierre.getClosingDate(),
                            cierre.getReasonForClosure(),
                            completed
                    );
                })
                .toList();
    }

    /**
     * Retorna el historial completo de un tratamiento cerrado, incluyendo notas y ficha clínica.
     *
     * @param treatmentId ID del tratamiento
     * @return detalle completo en DTO
     */
    @Transactional
    public ClosedTreatmentDetailDto getClosedTreatmentDetail(Long treatmentId) {
        log.info("Consultando historial de tratamiento cerrado ID={}", treatmentId);

        Treatment treatment = treatmentDao.findById(treatmentId)
                .orElseThrow(() -> new IllegalArgumentException("Tratamiento no encontrado: " + treatmentId));

        CloseTreatment close = closeTreatmentDao.findByTreatment_TreatmentId(treatmentId)
                .orElseThrow(() -> new IllegalArgumentException("El tratamiento no está cerrado: " + treatmentId));

        String studentName = treatment.getUserPatient().getUser().getFirstName() + " " +
                treatment.getUserPatient().getUser().getLastName();

        ClosedTreatmentDetailDto dto = new ClosedTreatmentDetailDto();
        dto.setTreatmentId(treatmentId);
        dto.setStudentName(studentName);
        dto.setSemester(treatment.getSemester());
        dto.setReason(treatment.getReason());
        dto.setStartDate(treatment.getStartDate());
        dto.setEndDate(treatment.getEndDate());
        dto.setClosureReason(close.getReasonForClosure());
        dto.setClosingDate(close.getClosingDate());

        // Adjuntar ficha clínica
        caseFileDao.findByTreatment_TreatmentId(treatmentId).ifPresent(cf ->
                dto.setCaseFile(new CaseFileDto(
                        cf.getCaseFileId(),
                        treatmentId,
                        cf.getDate(),
                        cf.getSummary(),
                        cf.getRecommendations()
                ))
        );

        // Adjuntar notas de sesión
        List<SessionNoteSummaryDto> notes = treatment.getSessions().stream()
                .filter(ts -> ts.getNotes() != null)
                .sorted(Comparator.comparing(ts -> ts.getScheduleSession().getTherapistScheduled().getDate()))
                .map(ts -> {
                    SessionNote note = ts.getNotes();
                    return new SessionNoteSummaryDto(
                            ts.getScheduleSession().getTherapistScheduled().getDate(),
                            note.getTopicAddressed(),
                            note.getSessionSummary(),
                            note.getRelevantObservations(),
                            note.getNextTopic()
                    );
                })
                .toList();

        dto.setSessionNotes(notes);

        log.info("Historial de tratamiento ID={} cargado correctamente con {} notas", treatmentId, notes.size());
        return dto;
    }

    @Transactional
    public boolean hasActiveTreatment(Long patientId) {
        log.info("Verificando si el paciente ID={} tiene tratamiento activo", patientId);
        return treatmentDao.findByUserPatient_UserPatientId(patientId).stream()
                .anyMatch(t -> !closeTreatmentDao.existsByTreatment_TreatmentId(t.getTreatmentId()));
    }


}


