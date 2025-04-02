package br.com.market.adapter.out.web.feing.adapter;

import br.com.market.adapter.out.web.feing.ProductsClient;
import br.com.market.application.domain.model.Product;
import br.com.market.application.domain.model.ProductsData;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductAdapterTest {

    @Mock
    private ProductsClient productsClient;

    @InjectMocks
    private ProductAdapter productAdapter;

    @Test
    void shouldReturnProductsWhenApiReturnsData() {
        var product1 = Product.builder().id(1L).name("Produto A").category("Categoria A").price(BigDecimal.TEN).build();
        var product2 = Product.builder().id(2L).name("Produto B").category("Categoria B").price(BigDecimal.ONE).build();

        var productData = new ProductsData<>(List.of(product1, product2));
        when(productsClient.getProducts()).thenReturn(productData);

        List<Product> result = productAdapter.getProduct();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Produto A", result.get(0).getName());
    }

    @Test
    void shouldThrowRuntimeExceptionWhenApiReturnsNotFound() {
        when(productsClient.getProducts()).thenThrow(FeignException.NotFound.class);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> productAdapter.getProduct());
        assertTrue(thrown.getMessage().contains("Erro ao consumir a API"));
    }

    @Test
    void shouldThrowRuntimeExceptionWhenApiReturnsFeignException() {
        when(productsClient.getProducts()).thenThrow(mock(FeignException.class));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> productAdapter.getProduct());
        assertTrue(thrown.getMessage().contains("Erro ao consumir a API"));
    }

    @Test
    void shouldThrowRuntimeExceptionWhenUnexpectedExceptionOccurs() {
        when(productsClient.getProducts()).thenThrow(new RuntimeException("Erro inesperado"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> productAdapter.getProduct());
        assertTrue(thrown.getMessage().contains("Erro inesperado ao buscar produtos"));
    }
}
