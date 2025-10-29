package com.sanacorp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

/**
 * Entidad que representa los horarios de atención de los médicos
 * Basada en la tabla 'horarios' de la base de datos
 */
@Entity
@Table(name = "horarios")
public class Horario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long idHorario;
    
    @NotNull(message = "El día de la semana es obligatorio")
    @Column(name = "dia", nullable = false)
    private Integer diaSemana; // 1=Lunes, 2=Martes, ..., 7=Domingo
    
    @NotNull(message = "La hora de inicio es obligatoria")
    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;
    
    @NotNull(message = "La hora de fin es obligatoria")
    @Column(name = "hora_fin", nullable = false)
    private LocalTime horaFin;
    
    @Column(name = "turno")
    private String turno; // 'Mañana', 'Tarde', 'Noche'
    
    // @Column(name = "activo", nullable = false)
    // private Boolean activo = true;
    
    // Relación Many-to-One con Medico
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_medico", referencedColumnName = "id", nullable = false)
    private Medico medico;
    
    // Constructores
    public Horario() {}
    
    public Horario(Integer diaSemana, LocalTime horaInicio, LocalTime horaFin, Medico medico) {
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.medico = medico;
    }
    
    // Getters y Setters
    public Long getIdHorario() {
        return idHorario;
    }
    
    public void setIdHorario(Long idHorario) {
        this.idHorario = idHorario;
    }
    
    public Integer getDiaSemana() {
        return diaSemana;
    }
    
    public void setDiaSemana(Integer diaSemana) {
        this.diaSemana = diaSemana;
    }
    
    public LocalTime getHoraInicio() {
        return horaInicio;
    }
    
    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }
    
    public LocalTime getHoraFin() {
        return horaFin;
    }
    
    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }
    
    public String getTurno() {
        return turno;
    }
    
    public void setTurno(String turno) {
        this.turno = turno;
    }
    
    // public Boolean getActivo() {
    //     return activo;
    // }
    
    // public void setActivo(Boolean activo) {
    //     this.activo = activo;
    // }
    
    public Medico getMedico() {
        return medico;
    }
    
    public void setMedico(Medico medico) {
        this.medico = medico;
    }
    
    // Métodos de utilidad
    public String getDiaSemanaTexto() {
        switch (diaSemana) {
            case 1: return "Lunes";
            case 2: return "Martes";
            case 3: return "Miércoles";
            case 4: return "Jueves";
            case 5: return "Viernes";
            case 6: return "Sábado";
            case 7: return "Domingo";
            default: return "Desconocido";
        }
    }
    
    public String getHorarioTexto() {
        return horaInicio + " - " + horaFin;
    }
    
    @Override
    public String toString() {
        return "Horario{" +
                "idHorario=" + idHorario +
                ", diaSemana=" + diaSemana +
                ", horaInicio=" + horaInicio +
                ", horaFin=" + horaFin +
                ", turno='" + turno + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Horario)) return false;
        Horario horario = (Horario) o;
        return idHorario != null && idHorario.equals(horario.idHorario);
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}