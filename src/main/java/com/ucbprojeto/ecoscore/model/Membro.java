package com.ucbprojeto.ecoscore.model;

import java.sql.Date;

public class Membro {
    private String cpf;
    private String senha;
    private Integer id_familia;
    private String nome;
    private Date data_nascimento;
    private String papel_familia;
    private String cpf_supervisor;

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Integer getId_familia() {
        return id_familia;
    }

    public void setId_familia(Integer id_familia) {
        this.id_familia = id_familia;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getData_nascimento() {
        return data_nascimento;
    }

    public void setData_nascimento(Date data_nascimento) {
        this.data_nascimento = data_nascimento;
    }

    public String getPapel_familia() {
        return papel_familia;
    }

    public void setPapel_familia(String papel_familia) {
        this.papel_familia = papel_familia;
    }

    public String getCpf_supervisor() {
        return cpf_supervisor;
    }

    public void setCpf_supervisor(String cpf_supervisor) {
        this.cpf_supervisor = cpf_supervisor;
    }
}
