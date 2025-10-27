package br.com.cybercooks.pedidos.adapter.impl;

import br.com.cybercooks.pedidos.adapter.ItemDoPedidoAdapter;
import br.com.cybercooks.pedidos.adapter.PedidoAdapter;
import br.com.cybercooks.pedidos.controller.dto.request.PedidoRequest;
import br.com.cybercooks.pedidos.controller.dto.response.PedidoResponse;
import br.com.cybercooks.pedidos.model.Pedido;
import br.com.cybercooks.pedidos.model.vos.DataDaOperacao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static br.com.cybercooks.pedidos.controller.dto.response.PedidoResponse.from;

@Component
@RequiredArgsConstructor
public class PedidoAdapterImpl implements PedidoAdapter {

    private ItemDoPedidoAdapter itemDoPedidoAdapter;

    @Override
    public PedidoResponse pedidoToPedidoResponse(Pedido pedido) {
        return from(pedido);
    }

    @Override
    public Pedido PedidoRequestToPedido(PedidoRequest pedidoRequest) {
        var itemDoPedidoList = itemDoPedidoAdapter.listItemDoPedidoRequestToListItemDoPedido(pedidoRequest.itens());
        var dataHora = new DataDaOperacao(pedidoRequest.dataHora());
        var pedido = new Pedido(dataHora, itemDoPedidoList);

        itemDoPedidoList.forEach(itemDoPedido -> itemDoPedido.setPedido(pedido));
        return pedido;
    }
}
