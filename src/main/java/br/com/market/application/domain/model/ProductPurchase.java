package br.com.market.application.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPurchase implements Serializable {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("quantidade")
    private int quantity;
}
