package com.example.agendaily2.pojos;

import java.io.Serializable;
//Clase Pojo de la tabla Recordatorio de la bdd
public class Recordatorio implements Serializable {

    private Integer recordatorioId;
    private String title;
    private String description;
    private Integer hora;
    private User userId;


    public Recordatorio() {
    }

    public Recordatorio(Integer recordatorioId, String title, String description, Integer hora, User userId) {
        this.recordatorioId = recordatorioId;
        this.title = title;
        this.description = description;
        this.hora = hora;
        this.userId = userId;
    }

    public Integer getRecordatorioId() {
        return recordatorioId;
    }

    public void setRecordatorioId(Integer recordatorioId) {
        this.recordatorioId = recordatorioId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getHora() {
        return hora;
    }

    public void setHora(Integer hora) {
        this.hora = hora;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Recordatorio{" +
                "recordatorioId=" + recordatorioId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", hora=" + hora +
                ", userId=" + userId +
                '}';
    }
}
