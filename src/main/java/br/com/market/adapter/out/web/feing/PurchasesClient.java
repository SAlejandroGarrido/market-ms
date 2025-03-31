package br.com.market.adapter.out.web.feing;

import br.com.market.application.domain.model.CustomerPurchase;
import br.com.market.application.domain.model.CustomerPurchasesData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@FeignClient(name = "purchasesClient", url = "http://run.mocky.io/v3")
public interface PurchasesClient {

    @GetMapping(value = "/1ecc78a0-9495-4e07-b4f4-14a6ae576858", consumes = MediaType.APPLICATION_JSON_VALUE)
    CustomerPurchasesData<List<CustomerPurchase>> getPurchases();
}
