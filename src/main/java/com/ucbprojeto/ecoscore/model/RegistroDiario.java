package com.ucbprojeto.ecoscore.model;

import java.sql.Date;

public class RegistroDiario {
    private int id_registro;
    private String cpf_membro;
    private int id_pratica;
    private Date data_registro;
    private int quantidade_realizada;
    private int pontos_ganhos;

    public int getId_registro() {
        return id_registro;
    }

    public void setId_registro(int id_registro) {
        this.id_registro = id_registro;
    }

    public String getCpf_membro() {
        return cpf_membro;
    }

    public void setCpf_membro(String cpf_membro) {
        this.cpf_membro = cpf_membro;
    }

    public int getId_pratica() {
        return id_pratica;
    }

    public void setId_pratica(int id_pratica) {
        this.id_pratica = id_pratica;
    }

    public Date getData_registro() {
        return data_registro;
    }

    public void setData_registro(Date data_registro) {
        this.data_registro = data_registro;
    }

    public int getQuantidade_realizada() {
        return quantidade_realizada;
    }

    public void setQuantidade_realizada(int quantidade_realizada) {
        this.quantidade_realizada = quantidade_realizada;
    }

    public int getPontos_ganhos() {
        return pontos_ganhos;
    }

    public void setPontos_ganhos(int pontos_ganhos) {
        this.pontos_ganhos = pontos_ganhos;
    }
}
