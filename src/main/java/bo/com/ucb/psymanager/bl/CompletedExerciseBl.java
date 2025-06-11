package bo.com.ucb.psymanager.bl;

import bo.com.ucb.psymanager.dao.*;
import bo.com.ucb.psymanager.dto.*;
import bo.com.ucb.psymanager.entities.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Lógica de negocio para registrar la finalización de ejercicios de bienestar.
 * Registra el ejercicio completado y actualiza el puntaje acumulado del paciente.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CompletedExerciseBl {
    @PersistenceContext
    private EntityManager entityManager;



    private final CompletedExerciseDao completedExerciseDao;
    private final UserWellnessStatsDao userWellnessStatsDao;
    private final WellnessExerciseDao wellnessExerciseDao;
    private final UserDao userDao;
    private final UserPatientDao userPatientDao;

    @Transactional
    public void completeExercise(String email, CompleteExerciseRequestDto dto) {
        log.info("Registrando ejercicio completado para usuario={} → ejercicioId={}", email, dto.getExerciseId());

        // Obtener paciente autenticado
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        UserPatient patient = userPatientDao.findByUser(user)
                .orElseThrow(() -> new RuntimeException("El usuario no está registrado como paciente"));

        // Validar que el ejercicio exista
        WellnessExercise exercise = wellnessExerciseDao.findById(dto.getExerciseId())
                .orElseThrow(() -> {
                    log.warn("No se encontró el ejercicio con ID {}", dto.getExerciseId());
                    return new IllegalArgumentException("Ejercicio no encontrado");
                });

        // Guardar el registro de ejercicio completado
        CompletedExercise completed = new CompletedExercise();
        completed.setUserPatient(patient);
        completed.setExercise(exercise);
        completed.setCompletedAt(LocalDateTime.now());
        completedExerciseDao.save(completed);

        log.info("Ejercicio completado registrado para pacienteId={}", patient.getUserPatientId());

        // Buscar si ya existen estadísticas
        Optional<UserWellnessStats> existingStatsOpt = userWellnessStatsDao.findById(patient.getUserPatientId());

        if (existingStatsOpt.isPresent()) {
            UserWellnessStats stats = existingStatsOpt.get();
            stats.setTotalPoints(stats.getTotalPoints() + exercise.getPointsReward());
            userWellnessStatsDao.save(stats); // sigue siendo válido
            log.info("Puntaje acumulado actualizado para pacienteId={}. Total actual: {}", patient.getUserPatientId(), stats.getTotalPoints());
        } else {
            log.info("No existen estadísticas previas para el paciente. Se crearán nuevas.");
            UserWellnessStats newStats = new UserWellnessStats();
            newStats.setUserPatientId(patient.getUserPatientId());
            newStats.setUserPatient(patient);
            newStats.setTotalPoints(exercise.getPointsReward());

            entityManager.persist(newStats);
            log.info("Estadísticas creadas con puntaje inicial: {}", newStats.getTotalPoints());
        }
    }
    /**
     * Obtiene las estadísticas semanales y por categoría para un paciente
     * en un período dado.
     *
     * @param patientId ID del paciente
     * @param start     Fecha de inicio (inclusive)
     * @param end       Fecha de fin   (inclusive)
     * @return DTO con total, serie semanal y distribución por categoría
     */
    @Transactional
    public StatisticsResponseDto getWeeklyStatistics(
            Long patientId,
            LocalDateTime start,
            LocalDateTime end
    ) {
        // 1. Validaciones básicas
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("La fecha de inicio debe ser anterior o igual a la fecha de fin");
        }

        // 2. Validar que el paciente existe
        UserPatient patient = userPatientDao.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado: " + patientId));

        // 3. Total de ejercicios completados
        Long total = completedExerciseDao.countTotalByPatientAndPeriod(patientId, start, end);
        TotalCountResponseDto totalDto = new TotalCountResponseDto(total);

        // 4. Serie semanal (mapea la proyección a tu DTO)
        List<WeeklyCountResponseDto> series = completedExerciseDao
                .findWeeklyCountsByPatientAndPeriod(patientId, start, end)
                .stream()
                .map(p -> new WeeklyCountResponseDto(p.getYear(), p.getWeek(), p.getCount()))
                .collect(Collectors.toList());

        // 5. Distribución por categoría
        List<CategoryCountResponseDto> byCategory = completedExerciseDao
                .findCategoryCountsByPatientAndPeriod(patientId, start, end);

        // 6. Armar y devolver el DTO final
        return new StatisticsResponseDto(totalDto, series, byCategory);
    }

    @Transactional
    public DailySeriesResponseDto getDailyStatistics(
            Long patientId,
            LocalDateTime start,
            LocalDateTime end
    ) {
        // validaciones como antes...
        userPatientDao.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado"));

        List<DailyCountResponseDto> daily = completedExerciseDao
                .findDailyCountsByPatientAndPeriod(patientId, start, end)
                .stream()
                .map(p -> new DailyCountResponseDto(p.getDay(), p.getCount()))
                .collect(Collectors.toList());

        Long total = daily.stream().mapToLong(DailyCountResponseDto::getCount).sum();
        return new DailySeriesResponseDto(total, daily);
    }

    @Transactional
    public HourlySeriesResponseDto getHourlyStatistics(
            Long patientId,
            LocalDateTime start,
            LocalDateTime end
    ) {
        // Validaciones...
        userPatientDao.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado"));

        List<HourlyCountResponseDto> series = completedExerciseDao
                .findHourlyCountsByPatientAndPeriod(patientId, start, end)
                .stream()
                .map(p -> new HourlyCountResponseDto(p.getHour(), p.getCount()))
                .collect(Collectors.toList());

        Long total = series.stream().mapToLong(HourlyCountResponseDto::getCount).sum();
        return new HourlySeriesResponseDto(total, series);
    }

}
