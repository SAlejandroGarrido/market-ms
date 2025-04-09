package br.com.market.application.domain.dto.in;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * DTO to represent a purchase.
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseDTO {
    @Schema(description = "Informações do cliente que realizou a compra")
    private CustomerDTO customer;

    @Schema(description = "Lista de produtos comprados")
    private List<PurchasesProductDTO> purchasesProducts;

    @Schema(description = "Valor total da compra", example = "1020.00")
    private BigDecimal purchasesTotalValue;

    /**
     * Returns a map of products in the purchase.
     *
     * @param purchasesProductDTO List of products in the purchase.
     * @return Map of products in the purchase.
     */
    public Map<Long, PurchasesProductDTO> getPurchaseProductDTOMap(List<PurchasesProductDTO> purchasesProductDTO) {
        return purchasesProductDTO.stream()
                .collect(Collectors.toMap(PurchasesProductDTO::getId, Function.identity()));
    }


    /**
     * Sets the total value of the purchase.
     */
    public void setTotalValuePurchase() {
        this.purchasesTotalValue = calculateTotalValuePurchase();
    }

    /**
     * Calculates the total value of the purchase.
     *
     * @return Total value of the purchase.
     */
    private BigDecimal calculateTotalValuePurchase() {
        if (this.purchasesProducts == null || this.purchasesProducts.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return this.purchasesProducts.stream()
                .map(PurchasesProductDTO::getTotalValue)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}