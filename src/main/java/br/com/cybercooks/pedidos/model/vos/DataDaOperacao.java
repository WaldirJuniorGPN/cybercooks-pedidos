package br.com.cybercooks.pedidos.model.vos;

import br.com.cybercooks.pedidos.exception.RegraDeNegocioException;
import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;

@Embeddable
public record DataDaOperacao(LocalDateTime value) {

    public DataDaOperacao {
        if (value.isAfter(LocalDateTime.now())) {
            throw new RegraDeNegocioException("a data/hora do pedido não pode estar no futuro");
        }
    }
}
