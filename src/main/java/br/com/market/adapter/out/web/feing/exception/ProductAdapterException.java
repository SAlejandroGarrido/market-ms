package br.com.market.adapter.out.web.feing.exception;


import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper=false)
public class ProductAdapterException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private String message;
    private String description;

    public ProductAdapterException(String message){
        super(message);
        this.message = message;
    }

    public ProductAdapterException(String message, String description){
        super(message);
        this.message = message;
        this.description = description;
    }
}
