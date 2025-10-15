package tecdes.pagamento.view;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import tecdes.pagamento.controller.ControllerPagamento;
import tecdes.pagamento.model.BoletoPagamento;
import tecdes.pagamento.model.CartaoPagamento;
import tecdes.pagamento.model.Pagamento;
import tecdes.pagamento.model.PixPagamento;

public class FormPagamento extends JFrame{
    private JTextField txtValor;
    private JComboBox<String> cmbMetodo;
    private JTextArea txtResultado;
    private ControllerPagamento controller;
    JLabel lblTipoCartao;
    JComboBox<String> cmbTipoCartao;
    JLabel lblParcelas;
    JTextField txtParcelas;

    public FormPagamento(){
        setSize(420, 520);
        setTitle("Gestor de Pagamentos - v1.0");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);

        controller = new ControllerPagamento();
        initComponents();
    }

    private void initComponents(){

         //-----Entradas------------------------------------
        JLabel lblValor = new JLabel("Valor (R$):");
        lblValor.setBounds(40, 30, 100, 25);
        add(lblValor);

        txtValor = new JTextField();
        txtValor.setBounds(150, 30, 200, 25);
        add(txtValor);

        JLabel lblMetodo = new JLabel("Método de Pagamento:");
        lblMetodo.setBounds(40, 70, 150, 25);
        add(lblMetodo);

        cmbMetodo = new JComboBox<>();
        cmbMetodo.addItem("Boleto");
        cmbMetodo.addItem("Cartão");
        cmbMetodo.addItem("Pix");
        cmbMetodo.setBounds(180, 70, 170, 25);
        add(cmbMetodo);

        lblTipoCartao = new JLabel("Tipo de cartão: ");
        lblTipoCartao.setBounds(40, 100, 120, 25);
        lblTipoCartao.setVisible(false);
        add(lblTipoCartao);

        cmbTipoCartao = new JComboBox<>(new String[]{"Débito", "Crédito"});
        cmbTipoCartao.setBounds(160, 110, 190, 25);
        cmbTipoCartao.setVisible(false);
        add(cmbTipoCartao);

        lblParcelas = new JLabel("Parcelas: ");
        lblParcelas.setBounds(40, 150, 120, 25);
        lblParcelas.setVisible(false);
        add(lblParcelas);

        txtParcelas =  new JTextField();
        txtParcelas.setBounds(160, 150, 50, 25);
        txtParcelas.setVisible(false);
        add(txtParcelas);

        //-----Botões------------------------------------------------
        JButton btnProcessar = new JButton("Processar Pagamento");
        btnProcessar.setBounds(120, 200, 180, 35);
        add(btnProcessar);

        JButton btnSalvarHistorico = new JButton ("Salvar Histórico");
        btnSalvarHistorico.setBounds (120, 240, 180, 35);
        add(btnSalvarHistorico);

        //-----Saída de dados------------------------------------------------
        txtResultado = new JTextArea();
        txtResultado.setEditable(false);
        txtResultado.setLineWrap(true);
        txtResultado.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(txtResultado);
        scroll.setBounds(40, 290, 320, 100);
        add(scroll);

        //-----Eventos (listener)------------------------------------------------
        btnProcessar.addActionListener(e -> Processar());
        cmbMetodo.addActionListener(e -> atualizarCampos());
        cmbTipoCartao.addActionListener(e -> atualizarParcelas());
        btnSalvarHistorico.addActionListener (e -> salvarHistoricoEmArquivo());
    }

    private void atualizarCampos() {
        String metodo = (String) cmbMetodo.getSelectedItem();
        boolean isCartao = metodo.equals("Cartão");

        lblTipoCartao.setVisible(isCartao);
        cmbTipoCartao.setVisible(isCartao);

        lblParcelas.setVisible(false);
        txtParcelas.setVisible(false);
        txtParcelas.setText("");

    }

    private void atualizarParcelas() {
        String tipo = (String) cmbTipoCartao.getSelectedItem();
        boolean isCredito = tipo.equals("Crédito");

        lblParcelas.setVisible(isCredito);
        txtParcelas.setVisible(isCredito);

    }

    private void Processar(){
        try {
            double valor = Double.parseDouble(txtValor.getText());              // Converter o valor para double
            String metodo = (String) cmbMetodo.getSelectedItem();               // Obter o método selecionado
            System.out.println(metodo);
            Pagamento pagamento = obterMetodo(metodo);                          // Obter a instância correta de Pagamento
            String resultado = controller.realizarPagamento(pagamento, valor);  // Processar o pagamento
            txtResultado.append(resultado + "\n");                              // Exibir o resultado

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Valor Inválido..."); // Tratar erro de conversão
        }
    }
    
    private Pagamento obterMetodo(String metodo){

        if(metodo.equals("Boleto")){
            return new BoletoPagamento();
        }

        if(metodo.equals("Cartão")){
            return new CartaoPagamento();
        }
            return new PixPagamento();
    }

    private void salvarHistoricoEmArquivo() {
        List<String> pagamentos = controller.listarPagamentos();

        if(pagamentos.isEmpty()){
            JOptionPane.showMessageDialog(null, "Não há pagamentos no histórico");
            return;

        }

        try(FileWriter writer = new FileWriter("histórico_pagamento.txt")) {
            for (String registro : pagamentos) {
                writer.write(registro);

            }
            JOptionPane.showMessageDialog(null, "Gravação de um arquivo com sucesso.");
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro na tentativa de salvar o arquivo: " + e.getMessage());
            
        }

    }


}