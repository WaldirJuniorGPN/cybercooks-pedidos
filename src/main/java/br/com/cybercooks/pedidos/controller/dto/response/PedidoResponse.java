package br.com.cybercooks.pedidos.controller.dto.response;

import br.com.cybercooks.pedidos.model.Pedido;
import br.com.cybercooks.pedidos.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponse(Long id,
                             LocalDateTime dataHora,
                             Status status,
                             List<ItemDoPedidoResponse> itens) {

    public static PedidoResponse from(Pedido pedido) {
        return new PedidoResponse(
                pedido.getId(),
                pedido.obterDataEHora(),
                pedido.getStatus(),
                pedido.obtemItensDoPedido()
                        .stream()
                        .map(ItemDoPedidoResponse::from)
                        .toList()
        );
    }
}
