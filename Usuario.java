package biblioteca;

// Importações atualizadas para o pacote java.time
import java.time.LocalDate;
import java.time.LocalDateTime;
// O antigo 'java.util.Date' não é mais necessário


public abstract class Usuario {

    // Atributos da tabela
    protected int id_User;
    protected String nome;
    protected String email;
    protected String senha;
    protected String sexo;
    protected LocalDate dataNascimento;
    protected LocalDateTime dataCadastro;
    protected String statusConta;
    protected String credencial;

    public Usuario(int id_User, String nome, String email, String senha,
                   String sexo, LocalDate dataNascimento, LocalDateTime dataCadastro,
                   String statusConta, String credencial) {

        this.id_User = id_User;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.sexo = sexo;
        this.dataNascimento = dataNascimento;
        this.dataCadastro = dataCadastro;
        this.statusConta = statusConta;
        this.credencial = credencial;
    }

    /* * Nota: Se você também precisar de um construtor para *registrar* um novo usuário,
     * você poderia criar um segundo construtor que recebe apenas os campos necessários
     * para o INSERT (ex: nome, email, senha, sexo, dataNascimento),
     * já que os outros têm valores DEFAULT ou são IDENTITY.
     */

    public boolean login() {
        System.out.println(nome + " fez login.");
        return true;
    }

    public void logout() {
        System.out.println(nome + " fez logout.");
    }


    public int getId_User() {
        return id_User;
    }

    public void setId_User(int id_User) {
        this.id_User = id_User;
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

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
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

    /**
     * Verifica se a conta está ativa.
     * Este método agora existe para todos os filhos (Cliente, Funcionario, ADM).
     * @return true se o statusConta for "Ativo", ignorando maiúsculas/minúsculas.
     */
    public boolean isContaAtiva() {
        // Compara a String herdada
        return this.statusConta != null && this.statusConta.equalsIgnoreCase("Ativo");
    }

}