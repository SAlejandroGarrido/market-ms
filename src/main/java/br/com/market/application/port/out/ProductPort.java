package br.com.market.application.port.out;

import br.com.market.application.domain.model.Product;

import java.util.List;

public interface ProductPort {

   List<Product> getProducts();
}
