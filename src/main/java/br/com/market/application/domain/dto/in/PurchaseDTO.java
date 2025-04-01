package br.com.market.application.domain.dto.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseDTO {
    private CustomerDTO customer;
    private List<PurchasesItemDTO> purchasesItems;
}
