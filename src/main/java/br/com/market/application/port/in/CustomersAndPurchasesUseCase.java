package br.com.market.application.port.in;

import br.com.market.application.domain.dto.in.CustomerLoyalDTO;
import br.com.market.application.domain.dto.in.PurchaseDTO;
import br.com.market.application.domain.dto.in.RecommendationProduct;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CustomersAndPurchasesUseCase {

    List<PurchaseDTO> getPurchasesOrderedByValue();

    PurchaseDTO getLargestPurchaseOfTheYear(int year);

    List<CustomerLoyalDTO> getTopCustomer();

    List<RecommendationProduct> recommendationTypeBasedOnRecurringPurchases();

}
