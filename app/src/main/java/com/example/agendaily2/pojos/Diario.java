package com.example.agendaily2.pojos;

import java.io.Serializable;

//Clase Pojo de la tabla Diario de la bdd


public class Diario implements Serializable {

    private Integer diarioId;
    private String fecha;
    private String description;
    private User userId;

    public Diario() {
    }

    public Diario(Integer diarioId, String fecha, String description, User userId) {
        this.diarioId = diarioId;
        this.fecha = fecha;
        this.description = description;
        this.userId = userId;
    }

    public Integer getDiarioId() {
        return diarioId;
    }

    public void setDiarioId(Integer diarioId) {
        this.diarioId = diarioId;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Diario{" +
                "diarioId=" + diarioId +
                ", fecha='" + fecha + '\'' +
                ", description='" + description + '\'' +
                ", userId=" + userId +
                '}';
    }
}
