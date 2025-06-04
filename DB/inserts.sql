INSERT INTO ps_permission (permission_name, description) VALUES
                                                             ('CREATE_APPOINTMENT', 'Permite agendar nuevas citas'),
                                                             ('CANCEL_APPOINTMENT', 'Permite cancelar citas programadas'),
                                                             ('VIEW_PATIENT_HISTORY', 'Permite visualizar historial clínico del paciente'),
                                                             ('COMPLETE_EXERCISE', 'Permite ejecutar ejercicios del kit de ayuda'),
                                                             ('MANAGE_SCHEDULE', 'Permite gestionar disponibilidad de horarios'),
                                                             ('VIEW_MY_APPOINTMENTS', 'Permite ver las citas propias del usuario'),
                                                             ('VIEW_ASSIGNED_PATIENTS', 'Permite ver los pacientes asignados al terapeuta'),
                                                             ('CREATE_TREATMENT_PLAN', 'Permite crear nuevos planes de tratamiento'),
                                                             ('ADD_EXERCISE_RESOURCE', 'Permite subir ejercicios y recursos al sistema'),
                                                             ('EDIT_PROFILE', 'Permite que el usuario edite su perfil personal'),
                                                             ('MANAGE_USERS', 'Permite administrar usuarios del sistema');

INSERT INTO ps_role (role, description) VALUES
                                            ('ADMIN', 'Administrador del sistema'),
                                            ('THERAPIST', 'Terapeuta responsable de atención psicológica'),
                                            ('PATIENT', 'Estudiante que accede a servicios psicológicos');

-- Asignación para PATIENT (role_id = 3)
INSERT INTO ps_role_permission (role_id, permission_id) VALUES
                                                            (3, 1),
                                                            (3, 2),
                                                            (3, 4),
                                                            (3, 6),
                                                            (3, 10);

-- Asignación para THERAPIST (role_id = 2)
INSERT INTO ps_role_permission (role_id, permission_id) VALUES
                                                            (2, 3),
                                                            (2, 5),
                                                            (2, 7),
                                                            (2, 8),
                                                            (2, 9),
                                                            (2, 10);

-- Asignación para ADMIN (role_id = 1)
INSERT INTO ps_role_permission (role_id, permission_id) VALUES
                                                            (1, 1),
                                                            (1, 2),
                                                            (1, 3),
                                                            (1, 4),
                                                            (1, 5),
                                                            (1, 6),
                                                            (1, 7),
                                                            (1, 8),
                                                            (1, 9),
                                                            (1, 10),
                                                            (1, 11);

INSERT INTO ps_specialty (specialty_name, description) VALUES
                                                           ('Psicoterapia Cognitivo-Conductual', 'Intervenciones centradas en el pensamiento y la conducta'),
                                                           ('Terapia Familiar Sistémica', 'Intervenciones orientadas a dinámicas familiares'),
                                                           ('Terapia Humanista', 'Enfoque centrado en la persona y su potencial'),
                                                           ('Psicoanálisis', 'Intervenciones basadas en el análisis del inconsciente'),
                                                           ('Terapia de Pareja', 'Atención a conflictos relacionales en vínculos afectivos'),
                                                           ('Terapia Breve Estratégica', 'Solución de problemas en pocas sesiones'),
                                                           ('Psicología Educativa', 'Apoyo en procesos de aprendizaje y adaptación académica'),
                                                           ('Psicología Clínica Infantil', 'Atención psicológica especializada en niños y adolescentes'),
                                                           ('Psicología de la Salud', 'Abordaje del bienestar integral en contexto sanitario'),
                                                           ('Intervención en Crisis', 'Apoyo inmediato ante eventos altamente estresantes');


INSERT INTO ps_career (career_name, faculty, status) VALUES
                                                         ('Administración de Empresas', 'Facultad de Ciencias Económicas y Financieras', 'Activo'),
                                                         ('Administración Turística', 'Facultad de Ciencias Económicas y Financieras', 'Activo'),
                                                         ('Contaduría Pública', 'Facultad de Ciencias Económicas y Financieras', 'Activo'),
                                                         ('Economía', 'Facultad de Ciencias Económicas y Financieras', 'Activo'),
                                                         ('Economía e Inteligencia de Negocios', 'Facultad de Ciencias Económicas y Financieras', 'Activo'),
                                                         ('Ingeniería Comercial', 'Facultad de Ciencias Económicas y Financieras', 'Activo'),
                                                         ('Ingeniería en Innovación Empresarial', 'Facultad de Ciencias Económicas y Financieras', 'Activo'),
                                                         ('Marketing y Medios Digitales', 'Facultad de Ciencias Económicas y Financieras', 'Activo'),
                                                         ('Ingeniería Ambiental', 'Facultad de Ingeniería', 'Activo'),
                                                         ('Ingeniería Biomédica', 'Facultad de Ingeniería', 'Activo'),
                                                         ('Ingeniería Bioquímica y de Bioprocesos', 'Facultad de Ingeniería', 'Activo'),
                                                         ('Ingeniería Civil', 'Facultad de Ingeniería', 'Activo'),
                                                         ('Ingeniería de Sistemas', 'Facultad de Ingeniería', 'Activo'),
                                                         ('Ingeniería en Logística de Cadenas de Suministro', 'Facultad de Ingeniería', 'Activo'),
                                                         ('Ingeniería en Multimedia e Interactividad Digital', 'Facultad de Ingeniería', 'Activo'),
                                                         ('Ingeniería Industrial', 'Facultad de Ingeniería', 'Activo'),
                                                         ('Ingeniería Química', 'Facultad de Ingeniería', 'Activo'),
                                                         ('Ingeniería Mecatrónica', 'Facultad de Ingeniería', 'Activo'),
                                                         ('Ingeniería en Telecomunicaciones', 'Facultad de Ingeniería', 'Activo'),
                                                         ('Comunicación Social', 'Facultad de Ciencias Sociales y Humanas', 'Activo'),
                                                         ('Comunicación Digital Multimedial', 'Facultad de Ciencias Sociales y Humanas', 'Activo'),
                                                         ('Psicología', 'Facultad de Ciencias Sociales y Humanas', 'Activo'),
                                                         ('Psicopedagogía', 'Facultad de Ciencias Sociales y Humanas', 'Activo'),
                                                         ('Ciencias Políticas y Relaciones Internacionales', 'Facultad de Derecho, Ciencias Políticas y Relaciones Internacionales', 'Activo'),
                                                         ('Derecho', 'Facultad de Derecho, Ciencias Políticas y Relaciones Internacionales', 'Activo'),
                                                         ('Arquitectura', 'Departamento de Arquitectura y Diseño Gráfico', 'Activo'),
                                                         ('Diseño Digital', 'Departamento de Arquitectura y Diseño Gráfico', 'Activo'),
                                                         ('Diseño Gráfico y Comunicación Visual', 'Departamento de Arquitectura y Diseño Gráfico', 'Activo'),
                                                         ('Medicina', 'Salud', 'Activo'),
                                                         ('Nutrición Clínica y Dietética', 'Salud', 'Activo');


INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-21', '09:00:00', '10:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-21', '12:00:00', '13:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-21', '10:30:00', '11:30:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-21', '14:00:00', '15:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-28', '14:00:00', '15:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-28', '12:00:00', '13:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-28', '10:30:00', '11:30:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-28', '09:00:00', '10:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-06-02', '12:00:00', '13:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-06-02', '09:00:00', '10:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-06-02', '14:00:00', '15:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-06-02', '10:30:00', '11:30:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-06', '12:00:00', '13:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-06', '09:00:00', '10:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-06', '10:30:00', '11:30:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-06', '14:00:00', '15:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-22', '12:00:00', '13:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-22', '14:00:00', '15:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-22', '09:00:00', '10:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-22', '10:30:00', '11:30:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-04-30', '09:00:00', '10:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-04-30', '14:00:00', '15:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-04-30', '10:30:00', '11:30:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-04-30', '12:00:00', '13:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-23', '09:00:00', '10:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-23', '12:00:00', '13:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-23', '14:00:00', '15:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-23', '10:30:00', '11:30:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-04-28', '10:30:00', '11:30:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-04-28', '12:00:00', '13:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-04-28', '09:00:00', '10:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-04-28', '14:00:00', '15:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-02', '14:00:00', '15:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-02', '09:00:00', '10:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-02', '12:00:00', '13:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-02', '10:30:00', '11:30:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-13', '09:00:00', '10:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-13', '14:00:00', '15:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-13', '10:30:00', '11:30:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-13', '12:00:00', '13:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-19', '14:00:00', '15:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-19', '12:00:00', '13:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-19', '10:30:00', '11:30:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-19', '09:00:00', '10:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-27', '14:00:00', '15:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-27', '09:00:00', '10:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-27', '10:30:00', '11:30:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-27', '12:00:00', '13:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-04-29', '14:00:00', '15:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-04-29', '10:30:00', '11:30:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-04-29', '09:00:00', '10:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-04-29', '12:00:00', '13:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-08', '10:30:00', '11:30:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-08', '12:00:00', '13:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-08', '14:00:00', '15:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-08', '09:00:00', '10:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-30', '09:00:00', '10:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-30', '12:00:00', '13:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-30', '10:30:00', '11:30:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-30', '14:00:00', '15:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-04-25', '10:30:00', '11:30:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-04-25', '09:00:00', '10:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-04-25', '14:00:00', '15:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-04-25', '12:00:00', '13:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-16', '10:30:00', '11:30:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-16', '12:00:00', '13:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-16', '09:00:00', '10:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-05-16', '14:00:00', '15:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-04-21', '12:00:00', '13:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-04-21', '09:00:00', '10:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-06-10', '09:00:00', '10:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-06-10', '12:00:00', '13:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-06-10', '10:30:00', '11:30:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-06-10', '14:00:00', '15:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-06-05', '12:00:00', '13:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-06-05', '14:00:00', '15:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-06-05', '09:00:00', '10:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-06-05', '10:30:00', '11:30:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-06-16', '09:00:00', '10:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-06-16', '12:00:00', '13:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-06-16', '14:00:00', '15:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-06-16', '10:30:00', '11:30:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-06-04', '10:30:00', '11:30:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-06-04', '12:00:00', '13:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-06-04', '14:00:00', '15:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-06-04', '09:00:00', '10:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-06-12', '09:00:00', '10:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-06-12', '10:30:00', '11:30:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-06-12', '14:00:00', '15:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-06-12', '12:00:00', '13:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-06-03', '10:30:00', '11:30:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-06-03', '09:00:00', '10:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-06-03', '14:00:00', '15:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-06-03', '12:00:00', '13:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-06-06', '14:00:00', '15:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-06-06', '10:30:00', '11:30:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-06-06', '12:00:00', '13:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-06-06', '09:00:00', '10:00:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-06-13', '10:30:00', '11:30:00');
INSERT INTO ps_therapist_scheduled (user_therapist_id, date, start_time, end_time) VALUES (1, '2025-06-13', '12:00:00', '13:00:00');

