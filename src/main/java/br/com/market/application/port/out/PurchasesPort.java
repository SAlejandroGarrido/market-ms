package br.com.market.application.port.out;

import br.com.market.application.domain.model.CustomerPurchase;

import java.util.List;

public interface PurchasesPort {

   List<CustomerPurchase> getPurchases();
}
