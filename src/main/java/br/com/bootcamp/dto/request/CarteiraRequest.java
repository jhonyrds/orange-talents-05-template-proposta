package br.com.bootcamp.dto.request;

import br.com.bootcamp.model.enums.TipoCarteira;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class CarteiraRequest {

    @Email
    @NotBlank
    private String email;

    private String carteira;

    public CarteiraRequest(String email, String carteira) {
        this.email = email;
        this.carteira = carteira;
    }

    public String getEmail() {
        return email;
    }

    public String getCarteira() {
        return carteira;
    }

    public void setCarteira(String carteira) {
        this.carteira = carteira;
    }

    public TipoCarteira getTipoCarteira() {
        return TipoCarteira.valueOf(carteira);
    }
}