INSERT INTO ps_user (
    user_id, address, birth_date, birth_gender, ci_complement, ci_extension, ci_number,
    email, first_name, identity_gender, last_name, password, phone_number
) VALUES (
             6, 'Via de Trinidad Baños', '2005-01-24', 'male',
             '', 'LP', '8651259',
             'nazario.donoso@ucb.edu.bo', 'Nazario', 'male', 'Donoso',
             '$2a$10$123456789012345678901u123456789012345678901u12345678', '+34602 607 268'
         );
INSERT INTO ps_user_patient (user_patient_id) VALUES (6);
INSERT INTO ps_user (
    user_id, address, birth_date, birth_gender, ci_complement, ci_extension, ci_number,
    email, first_name, identity_gender, last_name, password, phone_number
) VALUES (
             7, 'Alameda Gisela Olmedo', '2005-06-10', 'male',
             '', 'LP', '6422320',
             'eugenio.caparrós@ucb.edu.bo', 'Eugenio', 'male', 'Caparrós',
             '$2a$10$123456789012345678901u123456789012345678901u12345678', '+34630331830'
         );
INSERT INTO ps_user_patient (user_patient_id) VALUES (7);
INSERT INTO ps_user (
    user_id, address, birth_date, birth_gender, ci_complement, ci_extension, ci_number,
    email, first_name, identity_gender, last_name, password, phone_number
) VALUES (
             8, 'Alameda Josefa Cabanillas', '2004-11-13', 'male',
             '', 'LP', '6789519',
             'lucio.vallés@ucb.edu.bo', 'Lucio', 'male', 'Vallés',
             '$2a$10$123456789012345678901u123456789012345678901u12345678', '+34 891 217 512'
         );
INSERT INTO ps_user_patient (user_patient_id) VALUES (8);
INSERT INTO ps_user (
    user_id, address, birth_date, birth_gender, ci_complement, ci_extension, ci_number,
    email, first_name, identity_gender, last_name, password, phone_number
) VALUES (
             9, 'Glorieta de Rafa Vicente', '2003-12-17', 'male',
             '', 'LP', '7248591',
             'porfirio.poza@ucb.edu.bo', 'Porfirio', 'male', 'Poza',
             '$2a$10$123456789012345678901u123456789012345678901u12345678', '+34 952 99 11 45'
         );
INSERT INTO ps_user_patient (user_patient_id) VALUES (9);
INSERT INTO ps_user (
    user_id, address, birth_date, birth_gender, ci_complement, ci_extension, ci_number,
    email, first_name, identity_gender, last_name, password, phone_number
) VALUES (
             10, 'Glorieta de Modesta Seco', '1999-09-19', 'male',
             '', 'LP', '9544109',
             'jonatan.amor@ucb.edu.bo', 'Jonatan', 'male', 'Amor',
             '$2a$10$123456789012345678901u123456789012345678901u12345678', '+34985 91 01 95'
         );
INSERT INTO ps_user_patient (user_patient_id) VALUES (10);
INSERT INTO ps_user (
    user_id, address, birth_date, birth_gender, ci_complement, ci_extension, ci_number,
    email, first_name, identity_gender, last_name, password, phone_number
) VALUES (
             11, 'Cuesta de Florinda Manzanares', '1999-11-22', 'male',
             '', 'LP', '9732059',
             'asdrubal.rodríguez@ucb.edu.bo', 'Asdrubal', 'male', 'Rodríguez',
             '$2a$10$123456789012345678901u123456789012345678901u12345678', '+34739 25 98 56'
         );
INSERT INTO ps_user_patient (user_patient_id) VALUES (11);
INSERT INTO ps_user (
    user_id, address, birth_date, birth_gender, ci_complement, ci_extension, ci_number,
    email, first_name, identity_gender, last_name, password, phone_number
) VALUES (
             12, 'Pasaje de Rosendo Cañizares', '2001-07-11', 'male',
             '', 'LP', '8905913',
             'samu.seco@ucb.edu.bo', 'Samu', 'male', 'Seco',
             '$2a$10$123456789012345678901u123456789012345678901u12345678', '+34 735 84 35 28'
         );
INSERT INTO ps_user_patient (user_patient_id) VALUES (12);

INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (29, 6, 'ACCEPTED');
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (52, 6, 'ACCEPTED');
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (23, 6, 'ACCEPTED');
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (44, 6, 'ACCEPTED');
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (1, 6, 'ACCEPTED');
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (19, 6, 'ACCEPTED');
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (32, 7, 'ACCEPTED');
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (51, 7, 'ACCEPTED');
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (24, 7, 'ACCEPTED');
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (41, 7, 'ACCEPTED');
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (2, 7, 'ACCEPTED');
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (18, 7, 'ACCEPTED');
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (31, 8, 'ACCEPTED');
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (50, 8, 'ACCEPTED');
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (21, 8, 'ACCEPTED');
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (43, 8, 'ACCEPTED');
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (3, 8, 'ACCEPTED');
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (20, 8, 'ACCEPTED');
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (30, 9, 'ACCEPTED');
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (49, 9, 'ACCEPTED');
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (22, 9, 'ACCEPTED');
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (42, 9, 'ACCEPTED');
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (4, 9, 'ACCEPTED');
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (17, 9, 'ACCEPTED');


INSERT INTO ps_treatment (user_therapist_id, user_patient_id, start_date, end_date, reason, semester)
VALUES (1, 6, '2025-04-28', '2025-05-22', 'Ansiedad relacionada con exámenes finales y carga académica', '1-2025');

INSERT INTO ps_treatment (user_therapist_id, user_patient_id, start_date, end_date, reason, semester)
VALUES (1, 7, '2025-04-28', '2025-05-22', 'Desmotivación y bajo rendimiento académico en el semestre', '1-2025');

