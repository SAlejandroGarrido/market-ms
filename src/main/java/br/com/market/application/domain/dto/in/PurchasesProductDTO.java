package br.com.market.application.domain.dto.in;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Data Transfer Object for representing a purchased product.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Schema(description = "Informações do produto comprado")
public class PurchasesProductDTO {

    @Schema(description = "ID do produto", example = "2")
    private Long id;

    @Schema(description = "Quantidade comprada", example = "3")
    private int quantity;

    @Schema(description = "Tipo de vinho comprado", example = "Tinto")
    private String wineType;

    @Schema(description = "Preço unitário do produto", example = "90.00")
    private BigDecimal price;

    @Schema(description = "Safra do vinho", example = "2020")
    private String harvest;

    @Schema(description = "Ano da compra", example = "2024")
    private int purchaseYear;

    @Schema(description = "Valor total do produto", example = "270.00")
    private BigDecimal totalValue;

    /**
     * Calculates the total value of the product based on the price and quantity.
     *
     * @return Total value of the product.
     */
    private BigDecimal calculateTotalValueProduct() {
        if (this.price == null || this.quantity <= 0) {
            return BigDecimal.ZERO;
        }
        return this.price.multiply(BigDecimal.valueOf(this.quantity));
    }

    /**
     * Updates the total value of the product.
     */
    public void updateTotalValue() {
        this.totalValue = calculateTotalValueProduct();
    }
}