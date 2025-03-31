package br.com.market.application.domain.dto.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PurchasesItemDTO {
    private BigDecimal totalValue;
    private LocalDate date;
    public List<ProductDTO> productList;
}
