package com.ucbprojeto.ecoscore.model;

public class RelatorioMensal {
    private int id_relatorio;
    private int id_familia;
    private String mes_ano;
    private int total_pontos_familia;
    private int total_praticas_realizadas;

    public int getId_relatorio() {
        return id_relatorio;
    }

    public void setId_relatorio(int id_relatorio) {
        this.id_relatorio = id_relatorio;
    }

    public int getId_familia() {
        return id_familia;
    }

    public void setId_familia(int id_familia) {
        this.id_familia = id_familia;
    }

    public String getMes_ano() {
        return mes_ano;
    }

    public void setMes_ano(String mes_ano) {
        this.mes_ano = mes_ano;
    }

    public int getTotal_pontos_familia() {
        return total_pontos_familia;
    }

    public void setTotal_pontos_familia(int total_pontos_familia) {
        this.total_pontos_familia = total_pontos_familia;
    }

    public int getTotal_praticas_realizadas() {
        return total_praticas_realizadas;
    }

    public void setTotal_praticas_realizadas(int total_praticas_realizadas) {
        this.total_praticas_realizadas = total_praticas_realizadas;
    }
}
