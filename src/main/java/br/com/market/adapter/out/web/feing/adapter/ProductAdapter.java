package br.com.market.adapter.out.web.feing.adapter;

import br.com.market.adapter.out.web.feing.ProductsClient;
import br.com.market.application.domain.model.Product;
import br.com.market.application.port.out.ProductPort;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Component
public class ProductAdapter implements ProductPort {

    private final ProductsClient productsClient;

    @Override
    public List<Product> getProduct() {
        try {
            return productsClient.getProducts().getProducts();
        } catch (FeignException e) {
            throw new RuntimeException("Erro ao consumir a API: " + e.status(), e);
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao buscar produtos", e);
        }

    }

}
