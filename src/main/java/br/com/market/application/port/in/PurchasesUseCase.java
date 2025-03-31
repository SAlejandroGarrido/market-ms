package br.com.market.application.port.in;

import br.com.market.application.domain.dto.in.PurchaseDTO;
import org.springframework.data.domain.Page;

public interface PurchasesUseCase {

    Page<PurchaseDTO> getPurchasesOrderedByValue(int page, int size) throws Exception;
}
