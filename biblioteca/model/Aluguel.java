package com.biblioteca.model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Representa a entidade Aluguel do banco de dados.
 */
public class Aluguel {

    private int idAluguel;

    // Timestamp representa as colunas DATETIME (data + hora)
    private Timestamp dataAluguel;
    private Date dataPrevistaDevolucao;
    private Date dataEfetivaDevolucao;
    private Usuario usuario;
    private Livro livro;

    // Campo para o 'Visualizar Histórico')
    // Pode ser útil para a interface
    private String statusAluguel;

    // Construtor
    public Aluguel() {
    }

    // Getters e Setters

    public int getIdAluguel() {
        return idAluguel;
    }
    public void setIdAluguel(int idAluguel) {
        this.idAluguel = idAluguel;
    }

    public Timestamp getDataAluguel() {
        return dataAluguel;
    }
    public void setDataAluguel(Timestamp dataAluguel) {
        this.dataAluguel = dataAluguel;
    }

    public Date getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }
    public void setDataPrevistaDevolucao(Date dataPrevistaDevolucao) {
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
    }

    public Date getDataEfetivaDevolucao() {
        return dataEfetivaDevolucao;
    }
    public void setDataEfetivaDevolucao(Date dataEfetivaDevolucao) {
        this.dataEfetivaDevolucao = dataEfetivaDevolucao;
    }

    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Livro getLivro() {
        return livro;
    }
    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public String getStatusAluguel() {
        return statusAluguel;
    }
    public void setStatusAluguel(String statusAluguel) {
        this.statusAluguel = statusAluguel;
    }

    @Override
    public String toString() {
        return "Aluguel{" +
                "idAluguel=" + idAluguel +
                ", usuario=" + (usuario != null ? usuario.getNome() : "null") +
                ", livro=" + (livro != null ? livro.getTitulo() : "null") +
                ", dataAluguel=" + dataAluguel +
                '}';
    }
}