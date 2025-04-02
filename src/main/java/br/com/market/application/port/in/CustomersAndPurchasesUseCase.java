package br.com.market.application.port.in;

import br.com.market.application.domain.dto.in.CustomerLoyalDTO;
import br.com.market.application.domain.dto.in.PurchaseDTO;

import java.util.List;

public interface CustomersAndPurchasesUseCase {

    List<PurchaseDTO> getPurchasesOrderedByValue();

    PurchaseDTO getLargestPurchaseOfTheYear(int year);

    List<CustomerLoyalDTO> getTopCustomer();

}
