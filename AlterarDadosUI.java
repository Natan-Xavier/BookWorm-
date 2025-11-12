package biblioteca;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class AlterarDadosUI extends JFrame {

    private static final String URL = "jdbc:sqlserver://NOTE_GOMES:1433;databaseName=teste_Banco1;encrypt=false";
    private static final String USER = "sa";
    private static final String PASSWORD = "32452";

    private final Cliente cliente;
    private final TelaInicialUI parent;

    private final JTextField nomeField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JPasswordField senhaField = new JPasswordField();

    public AlterarDadosUI(Cliente cliente, TelaInicialUI parent) {
        this.cliente = cliente;
        this.parent = parent;

        setTitle("Alterar Dados do Usuário");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        nomeField.setText(cliente.getNome());
        emailField.setText(cliente.getEmail());

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        panel.add(nomeField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("E-mail:"), gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Nova Senha:"), gbc);
        gbc.gridx = 1;
        panel.add(senhaField, gbc);

        JButton salvarBtn = new JButton("Salvar Alterações");
        JButton voltarBtn = new JButton("Voltar");

        salvarBtn.addActionListener(this::salvarAlteracoes);
        voltarBtn.addActionListener(this::voltarTelaInicial);

        gbc.gridy++;
        gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel botoes = new JPanel();
        botoes.add(salvarBtn);
        botoes.add(voltarBtn);
        panel.add(botoes, gbc);

        add(panel);
    }

    private void salvarAlteracoes(ActionEvent e) {
        String novoNome = nomeField.getText().trim();
        String novoEmail = emailField.getText().trim();
        String novaSenha = new String(senhaField.getPassword()).trim();

        if (novoNome.isEmpty() || novoEmail.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome e e-mail não podem estar vazios!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Atualiza o cliente em memória
        cliente.setNome(novoNome);
        cliente.setEmail(novoEmail);
        if (!novaSenha.isEmpty()) cliente.setSenha(novaSenha);

        // Salva no banco
        atualizarNoBanco(cliente, novaSenha.isEmpty());

        JOptionPane.showMessageDialog(this, "Dados atualizados com sucesso!");
        voltarTelaInicial(null);
    }

    private void atualizarNoBanco(Cliente cliente, boolean manterSenhaAntiga) {
        String sql = manterSenhaAntiga ?
                "UPDATE Usuario SET Nome = ?, E_mail = ? WHERE E_mail = ?" :
                "UPDATE Usuario SET Nome = ?, E_mail = ?, Senha = ? WHERE E_mail = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEmail());

            if (manterSenhaAntiga) {
                stmt.setString(3, cliente.getEmail());
            } else {
                stmt.setString(3, cliente.getSenha());
                stmt.setString(4, cliente.getEmail());
            }

            stmt.executeUpdate();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar no banco: " + ex.getMessage(),
                    "Erro de Banco", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void voltarTelaInicial(ActionEvent e) {
        parent.setVisible(true);
        dispose();
    }
}
