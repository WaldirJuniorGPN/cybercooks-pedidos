package br.com.cybercooks.pedidos.repository;

import br.com.cybercooks.pedidos.model.Pedido;
import br.com.cybercooks.pedidos.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    Optional<Pedido> findByIdAndStatus(Long id, Status status);
}
