package com.meudelivery.victorcorreia.bigm;

/**
 * Created by Toner e Tintas on 31/01/2016.
 */
public class ItemPedido {
    private int id;
    private int idPedido;
    private int idItem;
    private String nome;
    private String valor;
    private String observacao;
    private int quant;

    public ItemPedido() {
        this.id = id;
        this.idPedido = idPedido;
        this.idItem = idItem;
        this.nome = nome;
        this.valor = valor;
        this.observacao = observacao;
        this.quant = quant;

    }



    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public int getIdItem() {

        return idItem;
    }

    public void setIdItem(int idItem) {

        this.idItem = idItem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public int getQuant() {

        return quant;
    }

    public void setQuant(int quant) {

        this.quant = quant;
    }

    public int getIdPedido() {

        return idPedido;
    }

    public void setIdPedido(int idPedido) {

        this.idPedido = idPedido;
    }
}