INSERT INTO ps_treatment (user_therapist_id, user_patient_id, start_date, end_date, reason, semester)
VALUES (1, 8, '2025-04-28', '2025-05-22', 'Problemas de adaptación al entorno universitario y estrés social', '1-2025');

INSERT INTO ps_treatment (user_therapist_id, user_patient_id, start_date, end_date, reason, semester)
VALUES (1, 9, '2025-04-28', '2025-05-22', 'Dificultades emocionales derivadas de conflictos familiares durante el semestre', '1-2025');


INSERT INTO ps_treatment_session (treatment_id, schedule_session_id, session_order, completed) VALUES (1, 1, 1, true);
INSERT INTO ps_treatment_session (treatment_id, schedule_session_id, session_order, completed) VALUES (1, 2, 2, true);
INSERT INTO ps_treatment_session (treatment_id, schedule_session_id, session_order, completed) VALUES (1, 3, 3, true);
INSERT INTO ps_treatment_session (treatment_id, schedule_session_id, session_order, completed) VALUES (1, 4, 4, true);
INSERT INTO ps_treatment_session (treatment_id, schedule_session_id, session_order, completed) VALUES (1, 5, 5, true);
INSERT INTO ps_treatment_session (treatment_id, schedule_session_id, session_order, completed) VALUES (1, 6, 6, true);
INSERT INTO ps_treatment_session (treatment_id, schedule_session_id, session_order, completed) VALUES (2, 7, 1, true);
INSERT INTO ps_treatment_session (treatment_id, schedule_session_id, session_order, completed) VALUES (2, 8, 2, true);
INSERT INTO ps_treatment_session (treatment_id, schedule_session_id, session_order, completed) VALUES (2, 9, 3, true);
INSERT INTO ps_treatment_session (treatment_id, schedule_session_id, session_order, completed) VALUES (2, 10, 4, true);
INSERT INTO ps_treatment_session (treatment_id, schedule_session_id, session_order, completed) VALUES (2, 11, 5, true);
INSERT INTO ps_treatment_session (treatment_id, schedule_session_id, session_order, completed) VALUES (2, 12, 6, true);
INSERT INTO ps_treatment_session (treatment_id, schedule_session_id, session_order, completed) VALUES (3, 13, 1, true);
INSERT INTO ps_treatment_session (treatment_id, schedule_session_id, session_order, completed) VALUES (3, 14, 2, true);
INSERT INTO ps_treatment_session (treatment_id, schedule_session_id, session_order, completed) VALUES (3, 15, 3, true);
INSERT INTO ps_treatment_session (treatment_id, schedule_session_id, session_order, completed) VALUES (3, 16, 4, true);
INSERT INTO ps_treatment_session (treatment_id, schedule_session_id, session_order, completed) VALUES (3, 17, 5, true);
INSERT INTO ps_treatment_session (treatment_id, schedule_session_id, session_order, completed) VALUES (3, 18, 6, true);
INSERT INTO ps_treatment_session (treatment_id, schedule_session_id, session_order, completed) VALUES (4, 19, 1, true);
INSERT INTO ps_treatment_session (treatment_id, schedule_session_id, session_order, completed) VALUES (4, 20, 2, true);
INSERT INTO ps_treatment_session (treatment_id, schedule_session_id, session_order, completed) VALUES (4, 21, 3, true);
INSERT INTO ps_treatment_session (treatment_id, schedule_session_id, session_order, completed) VALUES (4, 22, 4, true);
INSERT INTO ps_treatment_session (treatment_id, schedule_session_id, session_order, completed) VALUES (4, 23, 5, true);
INSERT INTO ps_treatment_session (treatment_id, schedule_session_id, session_order, completed) VALUES (4, 24, 6, true);

INSERT INTO ps_session_notes (treatment_session_id, topic_addressed, session_summary, relevant_observations, next_topic, created_at)
VALUES (1, 'Procrastinación', 'Se abordaron las emociones asociadas a los exámenes.', 'El paciente mostró apertura al hablar de sus emociones.', 'Técnicas de relajación', '2025-05-06 14:00:00');

INSERT INTO ps_session_notes (treatment_session_id, topic_addressed, session_summary, relevant_observations, next_topic, created_at)
VALUES (2, 'Presión familiar', 'El paciente expresó dificultad para mantener la concentración.', 'Se notó mejora en su disposición a recibir apoyo.', 'Seguimiento de compromisos personales', '2025-05-07 14:00:00');

INSERT INTO ps_session_notes (treatment_session_id, topic_addressed, session_summary, relevant_observations, next_topic, created_at)
VALUES (3, 'Presión familiar', 'El paciente expresó dificultad para mantener la concentración.', 'Se notó mejora en su disposición a recibir apoyo.', 'Seguimiento de compromisos personales', '2025-05-08 14:00:00');

INSERT INTO ps_session_notes (treatment_session_id, topic_addressed, session_summary, relevant_observations, next_topic, created_at)
VALUES (4, 'Autoestima', 'El paciente compartió conflictos recientes con compañeros de curso.', 'Dificultades para manejar la frustración persisten.', 'Resolución de conflictos', '2025-05-09 14:00:00');

INSERT INTO ps_session_notes (treatment_session_id, topic_addressed, session_summary, relevant_observations, next_topic, created_at)
VALUES (5, 'Ansiedad académica', 'Se discutió la rutina diaria del paciente y se identificaron patrones de procrastinación.', 'Se notó mejora en su disposición a recibir apoyo.', 'Técnicas de relajación', '2025-05-10 14:00:00');

INSERT INTO ps_session_notes (treatment_session_id, topic_addressed, session_summary, relevant_observations, next_topic, created_at)
VALUES (6, 'Autoestima', 'Se revisaron avances en la organización académica.', 'Aún hay resistencia al cambio de hábitos.', 'Resolución de conflictos', '2025-05-11 14:00:00');

