package com.biblioteca.model;

/**
 * Representa a entidade Autor do banco de dados.
 * (POJO - Plain Old Java Object)
 */
public class Autor {

    private int idAutor;
    private String nome;

    // Construtor vazio (importante para frameworks)
    public Autor() {
    }

    // Construtor para facilitar a criação
    public Autor(int idAutor, String nome) {
        this.idAutor = idAutor;
        this.nome = nome;
    }

    //Getters e Setters

    public int getIdAutor() {
        return idAutor;
    }
    public void setIdAutor(int idAutor) {
        this.idAutor = idAutor;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Autor{" +
                "idAutor=" + idAutor +
                ", nome='" + nome + '\'' +
                '}';
    }
}