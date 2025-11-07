package biblioteca;

import java.util.Date;

public abstract class Usuario {
    protected int id_User;
    protected String nome;
    protected String email;
    protected Date dataCadastro;
    protected String senha;

    public Usuario(int id, String nome, String email, String senha) {
        this.id_User = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataCadastro = new Date();
    }

    public boolean login() {
        System.out.println(nome + " fez login.");
        return true;
    }
    public void logout() { System.out.println(nome + " fez logout."); }

    public String getEmail() { return email; }
    public String getNome() { return nome; }
}
