package com.ucbprojeto.ecoscore.model;

public class MembroCrianca extends Membro {
    private String cpf_responsavel;
    private int bonus_escolar;

    public String getCpf_responsavel() {
        return cpf_responsavel;
    }

    public void setCpf_responsavel(String cpf_responsavel) {
        this.cpf_responsavel = cpf_responsavel;
    }

    public int getBonus_escolar() {
        return bonus_escolar;
    }

    public void setBonus_escolar(int bonus_escolar) {
        this.bonus_escolar = bonus_escolar;
    }
}