INSERT INTO ps_session_notes (treatment_session_id, topic_addressed, session_summary, relevant_observations, next_topic, created_at)
VALUES (7, 'Falta de motivación', 'El paciente expresó dificultad para mantener la concentración.', 'El paciente parece más motivado al implementar estrategias discutidas.', 'Técnicas de relajación', '2025-05-12 14:00:00');

INSERT INTO ps_session_notes (treatment_session_id, topic_addressed, session_summary, relevant_observations, next_topic, created_at)
VALUES (8, 'Procrastinación', 'Se abordaron las emociones asociadas a los exámenes.', 'El paciente mostró apertura al hablar de sus emociones.', 'Planificación del semestre', '2025-05-13 14:00:00');

INSERT INTO ps_session_notes (treatment_session_id, topic_addressed, session_summary, relevant_observations, next_topic, created_at)
VALUES (9, 'Procrastinación', 'Se abordaron las emociones asociadas a los exámenes.', 'Se notó mejora en su disposición a recibir apoyo.', 'Seguimiento de compromisos personales', '2025-05-14 14:00:00');

INSERT INTO ps_session_notes (treatment_session_id, topic_addressed, session_summary, relevant_observations, next_topic, created_at)
VALUES (10, 'Falta de motivación', 'El paciente compartió conflictos recientes con compañeros de curso.', 'Dificultades para manejar la frustración persisten.', 'Técnicas de relajación', '2025-05-15 14:00:00');

INSERT INTO ps_session_notes (treatment_session_id, topic_addressed, session_summary, relevant_observations, next_topic, created_at)
VALUES (11, 'Autoestima', 'Se discutió la rutina diaria del paciente y se identificaron patrones de procrastinación.', 'Dificultades para manejar la frustración persisten.', 'Fortalecimiento de autoestima', '2025-05-16 14:00:00');

INSERT INTO ps_session_notes (treatment_session_id, topic_addressed, session_summary, relevant_observations, next_topic, created_at)
VALUES (12, 'Ansiedad académica', 'El paciente compartió conflictos recientes con compañeros de curso.', 'Se notó mejora en su disposición a recibir apoyo.', 'Planificación del semestre', '2025-05-17 14:00:00');

INSERT INTO ps_session_notes (treatment_session_id, topic_addressed, session_summary, relevant_observations, next_topic, created_at)
VALUES (13, 'Hábitos de estudio', 'El paciente expresó dificultad para mantener la concentración.', 'Se observó mayor estabilidad emocional respecto a la primera sesión.', 'Profundizar en hábitos de estudio', '2025-05-18 14:00:00');

INSERT INTO ps_session_notes (treatment_session_id, topic_addressed, session_summary, relevant_observations, next_topic, created_at)
VALUES (14, 'Gestión del tiempo', 'El paciente expresó dificultad para mantener la concentración.', 'Se observó mayor estabilidad emocional respecto a la primera sesión.', 'Planificación del semestre', '2025-05-19 14:00:00');

INSERT INTO ps_session_notes (treatment_session_id, topic_addressed, session_summary, relevant_observations, next_topic, created_at)
VALUES (15, 'Hábitos de estudio', 'El paciente expresó dificultad para mantener la concentración.', 'Aún hay resistencia al cambio de hábitos.', 'Fortalecimiento de autoestima', '2025-05-20 14:00:00');

INSERT INTO ps_session_notes (treatment_session_id, topic_addressed, session_summary, relevant_observations, next_topic, created_at)
VALUES (16, 'Autoestima', 'Se revisaron avances en la organización académica.', 'El paciente parece más motivado al implementar estrategias discutidas.', 'Seguimiento de compromisos personales', '2025-05-21 14:00:00');

INSERT INTO ps_session_notes (treatment_session_id, topic_addressed, session_summary, relevant_observations, next_topic, created_at)
VALUES (17, 'Gestión del tiempo', 'El paciente compartió conflictos recientes con compañeros de curso.', 'Se observó mayor estabilidad emocional respecto a la primera sesión.', 'Técnicas de relajación', '2025-05-22 14:00:00');

INSERT INTO ps_session_notes (treatment_session_id, topic_addressed, session_summary, relevant_observations, next_topic, created_at)
VALUES (18, 'Comunicación asertiva', 'Se revisaron avances en la organización académica.', 'Dificultades para manejar la frustración persisten.', 'Planificación del semestre', '2025-05-23 14:00:00');

INSERT INTO ps_session_notes (treatment_session_id, topic_addressed, session_summary, relevant_observations, next_topic, created_at)
VALUES (19, 'Autoestima', 'Se realizó una técnica de respiración consciente para regular ansiedad.', 'El paciente mostró apertura al hablar de sus emociones.', 'Profundizar en hábitos de estudio', '2025-05-24 14:00:00');

INSERT INTO ps_session_notes (treatment_session_id, topic_addressed, session_summary, relevant_observations, next_topic, created_at)
VALUES (20, 'Problemas interpersonales', 'El paciente compartió conflictos recientes con compañeros de curso.', 'El paciente mostró apertura al hablar de sus emociones.', 'Resolución de conflictos', '2025-05-25 14:00:00');

INSERT INTO ps_session_notes (treatment_session_id, topic_addressed, session_summary, relevant_observations, next_topic, created_at)
VALUES (21, 'Ansiedad académica', 'Se abordaron las emociones asociadas a los exámenes.', 'Dificultades para manejar la frustración persisten.', 'Técnicas de relajación', '2025-05-26 14:00:00');

