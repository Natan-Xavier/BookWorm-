package biblioteca;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginUI extends JFrame {

    private final JTextField emailField = new JTextField();
    private final JPasswordField senhaField = new JPasswordField();
    private final JButton loginButton = new JButton("Entrar");
    private final JButton cadastrarButton = new JButton("Cadastrar-se");

    public LoginUI() {
        setTitle("Login - Biblioteca Online");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 250);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("E-mail:"), gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1;
        panel.add(senhaField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(loginButton, gbc);

        gbc.gridy++;
        panel.add(cadastrarButton, gbc);

        loginButton.addActionListener(this::fazerLogin);
        // Abre a tela de cadastro passando esta janela como parent e oculta o login
        cadastrarButton.addActionListener(this::actionPerformed);

        add(panel);
    }

    private void fazerLogin(ActionEvent e) {
        String email = emailField.getText().trim();
        String senha = new String(senhaField.getPassword());

        if (email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        biblioteca.Classes.Cliente cliente = new Cliente(email, senha);
        if (cliente.login()) {
            JOptionPane.showMessageDialog(this, "Bem-vindo(a)!");
            new BibliotecaUI(cliente).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "E-mail ou senha incorretos!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actionPerformed(ActionEvent e) {
        new CadastroUsuarioUI(this).setVisible(true);
        this.setVisible(false);
    }
}
