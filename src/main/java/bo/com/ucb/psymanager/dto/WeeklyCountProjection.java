package bo.com.ucb.psymanager.dto;

/**
 * Proyección para el conteo semanal de ejercicios completados.
 *   - year: año ISO de la semana
 *   - week: número de semana ISO (1–53)
 *   - count: cantidad de ejercicios completados en esa semana
 */
public interface WeeklyCountProjection {
    Integer getYear();
    Integer getWeek();
    Long getCount();
}
