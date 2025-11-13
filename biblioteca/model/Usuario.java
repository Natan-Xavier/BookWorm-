package com.biblioteca.model;

// Importamos java.sql.Date para ser o 'DATE' do SQL Server
import java.sql.Date;

/**
 * Representa a entidade Usuário do banco de dados,
 */
public class Usuario {

    private int idUsuario;
    private String nome;
    private String email;
    private String senha;
    private String sexo;
    private Date dataNascimento;
    private Date dataCadastro;
    private String statusConta;
    private String credencial;

    // Construtor vazio
    public Usuario() {
    }

    // Getters e Setters

    public int getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getSexo() {
        return sexo;
    }
    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }
    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }
    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getStatusConta() {
        return statusConta;
    }
    public void setStatusConta(String statusConta) {
        this.statusConta = statusConta;
    }

    public String getCredencial() {
        return credencial;
    }
    public void setCredencial(String credencial) {
        this.credencial = credencial;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", credencial='" + credencial + '\'' +
                ", statusConta='" + statusConta + '\'' +
                '}';
    }
}