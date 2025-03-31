package br.com.market.application.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPurchasesData<T> implements Serializable {

    @JsonProperty("cliente_compras")
    private T customerPurchases;
}
