package com.ucbprojeto.ecoscore.model;

import java.sql.Date;

public class Conquista {
    private int id_conquista;
    private int id_meta;
    private Date data_conquista;
    private int pontos_bonus;

    public int getId_conquista() {
        return id_conquista;
    }

    public void setId_conquista(int id_conquista) {
        this.id_conquista = id_conquista;
    }

    public int getId_meta() {
        return id_meta;
    }

    public void setId_meta(int id_meta) {
        this.id_meta = id_meta;
    }

    public Date getData_conquista() {
        return data_conquista;
    }

    public void setData_conquista(Date data_conquista) {
        this.data_conquista = data_conquista;
    }

    public int getPontos_bonus() {
        return pontos_bonus;
    }

    public void setPontos_bonus(int pontos_bonus) {
        this.pontos_bonus = pontos_bonus;
    }
}
