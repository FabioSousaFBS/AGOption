package agoption.projetos.com.agoption.model;

import java.io.Serializable;

public class Carros implements Serializable{

    private int idCarro;
    private String descricao;
    private double cmEtanol;
    private double cmGasolina;
    private double fator;

    public Carros(){

    }

    public Carros(int id, String dsCarro, double cmEtanol, double cmGasolina, double fator){
        super();
        this.idCarro = id;
        this.descricao = dsCarro;
        this.cmEtanol = cmEtanol;
        this.cmGasolina = cmGasolina;
        this.fator = fator;
    }


    public int getIdCarro() {
        return idCarro;
    }

    public void setIdCarro(int idCarro) {
        this.idCarro = idCarro;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getCmEtanol() {
        return cmEtanol;
    }

    public void setCmEtanol(double cmEtanol) {
        this.cmEtanol = cmEtanol;
    }

    public double getCmGasolina() {
        return cmGasolina;
    }

    public void setCmGasolina(double cmGasolina) {
        this.cmGasolina = cmGasolina;
    }

    public double getFator() {
        return fator;
    }

    public void setFator(double fator) {
        this.fator = fator;
    }
}
