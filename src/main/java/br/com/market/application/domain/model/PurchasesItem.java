package br.com.market.application.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PurchasesItem implements Serializable {
    @JsonProperty("valor_total")
    private BigDecimal totalValue;
    @JsonProperty("data")
    private LocalDate date;
    @JsonProperty("produtos")
    public List<Product> productList;
}
