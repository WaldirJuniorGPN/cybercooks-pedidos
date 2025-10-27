package br.com.cybercooks.pedidos.model;

import br.com.cybercooks.pedidos.model.vos.Descricao;
import br.com.cybercooks.pedidos.model.vos.Quantidade;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity(name = "ItemDoPedido")
@Table(name = "itens_dos_pedidos")
@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode
public class ItemDoPedido {

    public ItemDoPedido(Quantidade quantidade, Descricao descricao) {
        this.quantidade = quantidade;
        this.descricao = descricao;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Getter
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "quantidade", nullable = false))
    private Quantidade quantidade;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "descricao"))
    private Descricao descricao;

    @ManyToOne(optional = false)
    @Setter
    private Pedido pedido;

    public Integer obtemQuantidade() {
        return quantidade.value();
    }

    public String obtemDescricao() {
        return descricao.value();
    }
}
