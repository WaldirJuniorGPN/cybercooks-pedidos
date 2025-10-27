package br.com.cybercooks.pedidos.adapter;

import br.com.cybercooks.pedidos.controller.dto.request.PedidoRequest;
import br.com.cybercooks.pedidos.controller.dto.response.PedidoResponse;
import br.com.cybercooks.pedidos.model.Pedido;

public interface PedidoAdapter {

    PedidoResponse pedidoToPedidoResponse(Pedido pedido);

    Pedido PedidoRequestToPedido(PedidoRequest pedidoRequest);

}
