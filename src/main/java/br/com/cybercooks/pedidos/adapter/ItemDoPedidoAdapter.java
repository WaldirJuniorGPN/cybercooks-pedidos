package br.com.cybercooks.pedidos.adapter;

import br.com.cybercooks.pedidos.controller.dto.request.ItemDoPedidoRequest;
import br.com.cybercooks.pedidos.model.ItemDoPedido;

import java.util.List;

public interface ItemDoPedidoAdapter {

    List<ItemDoPedido> listItemDoPedidoRequestToListItemDoPedido(List<ItemDoPedidoRequest> itens);
}