INSERT INTO ps_session_notes (treatment_session_id, topic_addressed, session_summary, relevant_observations, next_topic, created_at)
VALUES (22, 'Procrastinación', 'Se discutió la rutina diaria del paciente y se identificaron patrones de procrastinación.', 'Aún hay resistencia al cambio de hábitos.', 'Seguimiento de compromisos personales', '2025-05-27 14:00:00');

INSERT INTO ps_session_notes (treatment_session_id, topic_addressed, session_summary, relevant_observations, next_topic, created_at)
VALUES (23, 'Falta de motivación', 'El paciente expresó dificultad para mantener la concentración.', 'El paciente mostró apertura al hablar de sus emociones.', 'Técnicas de relajación', '2025-05-28 14:00:00');

INSERT INTO ps_session_notes (treatment_session_id, topic_addressed, session_summary, relevant_observations, next_topic, created_at)
VALUES (24, 'Presión familiar', 'El paciente expresó dificultad para mantener la concentración.', 'Se notó mejora en su disposición a recibir apoyo.', 'Fortalecimiento de autoestima', '2025-05-29 14:00:00');


INSERT INTO ps_case_file (treatment_id, date, summary, recommendations)
VALUES (1, '2025-05-22', 'El paciente mostró un progreso significativo en el manejo de la ansiedad académica y expresó mayor control emocional ante situaciones de evaluación.', 'Se recomienda continuar con ejercicios de respiración y mantener una planificación semanal de estudios.');

INSERT INTO ps_case_file (treatment_id, date, summary, recommendations)
VALUES (2, '2025-05-22', 'Durante el tratamiento, se trabajó principalmente en la motivación y establecimiento de objetivos. El paciente logró mejorar su asistencia y participación académica.', 'Se sugiere reforzar técnicas de autoevaluación y mantener contacto con orientación académica si la desmotivación persiste.');

INSERT INTO ps_case_file (treatment_id, date, summary, recommendations)
VALUES (3, '2025-05-22', 'Se abordaron dificultades de adaptación al entorno universitario, identificando factores sociales y emocionales. El paciente presentó avances notables en la integración con su grupo de compañeros.', 'Reforzar espacios de socialización positiva y técnicas de autorregulación emocional.');

INSERT INTO ps_case_file (treatment_id, date, summary, recommendations)
VALUES (4, '2025-05-22', 'El proceso permitió al paciente expresar con mayor claridad los conflictos familiares que impactaban en su rendimiento académico. Se trabajó en estrategias de afrontamiento y comunicación.', 'Es recomendable mantener un espacio de apoyo terapéutico y fortalecer redes de apoyo institucional.');


INSERT INTO ps_close_treatment (closed_treatment_id, closing_date, reason_for_closure)
VALUES (1, '2025-05-22', 'El paciente completó todas las sesiones planificadas y logró estabilizar los síntomas de ansiedad. Se considera el tratamiento cerrado exitosamente.');

INSERT INTO ps_close_treatment (closed_treatment_id, closing_date, reason_for_closure)
VALUES (2, '2025-05-22', 'El paciente mostró avances sostenidos en su motivación y se cumplió el objetivo terapéutico inicial.');

INSERT INTO ps_close_treatment (closed_treatment_id, closing_date, reason_for_closure)
VALUES (3, '2025-05-22', 'La intervención permitió al paciente desarrollar recursos personales para adaptarse al entorno académico. Se logró el cierre con evolución favorable.');

INSERT INTO ps_close_treatment (closed_treatment_id, closing_date, reason_for_closure)
VALUES (4, '2025-05-22', 'El paciente logró identificar factores emocionales clave y aplicar herramientas de manejo personal. Se acordó el cierre en consenso con el terapeuta.');


INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (70, 10, 'ACCEPTED');
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (64, 10, 'ACCEPTED');
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (45, 10, 'ACCEPTED');
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (6, 10, 'ACCEPTED');
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (60, 10, 'ACCEPTED');
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (69, 11, 'ACCEPTED');
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (63, 11, 'ACCEPTED');
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (46, 11, 'ACCEPTED');
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (7, 11, 'ACCEPTED');
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (59, 11, 'ACCEPTED');
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (15, 12, 'ACCEPTED');
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (53, 12, 'ACCEPTED');
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (47, 12, 'ACCEPTED');
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (8, 12, 'ACCEPTED');
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (57, 12, 'ACCEPTED');


INSERT INTO ps_treatment (user_therapist_id, user_patient_id, start_date, end_date, reason, semester)
VALUES (1, 10, '2025-04-21', '2025-05-30', 'Ansiedad por carga académica acumulada', '1-2025');
INSERT INTO ps_treatment (user_therapist_id, user_patient_id, start_date, end_date, reason, semester)
VALUES (1, 11, '2025-04-21', '2025-05-30', 'Desmotivación ante bajo rendimiento académico', '1-2025');
INSERT INTO ps_treatment (user_therapist_id, user_patient_id, start_date, end_date, reason, semester)
VALUES (1, 12, '2025-05-06', '2025-05-30', 'Dificultades de adaptación a la vida universitaria', '1-2025');


INSERT INTO ps_treatment_session (treatment_id, schedule_session_id, session_order, completed)
VALUES (5, 25, 1, true),
       (5, 26, 2, true),
       (5, 27, 3, true),
       (5, 28, 4, true),
       (5, 29, 5, true);
INSERT INTO ps_treatment_session (treatment_id, schedule_session_id, session_order, completed)
VALUES (6, 30, 1, true),
       (6, 31, 2, true),
       (6, 32, 3, true),
       (6, 33, 4, true),
       (6, 34, 5, true);
