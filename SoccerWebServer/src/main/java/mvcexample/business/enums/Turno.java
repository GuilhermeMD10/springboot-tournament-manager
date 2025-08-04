package mvcexample.business.enums;

import java.time.LocalTime;

public enum Turno {
    MANHA(LocalTime.of(8, 0)),
    TARDE(LocalTime.of(12, 0)),
    NOITE(LocalTime.of(18, 0));

    private final LocalTime horaInicio;

    Turno(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }
}
