package com.ucbprojeto.ecoscore.model;

public class CategoriaPratica {
    public enum EDificuldade {
        FACIL,
        MEDIA,
        DIFICIL
    }

    private int id_pratica;
    private int id_categoria;
    private String nome;
    private String descricao;
    private int pontos_base;
    private EDificuldade dificuldade;

    public int getId_pratica() {
        return id_pratica;
    }

    public void setId_pratica(int id_pratica) {
        this.id_pratica = id_pratica;
    }

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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getPontos_base() {
        return pontos_base;
    }

    public void setPontos_base(int pontos_base) {
        this.pontos_base = pontos_base;
    }

    public EDificuldade getDificuldade() {
        return dificuldade;
    }

    public void setDificuldade(EDificuldade dificuldade) {
        this.dificuldade = dificuldade;
    }
}
