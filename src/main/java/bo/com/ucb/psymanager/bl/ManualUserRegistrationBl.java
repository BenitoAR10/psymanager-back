package bo.com.ucb.psymanager.bl;

import bo.com.ucb.psymanager.dao.*;
import bo.com.ucb.psymanager.dto.CompleteProfileRequestDto;
import bo.com.ucb.psymanager.dto.RegisterPatientRequestDto;
import bo.com.ucb.psymanager.entities.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.apache.log4j.Logger;
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

    private static final Logger logger = Logger.getLogger(ManualUserRegistrationBl.class);

    private final UserDao userDao;
    private final UserPatientDao userPatientDao;
    private final RoleDao roleDao;
    private final UserRoleDao userRoleDao;
    private final PasswordEncoder passwordEncoder;
    private final CareerDepartmentDao careerDepartmentDao;

    @PersistenceContext
    private EntityManager entityManager;


    @Autowired
    public ManualUserRegistrationBl(
            UserDao userDao,
            UserPatientDao userPatientDao,
            RoleDao roleDao,
            UserRoleDao userRoleDao,
            PasswordEncoder passwordEncoder,
            CareerDepartmentDao careerDepartmentDao
    ) {
        this.userDao = userDao;
        this.userPatientDao = userPatientDao;
        this.roleDao = roleDao;
        this.userRoleDao = userRoleDao;
        this.passwordEncoder = passwordEncoder;
        this.careerDepartmentDao = careerDepartmentDao;
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
            logger.warn("Intento de registro con email ya existente: " + dto.getEmail());
            throw new RuntimeException("Este correo ya está registrado.");
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
     * Guarda información personal y académica.
     *
     * @param email Email del usuario autenticado (desde el token).
     * @param dto DTO con los campos adicionales del perfil y carrera.
     * @throws RuntimeException si no se encuentra el usuario o su entidad paciente.
     */
    public void completePatientProfile(String email, CompleteProfileRequestDto dto) {
        logger.info("Completando perfil del paciente con email: " + email);

        User user = userDao.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("Usuario no encontrado al completar perfil: " + email);
                    return new RuntimeException("Usuario no encontrado");
                });

        // Actualizar datos del usuario
        user.setBirthDate(dto.getBirthDate());
        user.setBirthGender(dto.getBirthGender());
        user.setIdentityGender(dto.getIdentityGender());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAddress(dto.getAddress());
        user.setCiNumber(dto.getCiNumber());
        user.setCiComplement(dto.getCiComplement());
        user.setCiExtension(dto.getCiExtension());
        userDao.save(user);
        logger.info("Datos personales completados para: " + email);

        // Verificar que exista UserPatient
        UserPatient userPatient = userPatientDao.findById(user.getUserId())
                .orElseThrow(() -> {
                    logger.error("No se encontró UserPatient para usuario: " + email);
                    return new RuntimeException("Paciente no encontrado");
                });

        // Crear carrera académica
        CareerDepartment career = new CareerDepartment();
        career.setCareerName(dto.getCareerName());
        career.setFaculty(dto.getFaculty());
        career.setStatus(dto.getStatus());
        career.setUserPatient(userPatient);
        careerDepartmentDao.save(career);
        logger.info("Carrera académica registrada para el paciente: " + email);
    }
}