INSERT INTO ps_treatment_session (treatment_id, schedule_session_id, session_order, completed)
VALUES (7, 35, 1, true),
       (7, 36, 2, true),
       (7, 37, 3, true),
       (7, 38, 4, true),
       (7, 39, 5, true);

INSERT INTO ps_session_notes (treatment_session_id, topic_addressed, session_summary, relevant_observations, next_topic, created_at)
VALUES (25, 'Ansiedad académica', 'Se revisaron avances en la organización académica.', 'Se notó mejora en su disposición a recibir apoyo.', 'Fortalecimiento de autoestima', '2025-06-01 14:00:00');

INSERT INTO ps_session_notes (treatment_session_id, topic_addressed, session_summary, relevant_observations, next_topic, created_at)
VALUES (26, 'Autoestima', 'El paciente compartió conflictos recientes con compañeros de curso.', 'Se observó mayor estabilidad emocional respecto a la primera sesión.', 'Planificación del semestre', '2025-06-02 14:00:00');

INSERT INTO ps_session_notes (treatment_session_id, topic_addressed, session_summary, relevant_observations, next_topic, created_at)
VALUES (28, 'Presión familiar', 'Se abordaron las emociones asociadas a los exámenes.', 'Dificultades para manejar la frustración persisten.', 'Resolución de conflictos', '2025-06-03 14:00:00');

INSERT INTO ps_session_notes (treatment_session_id, topic_addressed, session_summary, relevant_observations, next_topic, created_at)
VALUES (29, 'Procrastinación', 'Se discutió la rutina diaria del paciente y se identificaron patrones de procrastinación.', 'Se observó mayor estabilidad emocional respecto a la primera sesión.', 'Fortalecimiento de autoestima', '2025-06-04 14:00:00');

INSERT INTO ps_session_notes (treatment_session_id, topic_addressed, session_summary, relevant_observations, next_topic, created_at)
VALUES (27, 'Problemas interpersonales', 'Se revisaron avances en la organización académica.', 'Aún hay resistencia al cambio de hábitos.', 'Técnicas de relajación', '2025-06-05 14:00:00');

INSERT INTO ps_session_notes (treatment_session_id, topic_addressed, session_summary, relevant_observations, next_topic, created_at)
VALUES (33, 'Autoestima', 'El paciente compartió conflictos recientes con compañeros de curso.', 'El paciente mostró apertura al hablar de sus emociones.', 'Resolución de conflictos', '2025-06-06 14:00:00');

INSERT INTO ps_session_notes (treatment_session_id, topic_addressed, session_summary, relevant_observations, next_topic, created_at)
VALUES (32, 'Presión familiar', 'El paciente expresó dificultad para mantener la concentración.', 'Se notó mejora en su disposición a recibir apoyo.', 'Planificación del semestre', '2025-06-07 14:00:00');

INSERT INTO ps_session_notes (treatment_session_id, topic_addressed, session_summary, relevant_observations, next_topic, created_at)
VALUES (34, 'Manejo del estrés', 'Se discutió la rutina diaria del paciente y se identificaron patrones de procrastinación.', 'Dificultades para manejar la frustración persisten.', 'Seguimiento de compromisos personales', '2025-06-08 14:00:00');

INSERT INTO ps_session_notes (treatment_session_id, topic_addressed, session_summary, relevant_observations, next_topic, created_at)
VALUES (30, 'Procrastinación', 'Se revisaron avances en la organización académica.', 'El paciente parece más motivado al implementar estrategias discutidas.', 'Técnicas de relajación', '2025-06-09 14:00:00');

INSERT INTO ps_session_notes (treatment_session_id, topic_addressed, session_summary, relevant_observations, next_topic, created_at)
VALUES (31, 'Comunicación asertiva', 'Se realizó una técnica de respiración consciente para regular ansiedad.', 'Se notó mejora en su disposición a recibir apoyo.', 'Profundizar en hábitos de estudio', '2025-06-10 14:00:00');

INSERT INTO ps_session_notes (treatment_session_id, topic_addressed, session_summary, relevant_observations, next_topic, created_at)
VALUES (40, 'Problemas interpersonales', 'Se revisaron avances en la organización académica.', 'Se observó mayor estabilidad emocional respecto a la primera sesión.', 'Seguimiento de compromisos personales', '2025-06-11 14:00:00');

INSERT INTO ps_session_notes (treatment_session_id, topic_addressed, session_summary, relevant_observations, next_topic, created_at)
VALUES (41, 'Gestión del tiempo', 'Se abordaron las emociones asociadas a los exámenes.', 'Dificultades para manejar la frustración persisten.', 'Técnicas de relajación', '2025-06-12 14:00:00');

INSERT INTO ps_session_notes (treatment_session_id, topic_addressed, session_summary, relevant_observations, next_topic, created_at)
VALUES (42, 'Presión familiar', 'El paciente compartió conflictos recientes con compañeros de curso.', 'Se notó mejora en su disposición a recibir apoyo.', 'Profundizar en hábitos de estudio', '2025-06-13 14:00:00');

INSERT INTO ps_session_notes (treatment_session_id, topic_addressed, session_summary, relevant_observations, next_topic, created_at)
VALUES (43, 'Falta de motivación', 'El paciente expresó dificultad para mantener la concentración.', 'El paciente parece más motivado al implementar estrategias discutidas.', 'Resolución de conflictos', '2025-06-14 14:00:00');

INSERT INTO ps_session_notes (treatment_session_id, topic_addressed, session_summary, relevant_observations, next_topic, created_at)
VALUES (44, 'Ansiedad académica', 'Se abordaron las emociones asociadas a los exámenes.', 'Se observó mayor estabilidad emocional respecto a la primera sesión.', 'Seguimiento de compromisos personales', '2025-06-15 14:00:00');


