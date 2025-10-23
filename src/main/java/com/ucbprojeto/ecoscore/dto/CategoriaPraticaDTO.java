package com.ucbprojeto.ecoscore.dto;

public class CategoriaPraticaDTO {
    private String nome;
    private double fator_multiplicador;

    // Getters e setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getFator_multiplicador() {
        return fator_multiplicador;
    }

    public void setFator_multiplicador(double fator_multiplicador) {
        this.fator_multiplicador = fator_multiplicador;
    }
}