package br.com.cybercooks.pedidos.model;

import br.com.cybercooks.pedidos.model.vos.DataDaOperacao;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static br.com.cybercooks.pedidos.model.Status.PAGO;
import static br.com.cybercooks.pedidos.model.Status.REALIZADO;
import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity(name = "Pedido")
@Table(name = "pedidos")
@DynamicUpdate
@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode
public class Pedido {

    public Pedido(DataDaOperacao dataHora, List<ItemDoPedido> itens) {
        this.dataHora = dataHora;
        this.itens.addAll(itens);
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Getter
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "data_hora", nullable = false))
    private DataDaOperacao dataHora;

    @Enumerated(STRING)
    @Column(name = "status", nullable = false)
    @Getter
    private Status status;

    @OneToMany(cascade = PERSIST, mappedBy = "pedido")
    private final List<ItemDoPedido> itens = new ArrayList<>();

    public LocalDateTime obterDataEHora() {
        return dataHora.value();
    }

    public List<ItemDoPedido> obtemItensDoPedido() {
        return List.copyOf(itens);
    }

    public void realizarPedido() {
        this.status = REALIZADO;
    }

    public void aprovarPagamento() {
        this.status = PAGO;
    }
}
