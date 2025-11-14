package com.biblioteca.service;

import com.biblioteca.dao.AluguelDAO;
import com.biblioteca.dao.ConfiguracoesDAO;
import com.biblioteca.dao.UsuarioDAO;
import com.biblioteca.model.Aluguel;
import com.biblioteca.model.Livro;
import com.biblioteca.model.Usuario;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Camada de Serviço (Regras de Negócio) para Aluguéis.
 */
public class AluguelService {

    private AluguelDAO aluguelDAO;
    private UsuarioDAO usuarioDAO;
    private ConfiguracoesDAO configuracoesDAO;

    public AluguelService() {
        this.aluguelDAO = new AluguelDAO();
        this.usuarioDAO = new UsuarioDAO();
        this.configuracoesDAO = new ConfiguracoesDAO();
    }

    /**
     * Processa a lógica de negócio para alugar um livro.
     * @param idUsuario O ID do cliente que está alugando.
     * @param idLivro   O ID do livro a ser alugado.
     * @throws ServiceException Se qualquer regra de negócio for violada.
     */
    public void alugarLivro(int idUsuario, int idLivro) throws ServiceException {
        try {
            //1: O usuário pode alugar?
            Usuario usuario = usuarioDAO.buscarPorId(idUsuario);

            if (usuario == null) {
                throw new ServiceException("Usuário não encontrado.");
            }
            if (!"Ativo".equalsIgnoreCase(usuario.getStatusConta())) {
                throw new ServiceException("Sua conta está '" + usuario.getStatusConta() + "'. " +
                        "Você não pode alugar livros no momento.");
            }

            //2: Qual o prazo do aluguel? ---
            int prazoEmDias = configuracoesDAO.buscarPrazoPadrao();

            //Calcula a data de devolução (usando a biblioteca java.time, mais moderna)
            LocalDate hoje = LocalDate.now();
            LocalDate dataDevolucao = hoje.plusDays(prazoEmDias);

            //Converte de volta para java.sql.Date
            Date dataPrevistaDevolucaoSql = Date.valueOf(dataDevolucao);

            //Monta o objeto Aluguel
            Aluguel novoAluguel = new Aluguel();

            //(Para o INSERT, só precisamos dos objetos com os IDs)
            Usuario u = new Usuario();
            u.setIdUsuario(idUsuario);
            Livro l = new Livro();
            l.setIdLivro(idLivro);

            novoAluguel.setUsuario(u);
            novoAluguel.setLivro(l);
            novoAluguel.setDataPrevistaDevolucao(dataPrevistaDevolucaoSql);

            aluguelDAO.alugar(novoAluguel);

        } catch (SQLException e) {
            throw new ServiceException("Erro no banco de dados ao tentar processar o aluguel.", e);
        }
    }

    /**
     * Processa a devolução de um livro.
     * @param idAluguel O ID do aluguel a ser marcado como devolvido.
     * @throws ServiceException
     */
    public void devolverLivro(int idAluguel) throws ServiceException {
        try {
            aluguelDAO.devolver(idAluguel);
        } catch (SQLException e) {
            throw new ServiceException("Erro no banco ao tentar devolver o livro.", e);
        }
    }

    /**
     * Busca o histórico de aluguéis de um cliente.
     * @param idUsuario O ID do cliente.
     * @return Uma lista de Alugueis (com Livro e Status preenchidos).
     * @throws ServiceException
     */
    public List<Aluguel> listarHistoricoPorUsuario(int idUsuario) throws ServiceException {
        try {
            return aluguelDAO.buscarHistoricoPorUsuario(idUsuario);
        } catch (SQLException e) {
            throw new ServiceException("Erro ao buscar histórico de aluguéis.", e);
        }
    }

    //MÉTODOS DE ADMINISTRAÇÃO

    /**
     * Atualiza o prazo padrão de aluguel do sistema.
     * @param novoPrazo O número de dias (ex: 31).
     * @throws ServiceException
     */
    public void atualizarPrazoPadrao(int novoPrazo) throws ServiceException {
        try {
            if (novoPrazo <= 0) {
                throw new ServiceException("O prazo deve ser maior que zero.");
            }
            configuracoesDAO.atualizarPrazoPadrao(novoPrazo);
        } catch (SQLException e) {
            throw new ServiceException("Erro ao atualizar o prazo padrão no banco.", e);
        }
    }

    /**
     * Busca o prazo padrão de aluguel do sistema.
     * @return O número de dias (int) do prazo.
     * @throws ServiceException
     */
    public int getPrazoPadrao() throws ServiceException {
        try {
            return configuracoesDAO.buscarPrazoPadrao();
        } catch (SQLException e) {
            throw new ServiceException("Erro ao buscar o prazo padrão no banco.", e);
        }
    }
}