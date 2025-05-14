package org.example.controle_vendas.model;

import java.time.LocalDateTime;

public class Pagamento {
    private int pagamentoId;
    private int vendaId;
    private int formaPagamentoId;  // agora só o id
    private LocalDateTime dataHoraPagamento;
    private double valorPago;
    private String status;

    public Pagamento() {}

    public Pagamento(int pagamentoId, int vendaId, int formaPagamentoId,
                     LocalDateTime dataHoraPagamento, double valorPago, String status) {
        this.pagamentoId = pagamentoId;
        this.vendaId = vendaId;
        this.formaPagamentoId = formaPagamentoId;
        this.dataHoraPagamento = dataHoraPagamento;
        this.valorPago = valorPago;
        this.status = status;
    }

    public int getPagamentoId() {
        return pagamentoId;
    }

    public void setPagamentoId(int pagamentoId) {
        this.pagamentoId = pagamentoId;
    }

    public int getVendaId() {
        return vendaId;
    }

    public void setVendaId(int vendaId) {
        this.vendaId = vendaId;
    }

    public int getFormaPagamentoId() {
        return formaPagamentoId;
    }

    public void setFormaPagamentoId(int formaPagamentoId) {
        this.formaPagamentoId = formaPagamentoId;
    }

    public LocalDateTime getDataHoraPagamento() {
        return dataHoraPagamento;
    }

    public void setDataHoraPagamento(LocalDateTime dataHoraPagamento) {
        this.dataHoraPagamento = dataHoraPagamento;
    }

    public double getValorPago() {
        return valorPago;
    }

    public void setValorPago(double valorPago) {
        this.valorPago = valorPago;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}