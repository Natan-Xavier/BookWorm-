package biblioteca;

// Importações necessárias para os construtores
import java.time.LocalDate;
import java.time.LocalDateTime;


public class Administrador extends Funcionario {

    // CONSTRUTORES

    /**
     * Construtor COMPLETO.
     * Usado para criar um objeto ADM com dados JÁ VINDOS do banco (ex: SELECT).
     */
    public Administrador(int id_User, String nome, String email, String senha,
                         String sexo, LocalDate dataNascimento, LocalDateTime dataCadastro,
                         String statusConta, String credencial) {

        super(id_User, nome, email, senha, sexo, dataNascimento, dataCadastro, statusConta, credencial);
    }

    /**
     * Construtor de REGISTRO (Novo ADM).
     * Usado para criar um novo ADM ANTES de salvar no banco (ex: INSERT).
     */
    public Administrador(String nome, String email, String senha, String sexo,
                         LocalDate dataNascimento) {

        // Chama o construtor COMPLETO da classe PAI (Funcionario),
        super(0, // ID é 0, pois o banco (IDENTITY) irá gerá-lo
                nome,
                email,
                senha,
                sexo,
                dataNascimento,
                LocalDateTime.now(),
                "Ativo",
                "ADM");
    }


    // MÉTODOS ESPECÍFICOS DO ADM

    public void gerarRelatorios() {
        System.out.println("Gerando relatórios simulados...");
    }

    // O método 'gerenciarStatusCliente' é herdado automaticamente de Funcionario
}