INSERT INTO ps_case_file (treatment_id, date, summary, recommendations)
VALUES (5, '2025-05-30', 'El paciente presentó progresos moderados en la regulación de su ansiedad académica. Participó activamente en la mayoría de las sesiones y fue receptivo a las estrategias propuestas.', 'Continuar con técnicas de relajación diarias y mantener una rutina de estudio balanceada.');

INSERT INTO ps_case_file (treatment_id, date, summary, recommendations)
VALUES (6, '2025-05-30', 'Durante el tratamiento, el paciente logró reconocer patrones de desmotivación y trabajó en el fortalecimiento de su autoestima. Aun requiere acompañamiento ocasional para prevenir recaídas.', 'Buscar apoyo en grupos de estudio y reforzar habilidades de comunicación con docentes.');

INSERT INTO ps_case_file (treatment_id, date, summary, recommendations)
VALUES (7, '2025-05-30', 'Se identificaron dificultades iniciales en la adaptación al entorno universitario. El paciente fue avanzando en el manejo de estrés y la organización personal.', 'Se recomienda mantener espacios de diálogo abiertos con sus tutores académicos y practicar técnicas de organización semanal.');


INSERT INTO ps_close_treatment (closed_treatment_id, closing_date, reason_for_closure)
VALUES (5, '2025-05-30', 'Finalización planificada tras cumplimiento del número de sesiones acordadas.');

INSERT INTO ps_close_treatment (closed_treatment_id, closing_date, reason_for_closure)
VALUES (6, '2025-05-30', 'Cierre acordado tras mejora significativa en los síntomas y cumplimiento del plan de intervención.');

INSERT INTO ps_close_treatment (closed_treatment_id, closing_date, reason_for_closure)
VALUES (7, '2025-05-30', 'Finalización del tratamiento tras superar los objetivos establecidos y estabilización emocional.');

-- Sesiones pasadas
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (61, 3, 'ACCEPTED');
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (36, 3, 'ACCEPTED');
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (14, 3, 'ACCEPTED');
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (55, 3, 'ACCEPTED');
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (40, 3, 'ACCEPTED');

-- Sesiones futuras
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (12, 3, 'ACCEPTED');
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (94, 3, 'ACCEPTED');
INSERT INTO ps_schedule_session (therapist_scheduled_id, user_patient_id, state) VALUES (98, 3, 'ACCEPTED');

INSERT INTO ps_treatment (
    user_therapist_id,
    user_patient_id,
    start_date,
    end_date,
    reason,
    semester
) VALUES (
             1,
             3,
             '2025-04-25',
             '2025-06-06',
             'Apoyo por estrés académico durante el semestre',
             '1-2025'
         );


INSERT INTO ps_treatment_session (treatment_id, session_order, schedule_session_id, completed)
VALUES (8, 1, 41, true);

INSERT INTO ps_treatment_session (treatment_id, session_order, schedule_session_id, completed)
VALUES (8, 2, 42, true);

INSERT INTO ps_treatment_session (treatment_id, session_order, schedule_session_id, completed)
VALUES (8, 3, 43, true);

INSERT INTO ps_treatment_session (treatment_id, session_order, schedule_session_id, completed)
VALUES (8, 4, 44, true);

INSERT INTO ps_treatment_session (treatment_id, session_order, schedule_session_id, completed)
VALUES (8, 5, 45, true);

INSERT INTO ps_treatment_session (treatment_id, session_order, schedule_session_id, completed)
VALUES (8, 6, 46, false);

INSERT INTO ps_treatment_session (treatment_id, session_order, schedule_session_id, completed)
VALUES (8, 7, 47, false);

INSERT INTO ps_treatment_session (treatment_id, session_order, schedule_session_id, completed)
VALUES (8, 8, 48, false);

INSERT INTO ps_session_notes (treatment_session_id, topic_addressed, session_summary, relevant_observations, next_topic, created_at)
VALUES (45, 'Manejo del estrés', 'El paciente expresó dificultad para mantener la concentración.', 'Aún hay resistencia al cambio de hábitos.', 'Técnicas de relajación', '2025-06-01 09:00:00');
INSERT INTO ps_session_notes (treatment_session_id, topic_addressed, session_summary, relevant_observations, next_topic, created_at)
VALUES (46, 'Gestión del tiempo', 'Se realizó una técnica de respiración consciente para regular ansiedad.', 'El paciente mostró apertura al hablar de sus emociones.', 'Seguimiento de compromisos personales', '2025-06-02 09:00:00');
INSERT INTO ps_session_notes (treatment_session_id, topic_addressed, session_summary, relevant_observations, next_topic, created_at)
VALUES (47, 'Problemas interpersonales', 'Se abordaron las emociones asociadas a los exámenes.', 'Se notó mejora en su disposición a recibir apoyo.', 'Profundizar en hábitos de estudio', '2025-06-03 09:00:00');
INSERT INTO ps_session_notes (treatment_session_id, topic_addressed, session_summary, relevant_observations, next_topic, created_at)
VALUES (48, 'Comunicación asertiva', 'El paciente expresó dificultad para mantener la concentración.', 'Se observó mayor estabilidad emocional respecto a la primera sesión.', 'Técnicas de relajación', '2025-06-04 09:00:00');
INSERT INTO ps_session_notes (treatment_session_id, topic_addressed, session_summary, relevant_observations, next_topic, created_at)
VALUES (49, 'Gestión del tiempo', 'El paciente compartió conflictos recientes con compañeros de curso.', 'Se observó mayor estabilidad emocional respecto a la primera sesión.', 'Profundizar en hábitos de estudio', '2025-06-05 09:00:00');
