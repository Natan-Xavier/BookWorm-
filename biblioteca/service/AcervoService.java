package com.biblioteca.service;

import com.biblioteca.dao.AutorDAO;
import com.biblioteca.dao.EditoraDAO;
import com.biblioteca.dao.GeneroDAO;
import com.biblioteca.dao.LivroDAO;
import com.biblioteca.model.Autor;
import com.biblioteca.model.Editora;
import com.biblioteca.model.Genero;
import com.biblioteca.model.Livro;

import java.sql.SQLException;
import java.util.List;

/**
 * Camada de Serviço (Regras de Negócio) para o Acervo.
 */
public class AcervoService {

    private LivroDAO livroDAO;
    private AutorDAO autorDAO;
    private GeneroDAO generoDAO;
    private EditoraDAO editoraDAO;

    public AcervoService() {
        this.livroDAO = new LivroDAO();
        this.autorDAO = new AutorDAO();
        this.generoDAO = new GeneroDAO();
        this.editoraDAO = new EditoraDAO();
    }

    //MÉTODOS PARA POPULAR O WEB (Dropdowns)

    /**
     * Busca a lista completa de autores.
     * (Usado para preencher <select> no formulário de Livro).
     */
    public List<Autor> listarAutores() throws ServiceException {
        try {
            return autorDAO.listarTodos();
        } catch (SQLException e) {
            throw new ServiceException("Erro ao listar autores do banco de dados.", e);
        }
    }

    /**
     * Busca a lista completa de gêneros.
     * (Usado para preencher <select> no formulário de Livro).
     */
    public List<Genero> listarGeneros() throws ServiceException {
        try {
            return generoDAO.listarTodos();
        } catch (SQLException e) {
            throw new ServiceException("Erro ao listar gêneros do banco de dados.", e);
        }
    }

    /**
     * Busca a lista completa de editoras.
     * (Usado para preencher <select> no formulário de Livro).
     */
    public List<Editora> listarEditoras() throws ServiceException {
        try {
            return editoraDAO.listarTodos();
        } catch (SQLException e) {
            throw new ServiceException("Erro ao listar editoras do banco de dados.", e);
        }
    }


    //MÉTODOS DE GERENCIAMENTO DE LIVRO

    /**
     * Valida e cadastra um novo livro.
     * @param livro O objeto Livro (com autores, generos, etc.) vindo da interface.
     * @throws ServiceException Se uma regra de negócio for violada (ex: ISBN duplicado).
     */
    public void cadastrarLivro(Livro livro) throws ServiceException {
        try {
            //Validação
            validarLivro(livro);

            // (Validação de upload de arquivo (capa/pdf) seria feita aqui também)
            // Ex: if (livro.getCapaPath() == null) { throw new ... }

            //Se passou nas regras, tenta cadastrar
            livroDAO.cadastrar(livro);

        } catch (SQLException e) {
            //Tratamento de Erro
            // Erro 2627 ou 2601 = Violação de UNIQUE (do ISBN)
            if (e.getErrorCode() == 2627 || e.getErrorCode() == 2601) {
                throw new ServiceException("Erro: O ISBN '" + livro.getIsbn() + "' já está cadastrado.", e);
            }
            // Outros tipos de erros
            throw new ServiceException("Erro ao cadastrar livro no banco de dados.", e);
        }
    }

    /**
     * Valida e edita um livro existente.
     * @param livro O objeto Livro com os dados atualizados (deve conter o ID).
     * @throws ServiceException Se uma regra de negócio for violada.
     */
    public void editarLivro(Livro livro) throws ServiceException {
        try {
            //Valida os mesmos campos do cadastro
            validarLivro(livro);

            //Valida se o ID existe
            if (livro.getIdLivro() == 0) {
                throw new ServiceException("ID do livro inválido para edição.");
            }
            //(Validação de upload de NOVOS arquivos (capa/pdf) seria feita aqui)

            //Tenta editar no banco
            livroDAO.editar(livro);

        } catch (SQLException e) {
            // Erro 2627 ou 2601 = Violação de UNIQUE (do ISBN)
            if (e.getErrorCode() == 2627 || e.getErrorCode() == 2601) {
                throw new ServiceException("Erro: O ISBN '" + livro.getIsbn() + "' já está cadastrado em outro livro.", e);
            }
            // Outros tipos de erros
            throw new ServiceException("Erro ao editar livro no banco de dados.", e);
        }
    }

    /**
     * Remove um livro do acervo.
     * O DAO (LivroDAO) já cuida da transação (remover de Aluguel, etc.).
     * @param idLivro O ID do livro a ser removido.
     * @throws ServiceException Se a remoção falhar.
     */
    public void removerLivro(int idLivro) throws ServiceException {
        try {
            livroDAO.remover(idLivro);
        } catch (SQLException e) {
            throw new ServiceException("Erro ao remover livro do banco de dados.", e);
        }
    }

