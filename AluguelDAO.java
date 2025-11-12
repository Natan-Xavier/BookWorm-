package biblioteca;

public class AluguelDAO {
    public void registrarAluguel(Cliente c, Livro l) {
        System.out.println("[SQL] INSERT INTO Aluguel ... para " + c.nome + " e livro " + l.getTitulo());
    }
    public void registrarDevolucao(Cliente c, Livro l) {
        System.out.println("[SQL] UPDATE Aluguel ... devolução de " + l.getTitulo());
    }
}