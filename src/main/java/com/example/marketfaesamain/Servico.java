package com.example.marketfaesamain;

import java.io.Serializable;
import java.util.List;

// Serializable permite que o Java converta este objeto em bytes para salvar em arquivo
public class Servico implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String titulo;
    private String categoria;
    private String descricao;
    private double valor;

    // Contador estático não é serializado — precisa ser recalculado ao carregar
    private static int contadorId = 1;

    public Servico(String titulo, String categoria, String descricao, double valor) {
        this.id = contadorId++;
        this.titulo = titulo;
        this.categoria = categoria;
        this.descricao = descricao;
        this.valor = valor;
    }

    // Chamado pelo BancoDados após carregar a lista
    // Garante que novos IDs continuem a partir do maior ID já existente
    public static void sincronizarContador(List<Servico> lista) {
        lista.stream()
                .mapToInt(Servico::getId)
                .max()
                .ifPresent(maiorId -> contadorId = maiorId + 1);
    }

    public int getId()           { return id; }
    public String getTitulo()    { return titulo; }
    public String getCategoria() { return categoria; }
    public String getDescricao() { return descricao; }
    public double getValor()     { return valor; }

    public void setTitulo(String titulo)       { this.titulo = titulo; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setValor(double valor)         { this.valor = valor; }

    @Override
    public String toString() {
        return String.format("[%d] %s | %s | R$ %.2f", id, titulo, categoria, valor);
    }
}