    /**
     * Busca um livro pelo seu termo (título, autor, gênero).
     * @param termo O termo (String) a ser buscado.
     * @return Uma lista de Livros (simplificada).
     * @throws ServiceException Se a busca falhar.
     */
    public List<Livro> buscarLivroPorTermo(String termo) throws ServiceException {
        try {
            if (termo == null || termo.trim().length() < 3) {
                throw new ServiceException("Termo de busca deve ter pelo menos 3 caracteres.");
            }
            return livroDAO.buscarPorTermo(termo);
        } catch (SQLException e) {
            throw new ServiceException("Erro ao buscar livros por termo.", e);
        }
    }

    /**
     * Busca um livro completo (com autores/gêneros) pelo ID.
     * @param idLivro O ID do livro.
     * @return O objeto Livro completo.
     * @throws ServiceException Se o livro não for encontrado ou falhar.
     */
    public Livro buscarLivroPorId(int idLivro) throws ServiceException {
        try {
            Livro livro = livroDAO.buscarPorId(idLivro);
            if (livro == null) {
                throw new ServiceException("Livro com ID " + idLivro + " não encontrado.");
            }
            return livro;
        } catch (SQLException e) {
            throw new ServiceException("Erro ao buscar livro por ID.", e);
        }
    }

    //MÉTODOS DE GERENCIAMENTO DE "APOIO"

    /**
     * Remove um autor, aplicando as regras de negócio.
     * @param idAutor O ID do autor a ser removido.
     * @throws ServiceException Se a remoção falhar por uma regra de negócio.
     */
    public void removerAutor(int idAutor) throws ServiceException {
        try {
            autorDAO.remover(idAutor);
        } catch (SQLException e) {
            //Tratamento de FK
            if (e.getErrorCode() == 547) { // Erro 547 = Violação de FOREIGN KEY
                throw new ServiceException("Este autor não pode ser removido, pois ele está " +
                        "associado a um ou mais livros cadastrados.");
            }
            throw new ServiceException("Erro ao remover autor do banco de dados.", e);
        }
    }

    /**
     * Remove um gênero, aplicando as regras de negócio.
     * (Lógica idêntica ao removerAutor)
     * @param idGenero O ID do gênero a ser removido.
     * @throws ServiceException Se a remoção falhar por uma regra de negócio.
     */
    public void removerGenero(int idGenero) throws ServiceException {
        try {
            generoDAO.remover(idGenero);
        } catch (SQLException e) {
            if (e.getErrorCode() == 547) {
                throw new ServiceException("Este gênero não pode ser removido, pois ele está " +
                        "associado a um ou mais livros cadastrados.");
            }
            throw new ServiceException("Erro ao remover gênero do banco de dados.", e);
        }
    }

    /**
     * Remove uma editora, aplicando as regras de negócio.
     * (Lógica idêntica ao removerAutor)
     * @param idEditora O ID da editora a ser removida.
     * @throws ServiceException Se a remoção falhar por uma regra de negócio.
     */
    public void removerEditora(int idEditora) throws ServiceException {
        try {
            editoraDAO.remover(idEditora);
        } catch (SQLException e) {
            if (e.getErrorCode() == 547) {
                throw new ServiceException("Esta editora não pode ser removida, pois ela está " +
                        "associada a um ou mais livros cadastrados.");
            }
            throw new ServiceException("Erro ao remover editora do banco de dados.", e);
        }
    }

    // (Aqui entrariam os métodos de cadastrar/editar Autor, Genero, Editora...)


    //MÉTODOS AUXILIARES (PRIVADOS)

    /**
     * Método helper privado para validar os campos de um Livro.
     * (Evita repetição de código em cadastrarLivro e editarLivro).
     * @param livro O objeto Livro a ser validado.
     * @throws ServiceException Se qualquer regra de negócio for violada.
     */
    private void validarLivro(Livro livro) throws ServiceException {
        if (livro.getTitulo() == null || livro.getTitulo().trim().isEmpty()) {
            throw new ServiceException("O título do livro é obrigatório.");
        }
        if (livro.getIsbn() == null || livro.getIsbn().trim().length() != 13) {

            throw new ServiceException("O ISBN é obrigatório e deve ter 13 dígitos.");
        }
        if (livro.getAutores() == null || livro.getAutores().isEmpty()) {
            throw new ServiceException("O livro deve ter pelo menos um autor.");
        }
        if (livro.getGeneros() == null || livro.getGeneros().isEmpty()) {
            throw new ServiceException("O livro deve ter pelo menos um gênero.");
        }
        if (livro.getEditora() == null) {
            throw new ServiceException("A editora do livro é obrigatória.");
        }
        if (livro.getCapaPath() == null || livro.getCapaPath().trim().isEmpty()) {
            throw new ServiceException("O caminho da capa é obrigatório.");
        }
        if (livro.getLivroPath() == null || livro.getLivroPath().trim().isEmpty()) {
            throw new ServiceException("O caminho do arquivo do livro é obrigatório.");
        }
    }
}