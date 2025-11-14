package com.biblioteca.service;

/**
 * Uma exceção personalizada para a camada de Service.
 * Usada para traduzir erros técnicos (como SQLException)
 * em mensagens amigáveis para a interface (WebSite).
 * * Ex: Em vez de "Violação de UNIQUE KEY 'UQ_Email'",
 * irá lançar "Este e-mail já está cadastrado."
 */
public class ServiceException extends Exception {

    /**
     * Construtor que aceita apenas uma mensagem amigável.
     * @param message A mensagem a ser exibida para o usuário.
     */
    public ServiceException(String message) {
        super(message);
    }

    /**
     * Construtor que "enrola" a exceção original (ex: SQLException).
     * @param message A mensagem amigável.
     * @param cause A exceção original (causa raiz).
     */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}