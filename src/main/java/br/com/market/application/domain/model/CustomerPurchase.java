package br.com.market.application.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPurchase implements Serializable {

    @JsonProperty("cliente")
    private Customer customer;
    @JsonProperty("compras")
    private List<PurchasesItem> purchasesItems;

}
