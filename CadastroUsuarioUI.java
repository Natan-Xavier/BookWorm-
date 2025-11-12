package biblioteca;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class CadastroUsuarioUI extends JFrame {

    // 🔹 Configuração do banco local
    private static final String URL = "jdbc:sqlserver://NOTE_GOMES:1433;databaseName=teste_Banco1;encrypt=false";
    private static final String USER = "sa";
    private static final String PASSWORD = "32452";

    private final LoginUI parent;
    private final JTextField nomeField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JPasswordField senhaField = new JPasswordField();
    private final JComboBox<String> sexoBox = new JComboBox<>(new String[]{"Selecione", "Masculino", "Feminino", "Outro"});
    private final JTextField dataNascField = new JTextField("YYYY-MM-DD");
    private final JButton cadastrarButton = new JButton("Cadastrar");
    private final JButton voltarButton = new JButton("Voltar ao login");

    public CadastroUsuarioUI(LoginUI parent) {
        this.parent = parent;
        setTitle("Cadastro de Usuário - Biblioteca Online");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 420);
        setLocationRelativeTo(parent);
        setResizable(false);
        initComponents();

        // Retorna para o login quando fechar
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (parent != null) parent.setVisible(true);
            }
            @Override
            public void windowClosing(WindowEvent e) {
                if (parent != null) parent.setVisible(true);
            }
        });
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        panel.add(nomeField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("E-mail:"), gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1;
        panel.add(senhaField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Sexo:"), gbc);
        gbc.gridx = 1;
        panel.add(sexoBox, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Data de Nascimento:"), gbc);
        gbc.gridx = 1;
        panel.add(dataNascField, gbc);

        gbc.gridy++;
        gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel botoes = new JPanel();
        botoes.add(cadastrarButton);
        botoes.add(voltarButton);
        panel.add(botoes, gbc);

        cadastrarButton.addActionListener(this::cadastrarUsuario);
        voltarButton.addActionListener(this::voltarAoLogin);

        add(panel);
    }

    private void cadastrarUsuario(ActionEvent event) {
        String nome = nomeField.getText().trim();
        String email = emailField.getText().trim();
        String senha = new String(senhaField.getPassword());
        String sexo = (String) sexoBox.getSelectedItem();
        String dataNascTexto = dataNascField.getText().trim();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() ||
                dataNascTexto.isEmpty() || dataNascTexto.equals("YYYY-MM-DD") || sexoBox.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this,
                    "Todos os campos são obrigatórios!\nPor favor, preencha todos os dados antes de continuar.",
                    "Campos obrigatórios", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validarEmail(email)) {
            JOptionPane.showMessageDialog(this,
                    "E-mail inválido! Digite um e-mail no formato correto (ex: usuario@exemplo.com).",
                    "Erro de E-mail", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!validarSenha(senha)) {
            JOptionPane.showMessageDialog(this,
                    "Senha inválida!\nA senha deve ter:\n" +
                            "- Entre 8 e 128 caracteres\n" +
                            "- Pelo menos uma letra maiúscula\n" +
                            "- Pelo menos uma letra minúscula\n" +
                            "- Pelo menos um número\n" +
                            "- Pelo menos um caractere especial (@, $, !, %, *, ?, &)",
                    "Erro de Senha", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            LocalDate dataNasc = LocalDate.parse(dataNascTexto);
            inserirNoBanco(nome, email, senha, sexo, dataNasc);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Data de nascimento inválida! Use o formato YYYY-MM-DD.",
                    "Erro de Data", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void inserirNoBanco(String nome, String email, String senha, String sexo, LocalDate dataNasc) {
        // 🔹 Verifica se o e-mail já existe
        String sqlVerifica = "SELECT COUNT(*) FROM Usuario WHERE E_mail = ?";
        // 🔹 Insere com nome da coluna corrigido (idUsuario é identidade, não precisa inserir)
        String sqlInsert = "INSERT INTO Usuario (Nome, E_mail, Senha, Sexo, data_Nasc) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmtVerifica = conn.prepareStatement(sqlVerifica);
             PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert)) {

            stmtVerifica.setString(1, email);
            ResultSet rs = stmtVerifica.executeQuery();
            rs.next();

            if (rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "Já existe um usuário com este e-mail!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            stmtInsert.setString(1, nome);
            stmtInsert.setString(2, email);
            stmtInsert.setString(3, senha);
            stmtInsert.setString(4, sexo);
            stmtInsert.setDate(5, Date.valueOf(dataNasc));
            stmtInsert.executeUpdate();

            JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();

            if (parent != null) parent.setVisible(true);
            dispose();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao cadastrar usuário:\n" + e.getMessage(),
                    "Erro de Banco", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCampos() {
        nomeField.setText("");
        emailField.setText("");
        senhaField.setText("");
        sexoBox.setSelectedIndex(0);
        dataNascField.setText("YYYY-MM-DD");
    }

    private boolean validarSenha(String senha) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,128}$";
        return Pattern.matches(regex, senha);
    }

    private boolean validarEmail(String email) {
        String regex = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        return Pattern.matches(regex, email);
    }

    private void voltarAoLogin(ActionEvent e) {
        if (parent != null) parent.setVisible(true);
        dispose();
    }
}
