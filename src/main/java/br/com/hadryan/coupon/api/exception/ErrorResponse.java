package br.com.hadryan.coupon.api.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Envelope padrão para todas as respostas de erro da API.
 *
 * @param message mensagem descritiva do erro
 * @param data    payload da resposta
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse<T>(
        String message,
        T data
) {
    public static <T> ErrorResponse<T> error(String message) {
        return new ErrorResponse<>(message, null);
    }

    public static <T> ErrorResponse<T> error(String message, T data) {
        return new ErrorResponse<>(message, data);
    }
}