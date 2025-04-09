package br.com.market.adapter.in.web.controller.exception;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper=false)
public class PurchaseNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private String message;
    private String description;

    public PurchaseNotFoundException(String message){
        super(message);
        this.message = message;
    }

    public PurchaseNotFoundException(String message, String description){
        super(message);
        this.message = message;
        this.description = description;
    }

    public PurchaseNotFoundException(String message, Throwable exception) {
        super(message, exception);
    }
}
