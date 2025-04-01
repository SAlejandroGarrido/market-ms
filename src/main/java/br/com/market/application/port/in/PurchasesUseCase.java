package br.com.market.application.port.in;

import br.com.market.application.domain.dto.in.PurchaseDTO;

import java.util.List;

public interface PurchasesUseCase {

    List<PurchaseDTO> getPurchasesOrderedByValue();
}
