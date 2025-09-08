package com.ucbprojeto.ecoscore.model;

public class CategoriaPratica {
    private int id_categoria;
    private String nome;
    private int fator_multiplicador;

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getFator_multiplicador() {
        return fator_multiplicador;
    }

    public void setFator_multiplicador(int fator_multiplicador) {
        this.fator_multiplicador = fator_multiplicador;
    }
}
