package br.com.cybercooks.pedidos.controller.dto.response;

import br.com.cybercooks.pedidos.model.ItemDoPedido;

public record ItemDoPedidoResponse(Long id,
                                   Integer quantidade,
                                   String descricao) {

    public static ItemDoPedidoResponse from(ItemDoPedido itemDoPedido) {
        return new ItemDoPedidoResponse(
                itemDoPedido.getId(),
                itemDoPedido.obtemQuantidade(),
                itemDoPedido.obtemDescricao()
        );
    }
}
