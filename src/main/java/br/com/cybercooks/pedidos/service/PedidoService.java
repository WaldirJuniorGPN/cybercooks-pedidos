package br.com.cybercooks.pedidos.service;

import br.com.cybercooks.pedidos.controller.dto.request.PedidoRequest;
import br.com.cybercooks.pedidos.controller.dto.request.StatusRequest;
import br.com.cybercooks.pedidos.controller.dto.response.PedidoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PedidoService {

    List<PedidoResponse> obterTodos();

    Page<PedidoResponse> obterTodos(Pageable pageable);

    PedidoResponse obterPorId(Long id);

    PedidoResponse criar(PedidoRequest pedidoRequest);

    PedidoResponse atualizarStatus(Long id, StatusRequest statusRequest);

    void aprovarPagamentoPedido(Long id);
}
