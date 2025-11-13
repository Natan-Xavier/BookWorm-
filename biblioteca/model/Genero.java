package com.biblioteca.model;

/**
 * Representa a entidade Genero do banco de dados.
 */
public class Genero {

    private int idGenero;
    private String nome;

    // Construtores
    public Genero() {
    }

    public Genero(int idGenero, String nome) {
        this.idGenero = idGenero;
        this.nome = nome;
    }

    // --- Getters e Setters ---

    public int getIdGenero() {
        return idGenero;
    }
    public void setIdGenero(int idGenero) {
        this.idGenero = idGenero;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Genero{" +
                "idGenero=" + idGenero +
                ", nome='" + nome + '\'' +
                '}';
    }
}