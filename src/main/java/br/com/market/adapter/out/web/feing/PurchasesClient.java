package br.com.market.adapter.out.web.feing;

import br.com.market.application.domain.model.CustomerPurchase;
import br.com.market.application.domain.model.CustomerPurchasesData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@FeignClient(name = "purchasesClient", url = "${clients.paths.purchases}")
public interface PurchasesClient {

    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    CustomerPurchasesData<List<CustomerPurchase>> getPurchases();
}
