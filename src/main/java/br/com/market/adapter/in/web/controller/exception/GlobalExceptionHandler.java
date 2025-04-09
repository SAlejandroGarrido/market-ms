package br.com.market.adapter.in.web.controller.exception;

import br.com.market.adapter.out.web.feing.exception.ProductAdapterException;
import br.com.market.adapter.out.web.feing.exception.PurchaseAdapterException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PurchaseNotFoundException.class)
    public ResponseEntity<ExceptionBody> handlePurchaseNotFoundException(PurchaseNotFoundException ex) {
        return buildResponseEntity(Optional.ofNullable(ex.getMessage())
                .orElse(ex.toString()), ex.getDescription(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductAdapterException.class)
    public ResponseEntity<ExceptionBody> handleProductAdapterException(ProductAdapterException ex) {
        return buildResponseEntity(Optional.ofNullable(ex.getMessage())
                .orElse(ex.toString()), ex.getDescription(), HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(PurchaseAdapterException.class)
    public ResponseEntity<ExceptionBody> handlePurchaseAdapterException(PurchaseAdapterException ex) {
        return buildResponseEntity(Optional.ofNullable(ex.getMessage())
                .orElse(ex.toString()), ex.getDescription(), HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionBody> handleGlobalException(Exception ex) {
        return buildResponseEntity(Optional.ofNullable(ex.getMessage())
                .orElse(ex.toString()), ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({Throwable.class})
    public ResponseEntity<ExceptionBody> handleException(Throwable eThrowable) {
        return buildResponseEntity(eThrowable.getMessage(), ExceptionResolver.getRootException(eThrowable), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ExceptionBody> buildResponseEntity(String message, String description, HttpStatus status) {
        ExceptionBody ex = ExceptionBody.builder()
                .message(Optional.ofNullable(message).orElse("Unexpected error"))
                .description(description)
                .build();
        return ResponseEntity.status(status).headers(new HttpHeaders()).body(ex);
    }


}