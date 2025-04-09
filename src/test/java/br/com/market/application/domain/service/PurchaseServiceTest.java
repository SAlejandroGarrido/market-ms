package br.com.market.application.domain.service;

import br.com.market.adapter.out.web.feing.adapter.ProductAdapter;
import br.com.market.adapter.out.web.feing.adapter.PurchasesAdapter;
import br.com.market.application.domain.model.CustomerPurchase;
import br.com.market.application.domain.model.Product;
import br.com.market.application.domain.model.ProductPurchase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PurchaseServiceTest {

    @Mock
    private PurchasesAdapter purchasesAdapter;

    @Mock
    private ProductAdapter productAdapter;

    @InjectMocks
    private PurchaseService purchaseService;

    @Test
    void shouldReturnPurchasesOrderedByValueAscending() {
        CustomerPurchase mock1 = CustomerPurchase.builder()
                .cpf("12345678900")
                .name("John Doe")
                .purchaseProducts(List.of(
                        ProductPurchase.builder()
                                .id(1L)
                                .quantity(2)
                                .build(),
                        ProductPurchase.builder()
                                .id(2L)
                                .quantity(1)
                                .build())).build();

        CustomerPurchase mock2 = CustomerPurchase.builder()
                .name("Jane Doe")
                .cpf("98765432100")
                .purchaseProducts(List.of(
                        ProductPurchase.builder()
                                .id(3L)
                                .quantity(1)
                                .build(),
                        ProductPurchase.builder()
                                .id(4L)
                                .quantity(2)
                                .build())).build();

        var customerPurchases = List.of(mock1, mock2);

        var products = List.of(
                Product.builder()
                        .id(1L)
                        .price(new BigDecimal("10.00"))
                        .build(),
                Product.builder()
                        .id(2L)
                        .price(new BigDecimal("20.00"))
                        .build(),
                Product.builder()
                        .id(3L)
                        .price(new BigDecimal("30.00"))
                        .build(),
                Product.builder()
                        .id(4L)
                        .price(new BigDecimal("40.00"))
                        .build());

        when(purchasesAdapter.getPurchases()).thenReturn(customerPurchases);
        when(productAdapter.getProducts()).thenReturn(products);

        var result = purchaseService.getPurchasesOrderedByValue();

        verify(purchasesAdapter, only()).getPurchases();
        assertEquals(2, result.size());
        assertTrue(result.get(0).getPurchasesTotalValue().compareTo(result.get(1).getPurchasesTotalValue()) <= 0,
                "As compras não estão em ordem crescente pelo valor total.");
    }


    @Test
    void shouldReturnEmptyPageWhenNoPurchases() {
        when(purchasesAdapter.getPurchases()).thenReturn(Collections.emptyList());

        var result = purchaseService.getPurchasesOrderedByValue();

        assertEquals(0, result.size());
        assertTrue(result.isEmpty());
    }

   @Test
    void shouldReturnLargestPurchaseOfTheYear() {
        int year = 2025;

        CustomerPurchase mock1 = CustomerPurchase.builder()
                .cpf("12345678900")
                .name("John Doe")
                .purchaseProducts(List.of(
                        ProductPurchase.builder()
                                .id(1L)
                                .quantity(2)
                                .build(),
                        ProductPurchase.builder()
                                .id(2L)
                                .quantity(1)
                                .build())).build();

        CustomerPurchase mock2 = CustomerPurchase.builder()
                .cpf("98765432100")
                .name("Jane Doe")
                .purchaseProducts(List.of(
                        ProductPurchase.builder()
                                .id(3L)
                                .quantity(1)
                                .build(),
                        ProductPurchase.builder()
                                .id(4L)
                                .quantity(2)
                                .build())).build();

        var customerPurchases = List.of(mock1, mock2);

        var products = List.of(
                Product.builder()
                        .id(1L)
                        .price(new BigDecimal("10.00"))
                        .purchaseYear(2025)
                        .wineType("red")
                        .harvest("2014")
                        .build(),
                Product.builder()
                        .id(2L)
                        .price(new BigDecimal("20.00"))
                        .purchaseYear(2025)
                        .wineType("white")
                        .harvest("2024")
                        .build(),
                Product.builder()
                        .id(3L)
                        .price(new BigDecimal("30.00"))
                        .purchaseYear(2025)
                        .wineType("rose")
                        .harvest("2018")
                        .build(),
                Product.builder()
                        .id(4L)
                        .purchaseYear(2025)
                        .wineType("red")
                        .price(new BigDecimal("40.00"))
                        .harvest("2023")
                        .build());

        when(purchasesAdapter.getPurchases()).thenReturn(customerPurchases);
        when(productAdapter.getProducts()).thenReturn(products);

        var result = purchaseService.getLargestPurchaseOfTheYear(year);

        verify(purchasesAdapter, only()).getPurchases();
        assertEquals(new BigDecimal("110.00"), result.getPurchasesTotalValue());
        assertEquals("98765432100", result.getCustomer().getCpf());
    }

    @Test
    void shouldThrowExceptionWhenNoPurchasesFoundForYear() {
        int year = 2025;
        when(purchasesAdapter.getPurchases()).thenReturn(Collections.emptyList());

        var exception = assertThrows(RuntimeException.class, () -> purchaseService.getLargestPurchaseOfTheYear(year));
        assertEquals("Nenhuma compra encontrada para o ano " + year, exception.getMessage());

        verify(purchasesAdapter, only()).getPurchases();
    }

    @Test
    void shouldReturnTop3Customers() {
        CustomerPurchase mock1 = CustomerPurchase.builder()
                .cpf("12345678900")
                .name("John Doe")
                .purchaseProducts(List.of(
                        ProductPurchase.builder()
                                .id(1L)
                                .quantity(2)
                                .build(),
                        ProductPurchase.builder()
                                .id(2L)
                                .quantity(1)
                                .build())).build();

        CustomerPurchase mock2 = CustomerPurchase.builder()
                .cpf("98765432100")
                .name("Jane Doe")
                .purchaseProducts(List.of(
                        ProductPurchase.builder()
                                .id(3L)
                                .quantity(1)
                                .build(),
                        ProductPurchase.builder()
                                .id(4L)
                                .quantity(2)
                                .build())).build();

        CustomerPurchase mock3 = CustomerPurchase.builder()
                .cpf("11111111111")
                .name("Alice")
                .purchaseProducts(List.of(
                        ProductPurchase.builder()
                                .id(5L)
                                .quantity(3)
                                .build(),
                        ProductPurchase.builder()
                                .id(6L)
                                .quantity(2)
                                .build())).build();

        var customerPurchases = List.of(mock1, mock2, mock3);

        var products = List.of(
                Product.builder()
                        .id(1L)
                        .price(new BigDecimal("10.00"))
                        .build(),
                Product.builder()
                        .id(2L)
                        .price(new BigDecimal("20.00"))
                        .build(),
                Product.builder()
                        .id(3L)
                        .price(new BigDecimal("30.00"))
                        .build(),
                Product.builder()
                        .id(4L)
                        .price(new BigDecimal("40.00"))
                        .build(),
                Product.builder()
                        .id(5L)
                        .price(new BigDecimal("50.00"))
                        .build(),
                Product.builder()
                        .id(6L)
                        .price(new BigDecimal("60.00"))
                        .build());

        when(purchasesAdapter.getPurchases()).thenReturn(customerPurchases);
        when(productAdapter.getProducts()).thenReturn(products);

        var result = purchaseService.getTopCustomer();

        assertEquals(3, result.size());
        assertEquals("11111111111", result.get(0).getCpf());
        assertEquals("98765432100", result.get(1).getCpf());
        assertEquals("12345678900", result.get(2).getCpf());
    }

    @Test
    void shouldReturnEmptyListWhenNoPurchases() {
        when(purchasesAdapter.getPurchases()).thenReturn(Collections.emptyList());

        var result = purchaseService.getTopCustomer();

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldHandleCustomersWithSameTotalPurchaseValue() {
        CustomerPurchase mock1 = CustomerPurchase.builder()
                .cpf("12345678900")
                .name("John Doe")
                .purchaseProducts(List.of(
                        ProductPurchase.builder()
                                .id(1L)
                                .quantity(2)
                                .build(),
                        ProductPurchase.builder()
                                .id(2L)
                                .quantity(1)
                                .build())).build();

        CustomerPurchase mock2 = CustomerPurchase.builder()
                .cpf("98765432100")
                .name("Jane Doe")
                .purchaseProducts(List.of(
                        ProductPurchase.builder()
                                .id(3L)
                                .quantity(1)
                                .build(),
                        ProductPurchase.builder()
                                .id(4L)
                                .quantity(2)
                                .build())).build();

        var customerPurchases = List.of(mock1, mock2);

        var products = List.of(
                Product.builder()
                        .id(1L)
                        .price(new BigDecimal("10.00"))
                        .build(),
                Product.builder()
                        .id(2L)
                        .price(new BigDecimal("20.00"))
                        .build(),
                Product.builder()
                        .id(3L)
                        .price(new BigDecimal("30.00"))
                        .build(),
                Product.builder()
                        .id(4L)
                        .price(new BigDecimal("40.00"))
                        .build());
        when(purchasesAdapter.getPurchases()).thenReturn(customerPurchases);
        when(productAdapter.getProducts()).thenReturn(products);

        var result = purchaseService.getTopCustomer();

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(customer -> customer.getCpf().equals("12345678900")));
        assertTrue(result.stream().anyMatch(customer -> customer.getCpf().equals("98765432100")));
    }

    @Test
    void shouldRecommendWineTypeBasedOnRecurringPurchases() {
        CustomerPurchase mock1 = CustomerPurchase.builder()
                .cpf("12345678900")
                .name("John Doe")
                .purchaseProducts(List.of(
                        ProductPurchase.builder()
                                .id(1L)
                                .quantity(2)
                                .build(),
                        ProductPurchase.builder()
                                .id(2L)
                                .quantity(1)
                                .build())).build();

        CustomerPurchase mock2 = CustomerPurchase.builder()
                .cpf("98765432100")
                .name("Jane Doe")
                .purchaseProducts(List.of(
                        ProductPurchase.builder()
                                .id(3L)
                                .quantity(1)
                                .build(),
                        ProductPurchase.builder()
                                .id(4L)
                                .quantity(2)
                                .build())).build();

        var customerPurchases = List.of(mock1, mock2);

        var products = List.of(
                Product.builder()
                        .id(1L)
                        .wineType("red")
                        .build(),
                Product.builder()
                        .id(2L)
                        .wineType("red")
                        .build(),
                Product.builder()
                        .id(3L)
                        .wineType("white")
                        .build(),
                Product.builder()
                        .id(4L)
                        .wineType("white")
                        .build());

        when(purchasesAdapter.getPurchases()).thenReturn(customerPurchases);
        when(productAdapter.getProducts()).thenReturn(products);

        var result = purchaseService.recommendationTypeBasedOnRecurringPurchases();

        assertEquals(2, result.size());
        assertEquals("red", result.get(0).getWineType());
        assertEquals("white", result.get(1).getWineType());
    }

    @Test
    void shouldReturnEmptyRecommendationWhenNoPurchases() {
        when(purchasesAdapter.getPurchases()).thenReturn(Collections.emptyList());
        when(productAdapter.getProducts()).thenReturn(Collections.emptyList());

        var result = purchaseService.recommendationTypeBasedOnRecurringPurchases();

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldHandleNoRecommendationForCustomer() {
        CustomerPurchase mock1 = CustomerPurchase.builder()
                .cpf("12345678900")
                .name("John Doe")
                .purchaseProducts(List.of(
                        ProductPurchase.builder()
                                .id(1L)
                                .quantity(2)
                                .build())).build();

        var customerPurchases = List.of(mock1);

        var products = List.of(
                Product.builder()
                        .id(2L)
                        .harvest("2023")
                        .wineType("rose")
                        .purchaseYear(2023)
                        .price(new BigDecimal("50.00"))
                        .wineType("white")
                        .build()) ;

        when(purchasesAdapter.getPurchases()).thenReturn(customerPurchases);
        when(productAdapter.getProducts()).thenReturn(products);

        var result = purchaseService.recommendationTypeBasedOnRecurringPurchases();

        assertEquals(1, result.size());
        assertEquals("Sem recomendação", result.get(0).getWineType());
    }

    @Test
    void shouldHandleNullValuesFromAdapters() {
        when(purchasesAdapter.getPurchases()).thenReturn(null);
        when(productAdapter.getProducts()).thenReturn(null);

        var resultPurchases = purchaseService.getPurchasesOrderedByValue();
        var resultCustomerLoyalDTOS= purchaseService.getTopCustomer();
        var resultRecommendations = purchaseService.recommendationTypeBasedOnRecurringPurchases();

        assertTrue(resultCustomerLoyalDTOS.isEmpty());
        assertTrue(resultPurchases.isEmpty());
        assertTrue(resultRecommendations.isEmpty());
    }

    @Test
    void shouldHandleSinglePurchase() {
        CustomerPurchase mock1 = CustomerPurchase.builder()
                .cpf("12345678900")
                .name("John Doe")
                .purchaseProducts(List.of(
                        ProductPurchase.builder()
                                .id(1L)
                                .quantity(1)
                                .build())).build();

        var customerPurchases = List.of(mock1);

        var products = List.of(
                Product.builder()
                        .id(1L)
                        .price(new BigDecimal("10.00"))
                        .build());

        when(purchasesAdapter.getPurchases()).thenReturn(customerPurchases);
        when(productAdapter.getProducts()).thenReturn(products);

        var result = purchaseService.getPurchasesOrderedByValue();

        assertEquals(1, result.size());
        assertEquals(new BigDecimal("10.00"), result.get(0).getPurchasesTotalValue());
    }

    @Test
    void shouldHandleEqualRecurringWineTypes() {
        CustomerPurchase mock1 = CustomerPurchase.builder()
                .cpf("12345678900")
                .name("John Doe")
                .purchaseProducts(List.of(
                        ProductPurchase.builder()
                                .id(1L)
                                .quantity(2)
                                .build(),
                        ProductPurchase.builder()
                                .id(2L)
                                .quantity(2)
                                .build())).build();

        var customerPurchases = List.of(mock1);

        var products = List.of(
                Product.builder()
                        .id(1L)
                        .wineType("red")
                        .build(),
                Product.builder()
                        .id(2L)
                        .wineType("white")
                        .build());

        when(purchasesAdapter.getPurchases()).thenReturn(customerPurchases);
        when(productAdapter.getProducts()).thenReturn(products);

        var result = purchaseService.recommendationTypeBasedOnRecurringPurchases();

        assertEquals(1, result.size());
        assertEquals("red", result.get(0).getWineType());
    }

    @Test
    void shouldFilterPurchasesByYear() {
        int year = 2023;

        CustomerPurchase mock1 = CustomerPurchase.builder()
                .cpf("12345678900")
                .name("John Doe")
                .purchaseProducts(List.of(
                        ProductPurchase.builder()
                                .id(1L)
                                .quantity(1)
                                .build())).build();

        var customerPurchases = List.of(mock1);

        var products = List.of(
                Product.builder()
                        .id(1L)
                        .price(new BigDecimal("10.00"))
                        .purchaseYear(2023)
                        .build(),
                Product.builder()
                        .id(2L)
                        .price(new BigDecimal("20.00"))
                        .purchaseYear(2022)
                        .build());

        when(purchasesAdapter.getPurchases()).thenReturn(customerPurchases);
        when(productAdapter.getProducts()).thenReturn(products);

        var result = purchaseService.getLargestPurchaseOfTheYear(year);

        assertEquals(new BigDecimal("10.00"), result.getPurchasesTotalValue());
    }
}