package com.ucbprojeto.ecoscore.model;

public class MembroAdulto {
    private String cpf;
    private String email;
    private boolean eh_responsavel;

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEh_responsavel() {
        return eh_responsavel;
    }

    public void setEh_responsavel(boolean eh_responsavel) {
        this.eh_responsavel = eh_responsavel;
    }
}
