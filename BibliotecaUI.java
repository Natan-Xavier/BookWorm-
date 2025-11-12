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

    private final JButton emprestarBtn = new JButton("Emprestar Livro");
    private final JButton devolverBtn = new JButton("Devolver Livro");
    private final JButton voltarBtn = new JButton("Voltar");
    private final JButton adicionarBtn = new JButton("Adicionar Livro");

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

        // Painel inferior com os botões
        JPanel botoesPanel = new JPanel();
        botoesPanel.add(emprestarBtn);
        botoesPanel.add(devolverBtn);
        botoesPanel.add(adicionarBtn);
        botoesPanel.add(voltarBtn);

        emprestarBtn.addActionListener(this::emprestarLivro);
        devolverBtn.addActionListener(this::devolverLivro);
        voltarBtn.addActionListener(this::voltarTelaLogin);
        adicionarBtn.addActionListener(this::abrirDialogAdicionar);

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

    private void abrirDialogAdicionar(ActionEvent e) {
        String titulo = JOptionPane.showInputDialog(this, "Título do Livro:");
        if (titulo == null) return; // cancelou
        titulo = titulo.trim();
        if (titulo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Título não pode ficar vazio.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String autor = JOptionPane.showInputDialog(this, "Autor do Livro:");
        if (autor == null) return; // cancelou
        autor = autor.trim();
        if (autor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Autor não pode ficar vazio.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int nextId = livros.stream().mapToInt(Livro::getId).max().orElse(0) + 1;
        Livro novo = new Livro(nextId, titulo, autor, true);
        livros.add(novo);
        atualizarTabela();
        JOptionPane.showMessageDialog(this, "Livro adicionado com sucesso!");
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

    private void voltarTelaLogin(ActionEvent e) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Deseja realmente voltar para a tela inicial?",
                "Confirmar saída", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            new LoginUI().setVisible(true);
            dispose();
        }
    }
}
