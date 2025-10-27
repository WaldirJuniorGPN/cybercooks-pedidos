package br.com.cybercooks.pedidos.controller.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;
import java.util.List;

public record PedidoRequest(

        @NotNull(message = "A data/hora não pode ser nula")
        @PastOrPresent(message = "A data/hora não pode estar no futuro")
        LocalDateTime dataHora,

        @Valid
        List<ItemDoPedidoRequest> itens

) {
}
