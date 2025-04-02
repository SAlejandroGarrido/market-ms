package br.com.market.adapter.out.web.feing;

import br.com.market.application.domain.model.Product;
import br.com.market.application.domain.model.ProductsData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@FeignClient(name = "productsClient", url = "http://run.mocky.io/v3")
public interface ProductsClient {

    @GetMapping(value = "/f4c5e48f-771a-4cd6-bc6a-b1641ebe9c91", consumes = MediaType.APPLICATION_JSON_VALUE)
    ProductsData<List<Product>> getProducts();
}
