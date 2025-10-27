package br.com.cybercooks.pedidos.adapter.impl;

import br.com.cybercooks.pedidos.adapter.ItemDoPedidoAdapter;
import br.com.cybercooks.pedidos.controller.dto.request.ItemDoPedidoRequest;
import br.com.cybercooks.pedidos.model.ItemDoPedido;
import br.com.cybercooks.pedidos.model.vos.Descricao;
import br.com.cybercooks.pedidos.model.vos.Quantidade;

import java.util.List;

public class ItemDoPedidoAdapterImpl implements ItemDoPedidoAdapter {

    @Override
    public List<ItemDoPedido> listItemDoPedidoRequestToListItemDoPedido(List<ItemDoPedidoRequest> itens) {
        return itens.stream()
                .map(item -> {
                    var quantidade = new Quantidade(item.quantidade());
                    var descricao = new Descricao(item.descricao());
                    return new ItemDoPedido(quantidade, descricao);
                })
                .toList();
    }
}
