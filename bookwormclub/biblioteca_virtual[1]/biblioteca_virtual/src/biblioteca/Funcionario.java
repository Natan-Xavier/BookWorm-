package biblioteca;

public class Funcionario extends Usuario {
    protected int matricula;
    public Funcionario(int id, String nome, String email, String senha, int matricula) {
        super(id, nome, email, senha);
        this.matricula = matricula;
    }

    public void gerenciarStatusCliente(Cliente c, boolean ativo) {
        if (ativo) c.desbloquearConta(); else c.bloquearConta();
        System.out.println("Status da conta de " + c.nome + ": " + (ativo ? "Ativa" : "Bloqueada"));
    }
}