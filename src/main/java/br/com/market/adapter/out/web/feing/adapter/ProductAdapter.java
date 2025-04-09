package br.com.market.adapter.out.web.feing.adapter;

import br.com.market.adapter.out.web.feing.ProductsClient;
import br.com.market.adapter.out.web.feing.exception.ProductAdapterException;
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

    public List<Product> getProducts() {
        log.info("Iniciando a busca de produtos via ProductsClient.");
        try {
            List<Product> products = productsClient.getProducts();
            log.info("Busca de produtos concluída com sucesso. Total de produtos encontrados: {}", products.size());
            return products;
        } catch (FeignException e) {
            log.error("Erro ao consumir a API de produtos: {}", e.getMessage(), e);
            throw new ProductAdapterException("Erro ao consumir a API:", e.getMessage());
        } catch (Exception e) {
            log.error("Erro inesperado ao buscar produtos: {}", e.getMessage(), e);
            throw new ProductAdapterException("Erro inesperado ao buscar produtos", e.getMessage());
        }

    }

}
