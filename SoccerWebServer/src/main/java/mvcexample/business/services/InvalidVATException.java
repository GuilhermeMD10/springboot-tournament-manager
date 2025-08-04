package mvcexample.business.services;

public class InvalidVATException extends ApplicationException {

    public InvalidVATException() {
        super("VAT inválido.");
    }

}
