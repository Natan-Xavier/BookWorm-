package biblioteca;

public class Cliente extends Usuario {
    private boolean status_Conta = true;

    public Cliente(int id, String nome, String email, String senha) {
        super(id, nome, email, senha);
    }

    // Construtor usado no LoginUI (apenas para demonstração)
    public Cliente(String email, String senha) {
        super(0, email != null && email.contains("@") ? email.substring(0, email.indexOf("@")) : email, email, senha);
    }

    // Override para compatibilidade com LoginUI (retorna boolean)
    @Override
    public boolean login() {
        System.out.println(nome + " fez login.");
        // Em uma aplicação real aqui viria verificação com BD; mantemos true para demo.
        return true;
    }

    public void alugarLivro(biblioteca.Classes.Livro livro) {
        if (!status_Conta) {
            System.out.println("Conta bloqueada! Não é possível alugar.");
            return;
        }
        biblioteca.DAO.AluguelDAO dao = new biblioteca.DAO.AluguelDAO();
        dao.registrarAluguel(this, livro);
    }

    public void devolverLivro(Livro livro) {
        biblioteca.DAO.AluguelDAO dao = new AluguelDAO();
        dao.registrarDevolucao(this, livro);
    }

    public void bloquearConta() { status_Conta = false; }
    public void desbloquearConta() { status_Conta = true; }
}
