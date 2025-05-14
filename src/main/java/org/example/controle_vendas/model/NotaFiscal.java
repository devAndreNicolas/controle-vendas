package org.example.controle_vendas.model;

import java.time.LocalDateTime;

public class NotaFiscal {
    private int notaFiscalId;
    private int vendaId;
    private String numero;
    private String serie;
    private LocalDateTime dataEmissao;

    public NotaFiscal() {}

    public NotaFiscal(int notaFiscalId, int vendaId, String numero, String serie, LocalDateTime dataEmissao) {
        this.notaFiscalId = notaFiscalId;
        this.vendaId = vendaId;
        this.numero = numero;
        this.serie = serie;
        this.dataEmissao = dataEmissao;
    }

    public int getNotaFiscalId() { return notaFiscalId; }
    public void setNotaFiscalId(int notaFiscalId) { this.notaFiscalId = notaFiscalId; }

    public int getVendaId() { return vendaId; }
    public void setVendaId(int vendaId) { this.vendaId = vendaId; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getSerie() { return serie; }
    public void setSerie(String serie) { this.serie = serie; }

    public LocalDateTime getDataEmissao() { return dataEmissao; }
    public void setDataEmissao(LocalDateTime dataEmissao) { this.dataEmissao = dataEmissao; }
}
