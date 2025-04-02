package br.com.market.adapter.out.web.feing;

import br.com.market.application.domain.model.Product;
import br.com.market.application.domain.model.ProductsData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@FeignClient(name = "productsClient", url = "${clients.paths.products}")
public interface ProductsClient {

    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    ProductsData<List<Product>> getProducts();
}
