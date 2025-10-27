package br.com.cybercooks.pedidos.controller.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ItemDoPedidoRequest(

        @NotNull(message = "A quantidade não pode ser nula")
        @Min(value = 1, message = "A quantidade do item deve ser maior do que zero")
        Integer quantidade,

        @NotBlank(message = "A descriçaõ não pode estar em branco")
        String descricao
) {
}
