package com.example.model;

import java.io.Serializable;
import java.util.Date;

public class Attendance  implements Serializable {
    private String status;
    private String fecha;
    private String event;
    private String user;
    private String beneficiary;

    public Attendance(String status, String fecha, String event, String user, String beneficiary) {
        this.status = status;
        this.fecha = fecha;
        this.event = event;
        this.user = user;
        this.beneficiary = beneficiary;
    }

    public Attendance() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }
}
