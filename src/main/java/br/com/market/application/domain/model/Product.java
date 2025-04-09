package br.com.market.application.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Product {
    @JsonProperty("codigo")
    private Long id;

    @JsonProperty("tipo_vinho")
    private String wineType;

    @JsonProperty("preco")
    private BigDecimal price;

    @JsonProperty("safra")
    private String harvest;

    @JsonProperty("ano_compra")
    private int purchaseYear;
}