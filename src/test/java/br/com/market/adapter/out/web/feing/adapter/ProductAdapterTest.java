package br.com.market.adapter.out.web.feing.adapter;

import br.com.market.adapter.out.web.feing.ProductsClient;
import br.com.market.adapter.out.web.feing.exception.ProductAdapterException;
import br.com.market.application.domain.model.Product;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductAdapterTest {

    @Mock
    private ProductsClient productsClient;

    @InjectMocks
    private ProductAdapter productAdapter;

  @Test
  void getProducts_ShouldReturnListOfProducts() {
      List<Product> expectedProducts = List.of(
              Product.builder()
                      .id(1L)
                      .wineType("Red Wine")
                      .price(BigDecimal.valueOf(100))
                      .harvest("2020")
                      .purchaseYear(2021)
                      .build(),
              Product.builder()
                      .id(2L)
                      .wineType("White Wine")
                      .price(BigDecimal.valueOf(200))
                      .harvest("2019")
                      .purchaseYear(2020)
                      .build()
      );

      when(productsClient.getProducts()).thenReturn(expectedProducts);

      List<Product> actualProducts = productAdapter.getProducts();

      assertEquals(expectedProducts, actualProducts);
  }

    @Test
    void getProducts_ShouldThrowProductAdapterException_WhenFeignExceptionOccurs() {
        when(productsClient.getProducts()).thenThrow(FeignException.class);

        assertThrows(ProductAdapterException.class, () -> productAdapter.getProducts());
    }

    @Test
    void getProducts_ShouldThrowProductAdapterException_WhenUnexpectedExceptionOccurs() {
        when(productsClient.getProducts()).thenThrow(RuntimeException.class);

        assertThrows(ProductAdapterException.class, () -> productAdapter.getProducts());
    }
}