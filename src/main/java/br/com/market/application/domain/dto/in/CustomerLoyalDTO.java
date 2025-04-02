package br.com.market.application.domain.dto.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerLoyalDTO {
    private String name;
    private String cpf;
    private BigDecimal totalValuePurchase;
}
