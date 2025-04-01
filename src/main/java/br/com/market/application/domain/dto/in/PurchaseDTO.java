package br.com.market.application.domain.dto.in;

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
    private BigDecimal totalValuePurchases;

    public void setTotalValuePurchases() {
        var totalValuePurchases = this.purchasesItems.stream()
                .map(PurchasesItemDTO::getTotalValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.setTotalValuePurchases(totalValuePurchases);
    }
}
