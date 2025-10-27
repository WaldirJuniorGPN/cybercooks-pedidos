package br.com.cybercooks.pedidos.model.vos;

import br.com.cybercooks.pedidos.exception.RegraDeNegocioException;
import jakarta.persistence.Embeddable;

@Embeddable
public record Descricao(String value) {
    public Descricao {
        if (value.isBlank()) {
            throw new RegraDeNegocioException("A descrição não pode estar em branco");
        }
    }
}
