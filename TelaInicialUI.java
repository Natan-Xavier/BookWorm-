package biblioteca;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TelaInicialUI extends JFrame {

    private final Cliente cliente;

    public TelaInicialUI(Cliente cliente) {
        this.cliente = cliente;
        setTitle("Biblioteca Online - Tela Inicial");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        JLabel titulo = new JLabel("Bem-vindo, " + cliente.getNome() + "!");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        JButton alterarDadosBtn = new JButton("Alterar meus dados");
        JButton bibliotecaBtn = new JButton("Acessar biblioteca");
        JButton voltarBtn = new JButton("Sair");

        alterarDadosBtn.addActionListener(this::abrirAlterarDados);
        bibliotecaBtn.addActionListener(this::abrirBiblioteca);
        voltarBtn.addActionListener(this::voltarLogin);

        JPanel painelBotoes = new JPanel(new GridLayout(3, 1, 10, 10));
        painelBotoes.add(alterarDadosBtn);
        painelBotoes.add(bibliotecaBtn);
        painelBotoes.add(voltarBtn);

        setLayout(new BorderLayout(10, 10));
        add(titulo, BorderLayout.NORTH);
        add(painelBotoes, BorderLayout.CENTER);
    }

    private void abrirAlterarDados(ActionEvent e) {
        new AlterarDadosUI(cliente, this).setVisible(true);
        setVisible(false);
    }

    private void abrirBiblioteca(ActionEvent e) {
        new BibliotecaUI(cliente).setVisible(true);
        dispose();
    }

    private void voltarLogin(ActionEvent e) {
        new LoginUI().setVisible(true);
        dispose();
    }
}
