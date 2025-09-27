package com.ucbprojeto.ecoscore.model;

public class Telefone {
    private int id_telefone;
    private String cpf_membro;
    private String numero;

    public int getId_telefone() {
        return id_telefone;
    }

    public void setId_telefone(int id_telefone) {
        this.id_telefone = id_telefone;
    }

    public String getCpf_membro() {
        return cpf_membro;
    }

    public void setCpf_membro(String cpf_membro) {
        this.cpf_membro = cpf_membro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}
