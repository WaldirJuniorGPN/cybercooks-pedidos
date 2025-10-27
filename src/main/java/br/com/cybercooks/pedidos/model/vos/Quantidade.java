package br.com.cybercooks.pedidos.model.vos;

import br.com.cybercooks.pedidos.exception.RegraDeNegocioException;
import jakarta.persistence.Embeddable;

@Embeddable
public record Quantidade(Integer value) {

    public Quantidade {
        if (value == null) {
            throw new RegraDeNegocioException("A quantidade não pode ser nula");
        }

        if (value.compareTo(0) < 0) {
            throw new RegraDeNegocioException("A quantidade deve ser maior ou igual a zero");
        }
    }
}
