package br.com.market.adapter.out.web.feing.exception;


import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper=false)
public class PurchaseAdapterException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private String message;
    private String description;

    public PurchaseAdapterException(String message){
        super(message);
        this.message = message;
    }

    public PurchaseAdapterException(String message, String description){
        super(message);
        this.message = message;
        this.description = description;
    }

    public PurchaseAdapterException(String message, Throwable exception) {
        super(message, exception);
    }

}
