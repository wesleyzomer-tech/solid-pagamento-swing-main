package tecdes.pagamento.controller;

import java.util.List;

import tecdes.pagamento.model.Pagamento;

public class ControllerPagamento<RepositoryPagamento> {

    private RepositoryPagamento repository;

    public ControllerPagamento() {
        repository = new RepositoryPagamento();

    }

    public String realizarPagamento(Pagamento metodo, double valor){
        return metodo.processar(valor);
    }

    public List<String> ListarPagamentos() {
        List<String> lista = repository.listar();
        System.out.println("Lista de Pagamentos" + lista);
        return lista;
        
    }

    public List<String> listarPagamentos() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<String> listarPagamentos(){
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
