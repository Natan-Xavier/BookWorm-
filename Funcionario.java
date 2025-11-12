package biblioteca;

// Importações necessárias para os construtores
import java.time.LocalDate;
import java.time.LocalDateTime;


public class Funcionario extends Usuario {

    // CONSTRUTORES

    public Funcionario(int id_User, String nome, String email, String senha,
                       String sexo, LocalDate dataNascimento, LocalDateTime dataCadastro,
                       String statusConta, String credencial) {

        // Chama o construtor da classe Pai (Usuario) com todos os 9 campos base
        super(id_User, nome, email, senha, sexo, dataNascimento, dataCadastro, statusConta, credencial);

    }

    /**
     * Construtor de REGISTRO (Novo Funcionario).
     * Usado para criar um novo funcionário ANTES de salvar no banco (ex: INSERT).
     */
    public Funcionario(String nome, String email, String senha, String sexo,
                       LocalDate dataNascimento) {

        // Chama o construtor Pai, preenchendo os campos com valores padrão
        super(0, // ID é 0, pois o banco (IDENTITY) irá gerá-lo
                nome,
                email,
                senha,
                sexo,
                dataNascimento,
                LocalDateTime.now(),
                "Ativo",
                "Funcionario");
    }


    // MÉTODOS ESPECÍFICOS DO FUNCIONÁRIO

    /*
     * Gerencia o status da conta de um objeto Cliente.
     * Este método funcionará com a nova classe Cliente.
     */



    // REVISAR A PARTIR DAQUI POIS NÃO HÁ DAO IMPLEMENTADO AINDA, E NÃO HÁ CAMPOS ESPECÍFICOS NO FUNCIONÁRIO
    // ALÉM DISSO, NÃO SEI O QUE PRECISA COLOCAR AQUI, AS FUNÇÕES DE FUNCIONÁRIO
    public void gerenciarStatusCliente(Cliente c, boolean ativo) {
        if (ativo) {
            c.desbloquearConta();
        } else {
            c.bloquearConta();
        }
        // Seria bom adicionar um DAO aqui para salvar a mudança no banco
        System.out.println("Status da conta de " + c.getNome() + " alterado para: " + (ativo ? "Ativa" : "Bloqueada"));
    }

    // (Não há getters e setters, pois não há campos específicos)
}