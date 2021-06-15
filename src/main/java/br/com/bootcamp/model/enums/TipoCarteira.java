package br.com.bootcamp.model.enums;

public enum TipoCarteira {
    PAYPAL,
    SAMSUM_PAY;

    public static boolean tipoCarteiraExistente(String tipoCarteira) {
        for (TipoCarteira tipoExistente: TipoCarteira.values()) {
            if (tipoExistente.name().equals(tipoExistente))
                return true;
        }
        return false;
    }
}
