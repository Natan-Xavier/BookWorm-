package biblioteca;

// Importações necessárias da classe Pai
import java.time.LocalDate;
import java.time.LocalDateTime;


public class Cliente extends Usuario {

    // CONSTRUTORES

    public Cliente(int id_User, String nome, String email, String senha,
                   String sexo, LocalDate dataNascimento, LocalDateTime dataCadastro,
                   String statusConta, String credencial) {

        // Chama o construtor da classe Pai (Usuario) com todos os dados.
        super(id_User, nome, email, senha, sexo, dataNascimento, dataCadastro, statusConta, credencial);
    }

    /**
     * Construtor de REGISTRO (Novo Cliente).
     * Usado para criar um novo cliente ANTES de salvar no banco (ex: INSERT).
     * O ID (0) será ignorado pelo IDENTITY, e as datas/status/credencial são definidos por padrão.
     */
    public Cliente(String nome, String email, String senha, String sexo, LocalDate dataNascimento) {

        super(0,
                nome,
                email,
                senha,
                sexo,
                dataNascimento,
                LocalDateTime.now(),
                "Ativo",
                "Cliente");
    }

    public void alugarLivro(Livro livro) {
        // Lógica atualizada para usar o statusConta (String) herdado
        if (!isContaAtiva()) {
            System.out.println("Conta bloqueada! Não é possível alugar.");
            return;
        }
        // Assumindo que AluguelDAO existe e está correto
        // AluguelDAO dao = new AluguelDAO();
        // dao.registrarAluguel(this, livro);
        System.out.println("Lógica de aluguel chamada para: " + this.nome);
    }

    public void devolverLivro(Livro livro) {
        // Assumindo que AluguelDAO existe e está correto
        // AluguelDAO dao = new AluguelDAO();
        // dao.registrarDevolucao(this, livro);
        System.out.println("Lógica de devolução chamada para: " + this.nome);
    }


    // MÉTODOS DE CONTROLE DE CONTA

    /**
     * Altera o status da conta para "Inativo".
     */
    public void bloquearConta() {
        this.statusConta = "Inativo"; // Usa o atributo (String) herdado
    }

    /**
     * Altera o status da conta para "Ativo".
     */
    public void desbloquearConta() {
        this.statusConta = "Ativo"; // Usa o atributo (String) herdado
    }

    /**
     * Verifica se a conta está ativa.
     * @return true se o statusConta for "Ativo", ignorando maiúsculas/minúsculas.
     */
    public boolean isContaAtiva() {
        // Compara a String herdada
        return this.statusConta != null && this.statusConta.equalsIgnoreCase("Ativo");
    }


    //  MÉTODOS MANTIDOS (para integração com UI)

    @Override
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String getSenha() {
        return senha;
    }
}