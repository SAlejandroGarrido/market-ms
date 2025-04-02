package br.com.market.application.domain.dto.in;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseDTO {
    private CustomerDTO customer;
    private List<PurchasesItemDTO> purchasesItems;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal totalValuePurchases;

    @PostConstruct
    public void calculeteTotalValueAndSetValue() {
        this.totalValuePurchases = this.purchasesItems.stream()
                .map(PurchasesItemDTO::getTotalValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
