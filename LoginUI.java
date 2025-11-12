package biblioteca;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
// As importações java.sql.* foram REMOVIDAS. A UI não mexe mais com banco!

// Importamos as classes que precisamos
import biblioteca.Usuario;
import biblioteca.Cliente;
import biblioteca.Funcionario;
import biblioteca.Administrador;
import biblioteca.UsuarioDAO;

public class LoginUI extends JFrame {

    // 1. DADOS DO BANCO REMOVIDOS
    // A classe ConexaoDAO cuida disso agora.

    // 2. COMPONENTES DA UI (Sem alteração)
    private final JTextField emailField = new JTextField(20);
    private final JPasswordField senhaField = new JPasswordField(20);
    private final JButton loginButton = new JButton("Entrar");
    private final JButton cadastrarButton = new JButton("Cadastrar-se");

    // 3. ADICIONAMOS A REFERÊNCIA AO DAO
    private final UsuarioDAO usuarioDAO;

    public LoginUI() {
        setTitle("Login - Biblioteca Online");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);
        setResizable(false);

        // 4. INSTANCIAMOS O DAO NO CONSTRUTOR
        this.usuarioDAO = new UsuarioDAO();

        initComponents();
    }

    private void initComponents() {
        // Seu código de layout (initComponents) estava perfeito.
        // Nenhuma alteração aqui.
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
        cadastrarButton.addActionListener(this::abrirCadastro);

        add(panel);
    }


    /**
     * Método fazerLogin TOTALMENTE refatorado para usar o UsuarioDAO.
     */
    private void fazerLogin(ActionEvent e) {
        String email = emailField.getText().trim();
        String senha = new String(senhaField.getPassword()).trim();

        if (email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 5. NOVA LÓGICA DE LOGIN
        // Removemos todo o bloco try-with-resources (Connection, PreparedStatement, etc.)

        // Chamamos o DAO. Ele que faz o trabalho sujo.
        Usuario usuarioLogado = usuarioDAO.validarLogin(email, senha);

        // Verificamos o resultado
        if (usuarioLogado != null) {

            // Verificação BÔNUS (graças ao Passo 1)
            if (!usuarioLogado.isContaAtiva()) {
                JOptionPane.showMessageDialog(this,
                        "Sua conta está '" + usuarioLogado.getStatusConta() + "'.\nContate o suporte.",
                        "Conta Inativa", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // ✅ Login bem-sucedido!
            JOptionPane.showMessageDialog(this, "Bem-vindo(a), " + usuarioLogado.getNome() + "!");

            // 6. ROTEAMENTO DE USUÁRIO
            // Verificamos qual TIPO de usuário o DAO nos retornou

            if (usuarioLogado instanceof Administrador) {
                // Abre a tela de ADM
                // Ex: new TelaPrincipalADM((Administrador) usuarioLogado).setVisible(true);
                System.out.println("LOGADO COMO ADM (Implementar tela)");
                // Por enquanto, vamos abrir a tela inicial
                new TelaInicialUI((Usuario) usuarioLogado).setVisible(true); // Ajuste se TelaInicialUI só aceitar Cliente

            } else if (usuarioLogado instanceof Funcionario) {
                // Abre a tela de Funcionário
                // Ex: new TelaPrincipalFuncionario((Funcionario) usuarioLogado).setVisible(true);
                System.out.println("LOGADO COMO FUNCIONÁRIO (Implementar tela)");
                // Por enquanto, vamos abrir a tela inicial
                new TelaInicialUI((Usuario) usuarioLogado).setVisible(true); // Ajuste se TelaInicialUI só aceitar Cliente

            } else { // é Cliente
                // Abre a tela de Cliente
                new TelaInicialUI((Cliente) usuarioLogado).setVisible(true);
            }

            dispose(); // Fecha a tela de login

        } else {
            // Login falhou (DAO retornou null)
            JOptionPane.showMessageDialog(this, "E-mail ou senha inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
        }

        // O bloco catch (SQLException) não é mais necessário aqui,
        // pois o UsuarioDAO já trata os erros de SQL.
    }

    private void abrirCadastro(ActionEvent e) {
        // Nenhuma alteração necessária aqui
        // Assumindo que CadastroUsuarioUI existe
        // new CadastroUsuarioUI(this).setVisible(true);
        // this.setVisible(false);
        System.out.println("Abrir tela de cadastro...");
    }
}