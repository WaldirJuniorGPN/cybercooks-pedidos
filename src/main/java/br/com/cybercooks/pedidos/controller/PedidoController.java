package br.com.cybercooks.pedidos.controller;

import br.com.cybercooks.pedidos.controller.dto.request.PedidoRequest;
import br.com.cybercooks.pedidos.controller.dto.response.PedidoResponse;
import br.com.cybercooks.pedidos.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/v1/pedidos")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "Gerenciamento de pedidos")
public class PedidoController {

    private final PedidoService service;

    @Operation(
            summary = "Obtém uma lista paginada",
            description = "Retorna uma lista paginada de todos os pedidos, independente do seu status"
    )
    @GetMapping
    public ResponseEntity<Page<PedidoResponse>> obterTodos(Pageable pageable) {
        var pageResponse = service.obterTodos(pageable);

        return ResponseEntity.ok(pageResponse);
    }

    @Operation(
            summary = "Obtém uma lista não paginada",
            description = "Retorna uma lista não paginada de todos os pedidos, independente do seu status"
    )
    @GetMapping("/listar")
    public ResponseEntity<List<PedidoResponse>> obterTodos() {
        var listResponse = service.obterTodos();

        return ResponseEntity.ok(listResponse);
    }

    @Operation(
            summary = "Obtém o pedido através do seu ID"
    )
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> obterPorId(@PathVariable Long id) {
        var response = service.obterPorId(id);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Cria um novo pedido"
    )
    @PostMapping
    public ResponseEntity<PedidoResponse> criar(@Valid @RequestBody PedidoRequest request) {
        var response = service.criar(request);
        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @Operation(
            summary = "Aprova o pagamento do Pedido",
            description = "Altera o status do pedido para PAGO"
    )
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> aprovarPagamento(@PathVariable Long id) {
        service.aprovarPagamentoPedido(id);

        return ResponseEntity.noContent().build();
    }
}
