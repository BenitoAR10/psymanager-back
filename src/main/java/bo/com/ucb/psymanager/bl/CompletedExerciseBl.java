package bo.com.ucb.psymanager.bl;

import bo.com.ucb.psymanager.dao.*;
import bo.com.ucb.psymanager.dto.CompleteExerciseRequestDto;
import bo.com.ucb.psymanager.entities.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

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


}
