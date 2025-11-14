package com.biblioteca.model;

import java.sql.Date;
import java.util.ArrayList; // Importamos o ArrayList para inicializar as listas
import java.util.List;     // Importamos a interface List

/**
 * Representa a entidade Livro do banco de dados,
 * incluindo seus relacionamentos com Autor, Genero e Editora.
 */
public class Livro {

    private int idLivro;
    private String titulo;
    private String isbn;
    private String sinopse;
    private int anoPublicacao;
    private Date dataCadastro;
    private int paginas;
    private String capaPath;
    private String livroPath;

    // Relacionamentos

    // Relacionamento 1:N (Um livro tem uma editora)
    private Editora editora;

    // Relacionamento N:N (Um livro tem vários autores)
    private List<Autor> autores;

    // Relacionamento N:N (Um livro tem vários gêneros)
    private List<Genero> generos;

    // Construtor
    public Livro() {
        this.autores = new ArrayList<>();
        this.generos = new ArrayList<>();
    }

    //  Getters e Setters

    public int getIdLivro() {
        return idLivro;
    }
    public void setIdLivro(int idLivro) {
        this.idLivro = idLivro;
    }

    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getSinopse() {
        return sinopse;
    }
    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public int getAnoPublicacao() {
        return anoPublicacao;
    }
    public void setAnoPublicacao(int anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }
    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public int getPaginas() {
        return paginas;
    }
    public void setPaginas(int paginas) {
        this.paginas = paginas;
    }

    public String getCapaPath() {
        return capaPath;
    }
    public void setCapaPath(String capaPath) {
        this.capaPath = capaPath;
    }

    public String getLivroPath() {
        return livroPath;
    }
    public void setLivroPath(String livroPath) {
        this.livroPath = livroPath;
    }

    public Editora getEditora() {
        return editora;
    }
    public void setEditora(Editora editora) {
        this.editora = editora;
    }

    public List<Autor> getAutores() {
        return autores;
    }
    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }

    public List<Genero> getGeneros() {
        return generos;
    }
    public void setGeneros(List<Genero> generos) {
        this.generos = generos;
    }

    // Métodos utilitários

    public void adicionarAutor(Autor autor) {
        this.autores.add(autor);
    }

    public void adicionarGenero(Genero genero) {
        this.generos.add(genero);
    }

    @Override
    public String toString() {
        return "Livro{" +
                "idLivro=" + idLivro +
                ", titulo='" + titulo + '\'' +
                ", editora=" + (editora != null ? editora.getNome() : "null") +
                ", autores=" + autores.size() +
                ", generos=" + generos.size() +
                '}';
    }
}