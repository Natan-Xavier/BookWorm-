package com.biblioteca.service;

import com.biblioteca.dao.AluguelDAO;
import com.biblioteca.dao.UsuarioDAO;
import com.biblioteca.model.Usuario;

import java.sql.SQLException;

/**
 * Camada de Serviço para Usuários.
 */
public class UsuarioService {

    private UsuarioDAO usuarioDAO;
    private AluguelDAO aluguelDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
        this.aluguelDAO = new AluguelDAO();
    }

    /**
     * Processa a lógica de cadastro de um novo usuário.
     * @param usuario O objeto Usuario vindo da interface (WebSite).
     * @throws ServiceException Se uma regra de negócio for violada (ex: e-mail duplicado).
     */
    public void cadastrar(Usuario usuario) throws ServiceException {
        try {
            if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
                throw new ServiceException("O nome é obrigatório.");
            }
            if (usuario.getEmail() == null || !usuario.getEmail().contains("@")) {
                throw new ServiceException("E-mail inválido.");
            }
            // (Aqui também entraria a lógica de HASHING da senha, que podemos adicionar depois)

            //O Service garante os valores padrão, não a interface
            usuario.setStatusConta("Ativo");

            //Se o ADM não definiu uma credencial, ela é 'Cliente'
            if (usuario.getCredencial() == null || usuario.getCredencial().isEmpty()) {
                usuario.setCredencial("Cliente");
            }
            //Tenta cadastrar no banco
            usuarioDAO.cadastrar(usuario);

        } catch (SQLException e) {
            //Tratamento de Erro

            // Erro 2627 ou 2601 = Violação de Chave Única (UNIQUE)
            if (e.getErrorCode() == 2627 || e.getErrorCode() == 2601) {
                // Traduz o erro técnico em uma mensagem amigável
                throw new ServiceException("Este e-mail já está cadastrado no sistema.");
            }
            // Se for outro erro de SQL, lança um erro genérico
            throw new ServiceException("Erro ao tentar cadastrar usuário no banco de dados.", e);
        }
    }

    /**
     * Processa a lógica de login de um usuário.
     * @param email O e-mail (do formulário web).
     * @param senha A senha (do formulário web).
     * @return O objeto Usuario preenchido (para ser salvo na Sessão Web).
     * @throws ServiceException Se o login falhar (ex: conta bloqueada).
     */
    public Usuario login(String email, String senha) throws ServiceException {
        try {
            // (Aqui entraria o HASH da 'senha' antes de enviar ao DAO)

            //1. Tenta autenticar no banco
            Usuario usuario = usuarioDAO.login(email, senha);

            //2. Regra: Usuário ou senha inválidos?
            if (usuario == null) {
                throw new ServiceException("E-mail ou senha inválidos.");
            }

            //3. Regra: Conta bloqueada?
            if ("Bloqueado".equalsIgnoreCase(usuario.getStatusConta())) {
                throw new ServiceException("Esta conta está bloqueada. Contate um administrador.");
            }

            //4. Sucesso!
            return usuario;

        } catch (SQLException e) {
            throw new ServiceException("Erro no banco ao tentar fazer login.", e);
        }
    }

    /**
     * Remove um cliente e todo o seu histórico.
     * (Implementando a regra que você pediu para eu lembrar).
     * @param idCliente O ID do cliente a ser removido.
     * @throws ServiceException Se a remoção falhar.
     */
    public void removerCliente(int idCliente) throws ServiceException {
        try {
            //1. Remove o histórico de aluguéis PRIMEIRO
            aluguelDAO.removerHistoricoPorUsuario(idCliente);

            //2. Remove o usuário DEPOIS
            usuarioDAO.remover(idCliente);

        } catch (SQLException e) {
            throw new ServiceException("Erro ao remover cliente e seu histórico.", e);
        }
    }

    /**
     * Atualiza o perfil de um usuário.
     * @param usuario O objeto com os dados a atualizar (deve incluir o ID).
     * @throws ServiceException Se a validação ou atualização falhar.
     */
    public void atualizarPerfil(Usuario usuario) throws ServiceException {
        try {
            if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
                throw new ServiceException("O nome é obrigatório.");
            }
            // (Aqui entraria a lógica de HASH da nova senha, se ela foi alterada)

            usuarioDAO.atualizarPerfil(usuario);

        } catch (SQLException e) {
            if (e.getErrorCode() == 2627 || e.getErrorCode() == 2601) {
                throw new ServiceException("Este e-mail já está sendo usado por outra conta.");
            }
            throw new ServiceException("Erro ao atualizar perfil no banco de dados.", e);
        }
    }

    /**
     * Atualiza o status de um usuário (ex: Bloquear).
     * @param idUsuario O ID do usuário-alvo.
     * @param novoStatus O status (ex: "Bloqueado", "Ativo").
     * @throws ServiceException Se a atualização falhar.
     */
    public void atualizarStatus(int idUsuario, String novoStatus) throws ServiceException {
        try {
            if (novoStatus == null || (!novoStatus.equals("Ativo") && !novoStatus.equals("Bloqueado"))) {
                throw new ServiceException("Status inválido. Use 'Ativo' ou 'Bloqueado'.");
            }
            usuarioDAO.atualizarStatus(idUsuario, novoStatus);
        } catch (SQLException e) {
            throw new ServiceException("Erro ao atualizar status do usuário.", e);
        }
    }

    /**
     * Lista todos os usuários com a credencial 'Cliente'.
     * @return Lista de objetos Usuario (Clientes).
     * @throws ServiceException
     */
    public List<Usuario> listarClientes() throws ServiceException {
        try {
            return usuarioDAO.listarPorCredencial("Cliente");
        } catch (SQLException e) {
            throw new ServiceException("Erro ao buscar lista de clientes.", e);
        }
    }

    /**
     * Lista TODOS os usuários do sistema.
     * @return Lista de todos os objetos Usuario.
     * @throws ServiceException
     */
    public List<Usuario> listarTodosUsuarios() throws ServiceException {
        try {
            return usuarioDAO.listarTodos();
        } catch (SQLException e) {
            throw new ServiceException("Erro ao buscar lista completa de usuários.", e);
        }
    }

    /**
     * Busca um único usuário pelo ID.
     * @param idUsuario O ID do usuário.
     * @return O objeto Usuario encontrado.
     * @throws ServiceException Se não encontrar ou der erro.
     */
    public Usuario buscarPorId(int idUsuario) throws ServiceException {
        try {
            Usuario usuario = usuarioDAO.buscarPorId(idUsuario);
            if (usuario == null) {
                throw new ServiceException("Usuário não encontrado com o ID: " + idUsuario);
            }
            return usuario;
        } catch (SQLException e) {
            throw new ServiceException("Erro ao buscar usuário por ID.", e);
        }
    }
}
}