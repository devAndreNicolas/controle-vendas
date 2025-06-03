package org.example.controle_vendas.model; // Ou outro pacote adequado

public enum UnidadeMedida {
    UN("Unidade"), // Unidade
    KG("Quilograma"), // Quilograma
    L("Litro"), // Litro
    M("Metro"), // Metro
    PCT("Pacote"), // Pacote
    CX("Caixa"), // Caixa
    DZ("Dúzia"); // Dúzia

    private final String descricao;

    UnidadeMedida(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }

    public static UnidadeMedida fromDescricao(String text) {
        for (UnidadeMedida um : UnidadeMedida.values()) {
            if (um.descricao.equalsIgnoreCase(text) || um.name().equalsIgnoreCase(text)) {
                return um;
            }
        }
        return null;
    }
}