package br.com.market.adapter.out.web.feing.adapter;

import br.com.market.adapter.out.web.feing.PurchasesClient;
import br.com.market.application.domain.model.Customer;
import br.com.market.application.domain.model.CustomerPurchase;
import br.com.market.application.domain.model.CustomerPurchasesData;
import br.com.market.application.domain.model.Product;
import br.com.market.application.domain.model.PurchasesItem;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PurchasesAdapterTest {

    @Mock
    private PurchasesClient purchasesClient;

    @InjectMocks
    private PurchasesAdapter purchasesAdapter;

    @Test
    void shouldReturnPurchasesWhenApiReturnsData() {

        var customer = Customer.builder().cpf("test").name("test").build();
        var product1 = Product.builder().id(1L).quantity(2).build();
        var product2 = Product.builder().id(2L).quantity(3).build();
        var datePurchaseLocalDate = LocalDate.parse("2025-03-31");

        var purchase1 = CustomerPurchase.builder().customer(customer)
                .purchasesItems(Collections.singletonList(PurchasesItem.builder()
                        .date(datePurchaseLocalDate)
                        .totalValue(BigDecimal.TEN)
                        .productList(Collections.singletonList(product1))
                        .build()))
                .build();
        var purchase2 = CustomerPurchase.builder().customer(customer)
                .purchasesItems(Collections.singletonList(PurchasesItem.builder()
                        .date(datePurchaseLocalDate)
                        .totalValue(BigDecimal.ONE)
                        .productList(Collections.singletonList(product2))
                        .build()))
                .build();

        when(purchasesClient.getPurchases()).thenReturn(new CustomerPurchasesData<>(List.of(purchase1,purchase2)));

        List<CustomerPurchase> result = purchasesAdapter.getPurchases();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("test", result.get(0).getCustomer().getName());
    }

    @Test
    void shouldThrowRuntimeExceptionWhenApiReturnsNotFound() {
        // Simula erro 404 na API
        when(purchasesClient.getPurchases()).thenThrow(FeignException.NotFound.class);

        // Execução e validação
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> purchasesAdapter.getPurchases());
        assertTrue(thrown.getMessage().contains("Erro ao consumir a API"));
    }

    @Test
    void shouldThrowRuntimeExceptionWhenApiReturnsFeignException() {
        // Simula erro 500 na API
        when(purchasesClient.getPurchases()).thenThrow(mock(FeignException.class));

        // Execução e validação
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> purchasesAdapter.getPurchases());
        assertTrue(thrown.getMessage().contains("Erro ao consumir a API"));
    }

    @Test
    void shouldThrowRuntimeExceptionWhenUnexpectedExceptionOccurs() {
        // Simula uma exceção inesperada
        when(purchasesClient.getPurchases()).thenThrow(new RuntimeException("Erro inesperado"));

        // Execução e validação
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> purchasesAdapter.getPurchases());
        assertTrue(thrown.getMessage().contains("Erro inesperado ao buscar compras"));
    }
}
