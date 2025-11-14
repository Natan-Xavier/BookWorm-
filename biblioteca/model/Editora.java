package com.biblioteca.model;

/**
 * Representa a entidade Editora do banco de dados.
 */
public class Editora {

    private int idEditora;
    private String nome;

    // Construtores
    public Editora() {
    }

    public Editora(int idEditora, String nome) {
        this.idEditora = idEditora;
        this.nome = nome;
    }

    // --- Getters e Setters ---

    public int getIdEditora() {
        return idEditora;
    }
    public void setIdEditora(int idEditora) {
        this.idEditora = idEditora;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Editora{" +
                "idEditora=" + idEditora +
                ", nome='" + nome + '\'' +
                '}';
    }
}