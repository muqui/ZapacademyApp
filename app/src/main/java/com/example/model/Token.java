package com.example.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Token  implements Serializable {
    @SerializedName("user")
    User usuario;
    @SerializedName("token")
    private String Token;

    public Token() {
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }
}
