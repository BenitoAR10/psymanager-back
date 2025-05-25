package bo.com.ucb.psymanager.bl;

import bo.com.ucb.psymanager.dao.*;
import bo.com.ucb.psymanager.dto.CompleteProfileRequestDto;
import bo.com.ucb.psymanager.dto.RegisterPatientRequestDto;
import bo.com.ucb.psymanager.entities.*;
import bo.com.ucb.psymanager.exceptions.CiNumberAlreadyExistsException;
import bo.com.ucb.psymanager.exceptions.EmailAlreadyExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Lógica de negocio para registrar usuarios manualmente (sin OAuth),
 * especialmente pacientes, incluyendo la creación del usuario base,
 * la entidad UserPatient y la asignación de roles.
 */
@Service
public class ManualUserRegistrationBl {

    private static final Logger logger = LoggerFactory.getLogger(ManualUserRegistrationBl.class);


    private final UserDao userDao;
    private final UserPatientDao userPatientDao;
    private final RoleDao roleDao;
    private final UserRoleDao userRoleDao;
    private final PasswordEncoder passwordEncoder;
    private final CareerDepartmentDao careerDepartmentDao;
    private final CareerDao careerDao;

    @PersistenceContext
    private EntityManager entityManager;


    @Autowired
    public ManualUserRegistrationBl(
            UserDao userDao,
            UserPatientDao userPatientDao,
            RoleDao roleDao,
            UserRoleDao userRoleDao,
            PasswordEncoder passwordEncoder,
            CareerDepartmentDao careerDepartmentDao,
            CareerDao careerDao
    ) {
        this.userDao = userDao;
        this.userPatientDao = userPatientDao;
        this.roleDao = roleDao;
        this.userRoleDao = userRoleDao;
        this.passwordEncoder = passwordEncoder;
        this.careerDepartmentDao = careerDepartmentDao;
        this.careerDao = careerDao;
    }

    /**
     * Registra a un nuevo paciente en el sistema.
     * - Verifica si el email ya está registrado.
     * - Encripta la contraseña.
     * - Crea el usuario base y el paciente.
     * - Asigna el rol "PATIENT".
     *
     * @param dto DTO con los datos básicos de registro.
     * @throws RuntimeException si el email ya existe o el rol no está configurado.
     */
    @Transactional
    public void registerNewPatientUser(RegisterPatientRequestDto dto) {
        logger.info("Registrando nuevo paciente con email: " + dto.getEmail());

        if (userDao.findByEmail(dto.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("El correo electrónico ya está registrado.");
        }


        String hashedPassword = passwordEncoder.encode(dto.getPassword());

        // Crear entidad User
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(hashedPassword);
        entityManager.persist(user);
        entityManager.flush();
        logger.info("Usuario creado con ID: " + user.getUserId());


        // Crear entidad UserPatient
        UserPatient userPatient = new UserPatient();
        userPatient.setUser(user);
        entityManager.persist(userPatient);
        logger.info("Entidad UserPatient creada para el usuario ID: " + user.getUserId());

        // Asignar rol PATIENT
        Role patientRole = roleDao.findByRole("PATIENT")
                .orElseThrow(() -> {
                    logger.error("Rol 'PATIENT' no encontrado en la base de datos.");
                    return new RuntimeException("Rol 'PATIENT' no configurado");
                });

        userRoleDao.save(new UserRole(user, patientRole));
        logger.info("Rol 'PATIENT' asignado al usuario: " + user.getEmail());
    }

    /**
     * Completa el perfil del paciente autenticado.
     * Guarda información personal y asocia la carrera académica seleccionada.
     *
     * @param email Email del usuario autenticado (desde el token).
     * @param dto   DTO con los datos adicionales del perfil y el ID de carrera seleccionada.
     * @throws RuntimeException si no se encuentra el usuario, paciente o carrera.
     */
    public void completePatientProfile(String email, CompleteProfileRequestDto dto) {
        logger.info("Completando perfil del paciente con email: {}", email);

        // Buscar usuario por email
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("Usuario no encontrado: {}", email);
                    return new RuntimeException("Usuario no encontrado");
                });

        // Validar CI duplicado
        if (user.getCiNumber() == null || !user.getCiNumber().equals(dto.getCiNumber())) {
            boolean exists = userDao.existsByCiNumber(dto.getCiNumber());
            if (exists) {
                logger.warn("El CI ya está registrado: {}", dto.getCiNumber());
                throw new CiNumberAlreadyExistsException("El número de carnet ya está registrado.");
            }
        }

        // Actualizar información personal
        user.setBirthDate(dto.getBirthDate());
        user.setBirthGender(dto.getBirthGender());
        user.setIdentityGender(dto.getIdentityGender());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAddress(dto.getAddress());
        user.setCiNumber(dto.getCiNumber());
        user.setCiComplement(dto.getCiComplement());
        user.setCiExtension(dto.getCiExtension());
        userDao.save(user);
        logger.info("Datos personales actualizados para el usuario: {}", email);

        // Verificar que el usuario tenga una entidad UserPatient asociada
        UserPatient userPatient = userPatientDao.findById(user.getUserId())
                .orElseThrow(() -> {
                    logger.error("Entidad UserPatient no encontrada para: {}", email);
                    return new RuntimeException("Paciente no encontrado");
                });

        // Verificar que exista la carrera con el ID proporcionado
        Career career = careerDao.findById(dto.getCareerId())
                .orElseThrow(() -> {
                    logger.error("Carrera no encontrada con ID: {}", dto.getCareerId());
                    return new RuntimeException("Carrera académica no válida");
                });

        // Asociar carrera con el paciente
        CareerDepartment academicLink = new CareerDepartment();
        academicLink.setUserPatient(userPatient);
        academicLink.setCareer(career);
        careerDepartmentDao.save(academicLink);
        logger.info("Carrera académica asociada correctamente para el paciente: {}", email);
    }

}