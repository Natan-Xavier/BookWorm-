package biblioteca;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class TesteInserirLivro {

    public static void main(String[] args) {

        // --- Abrir seletor para a capa ---
        JFileChooser seletorCapa = new JFileChooser();
        seletorCapa.setDialogTitle("Selecione a capa do livro (imagem)");
        int resultadoCapa = seletorCapa.showOpenDialog(null);

        if (resultadoCapa != JFileChooser.APPROVE_OPTION) {
            System.out.println("Operação cancelada: nenhuma capa selecionada.");
            return;
        }

        File capa = seletorCapa.getSelectedFile();
        System.out.println("📘 Capa selecionada: " + capa.getAbsolutePath());

        // --- Abrir seletor para o arquivo do livro ---
        JFileChooser seletorLivro = new JFileChooser();
        seletorLivro.setDialogTitle("Selecione o arquivo do livro (PDF, DOCX, etc.)");
        int resultadoLivro = seletorLivro.showOpenDialog(null);

        if (resultadoLivro != JFileChooser.APPROVE_OPTION) {
            System.out.println("Operação cancelada: nenhum livro selecionado.");
            return;
        }

        File livro = seletorLivro.getSelectedFile();
        System.out.println("📗 Livro selecionado: " + livro.getAbsolutePath());

        // --- IDs de relacionamento (devem existir no banco) ---
        int idEditora = 1;
        int idAutor = 1;
        int idGenero = 1;

        try (InputStream capaStream = new FileInputStream(capa);
             InputStream livroStream = new FileInputStream(livro)) {

            LivroDAO dao = new LivroDAO();
            dao.adicionarLivroCompleto(
                    JOptionPane.showInputDialog("Título do livro:"),
                    JOptionPane.showInputDialog("ISBN (13 dígitos):"),
                    JOptionPane.showInputDialog("Sinopse:"),
                    Integer.parseInt(JOptionPane.showInputDialog("Ano de publicação:")),
                    capaStream, capa.length(),
                    Integer.parseInt(JOptionPane.showInputDialog("Número de páginas:")),
                    livroStream, livro.length(),
                    idEditora,
                    idAutor,
                    idGenero
            );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao inserir o livro:\n" + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
