package com.ucbprojeto.ecoscore.dto;

import java.sql.Date;

public class MetaDTO {
    private int id_familia;
    private String titulo;
    private String descricao;
    private int pontos_objetivo;
    private Date data_inicio;
    private Date data_fim;
    private String status; // "ATIVA", "CONCLUIDA", "CANCELADA"

    // Getters e setters
    public int getId_familia() {
        return id_familia;
    }

    public void setId_familia(int id_familia) {
        this.id_familia = id_familia;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getPontos_objetivo() {
        return pontos_objetivo;
    }

    public void setPontos_objetivo(int pontos_objetivo) {
        this.pontos_objetivo = pontos_objetivo;
    }

    public Date getData_inicio() {
        return data_inicio;
    }

    public void setData_inicio(Date data_inicio) {
        this.data_inicio = data_inicio;
    }

    public Date getData_fim() {
        return data_fim;
    }

    public void setData_fim(Date data_fim) {
        this.data_fim = data_fim;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}