package br.com.bootcamp.dto.response;

public class CarteiraResponse {

    private String resultado;

    private String id;

    public CarteiraResponse(String resultado, String id) {
        this.resultado = resultado;
        this.id = id;
    }

    public String getResultado() {
        return resultado;
    }

    public String getId() {
        return id;
    }
}
