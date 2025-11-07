package biblioteca;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class BibliotecaUI extends JFrame {

    private final Cliente cliente;
    private final JTable tabelaLivros = new JTable();
    private final ArrayList<Livro> livros = new ArrayList<>();

    public BibliotecaUI(Cliente cliente) {
        this.cliente = cliente;
        setTitle("Biblioteca - Usuário: " + cliente.getEmail());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        initComponentes();
        carregarLivros();
    }

    private void initComponentes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tabelaLivros.setModel(new DefaultTableModel(new Object[]{"ID", "Título", "Autor", "Disponível"}, 0));
        panel.add(new JScrollPane(tabelaLivros), BorderLayout.CENTER);

        JPanel botoesPanel = new JPanel();
        JButton emprestarBtn = new JButton("Emprestar Livro");
        JButton devolverBtn = new JButton("Devolver Livro");
        botoesPanel.add(emprestarBtn);
        botoesPanel.add(devolverBtn);

        emprestarBtn.addActionListener(this::emprestarLivro);
        devolverBtn.addActionListener(this::devolverLivro);

        panel.add(botoesPanel, BorderLayout.SOUTH);

        add(panel);
    }

    private void carregarLivros() {
        // Simulação de livros
        livros.clear();
        livros.add(new Livro(1, "Java Básico", "Autor A", true));
        livros.add(new Livro(2, "POO em Java", "Autor B", true));
        livros.add(new Livro(3, "Banco de Dados", "Autor C", true));

        atualizarTabela();
    }

    private void atualizarTabela() {
        DefaultTableModel model = (DefaultTableModel) tabelaLivros.getModel();
        model.setRowCount(0);
        for (Livro livro : livros) {
            model.addRow(new Object[]{livro.getId(), livro.getTitulo(), livro.getAutor(), livro.isDisponivel() ? "Sim" : "Não"});
        }
    }

    private void emprestarLivro(ActionEvent e) {
        int linha = tabelaLivros.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um livro para emprestar.");
            return;
        }
        Livro livro = livros.get(linha);
        if (!livro.isDisponivel()) {
            JOptionPane.showMessageDialog(this, "Livro indisponível.");
            return;
        }
        livro.setDisponivel(false);
        JOptionPane.showMessageDialog(this, "Livro emprestado com sucesso!");
        atualizarTabela();
    }

    private void devolverLivro(ActionEvent e) {
        int linha = tabelaLivros.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um livro para devolver.");
            return;
        }
        Livro livro = livros.get(linha);
        if (livro.isDisponivel()) {
            JOptionPane.showMessageDialog(this, "Livro já está disponível.");
            return;
        }
        livro.setDisponivel(true);
        JOptionPane.showMessageDialog(this, "Livro devolvido com sucesso!");
        atualizarTabela();
    }
}