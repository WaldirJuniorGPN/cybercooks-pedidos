package br.com.cybercooks.pedidos.service.impl;

import br.com.cybercooks.pedidos.adapter.PedidoAdapter;
import br.com.cybercooks.pedidos.controller.dto.request.PedidoRequest;
import br.com.cybercooks.pedidos.controller.dto.request.StatusRequest;
import br.com.cybercooks.pedidos.controller.dto.response.PedidoResponse;
import br.com.cybercooks.pedidos.model.Pedido;
import br.com.cybercooks.pedidos.repository.PedidoRepository;
import br.com.cybercooks.pedidos.service.PedidoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.cybercooks.pedidos.model.Status.REALIZADO;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoAdapter adapter;
    private final PedidoRepository repository;

    @Override
    public List<PedidoResponse> obterTodos() {
        return repository.findAll()
                .stream()
                .map(adapter::pedidoToPedidoResponse)
                .toList();
    }

    @Override
    public Page<PedidoResponse> obterTodos(Pageable pageable) {
        return repository.findAll(pageable)
                .map(adapter::pedidoToPedidoResponse);
    }

    @Override
    public PedidoResponse obterPorId(Long id) {
        var pedido = buscarPedidoNoBanco(id);

        return adapter.pedidoToPedidoResponse(pedido);
    }

    @Override
    public PedidoResponse criar(PedidoRequest pedidoRequest) {
        var pedido = adapter.PedidoRequestToPedido(pedidoRequest);
        pedido.realizarPedido();
        repository.save(pedido);

        return adapter.pedidoToPedidoResponse(pedido);
    }

    @Override
    public PedidoResponse atualizarStatus(Long id, StatusRequest statusRequest) {
        return null;
    }

    @Override
    public void aprovarPagamentoPedido(Long id) {
        var pedido = repository.findByIdAndStatus(id, REALIZADO)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Pedido de ID: %d não encontrado ou não está apito para ser aprovado", id)));
        pedido.aprovarPagamento();

        repository.save(pedido);
    }

    private Pedido buscarPedidoNoBanco(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Pedido de ID: %d não foi encontrado", id)));
    }
}
