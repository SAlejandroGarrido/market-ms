package br.com.market.application.domain.service;

import br.com.market.adapter.out.web.feing.adapter.ProductAdapter;
import br.com.market.adapter.out.web.feing.adapter.PurchasesAdapter;
import br.com.market.application.domain.dto.in.CustomerDTO;
import br.com.market.application.domain.dto.in.ProductDTO;
import br.com.market.application.domain.dto.in.PurchaseDTO;
import br.com.market.application.domain.dto.in.PurchasesItemDTO;
import br.com.market.application.domain.model.Customer;
import br.com.market.application.domain.model.CustomerPurchase;
import br.com.market.application.domain.model.Product;
import br.com.market.application.domain.model.ProductPurchase;
import br.com.market.application.domain.model.PurchasesItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PurchaseServiceTest {

    @Mock
    private PurchasesAdapter purchasesAdapter;

    @Mock
    private ProductAdapter productAdapter;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PurchaseService purchaseService;


    @Test
    void shouldReturnPurchasesOrderedByValueAscending() {
        var customerPurchases = createMockCustomerPurchases();
        var productList = createMockProductList();
        var mappedPurchases = createMockPurchaseDTOList();

        when(purchasesAdapter.getPurchases()).thenReturn(customerPurchases);
        when(productAdapter.getProduct()).thenReturn(productList);
        when(modelMapper.map(customerPurchases.get(0), PurchaseDTO.class)).thenReturn(mappedPurchases.get(0));
        when(modelMapper.map(customerPurchases.get(1), PurchaseDTO.class)).thenReturn(mappedPurchases.get(1));

        var result = purchaseService.getPurchasesOrderedByValue();

        verify(purchasesAdapter, only()).getPurchases();
        verify(modelMapper, times(1)).map(customerPurchases.get(0), PurchaseDTO.class);
        verify(modelMapper, times(1)).map(customerPurchases.get(1), PurchaseDTO.class);

        assertEquals(2, result.size());
        assertTrue(result.get(0).getPurchasesItems().get(0).getTotalValue()
                        .compareTo(result.get(1).getPurchasesItems().get(0).getTotalValue()) <= 0,
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
        var customerPurchases = createMockCustomerPurchases();
        var productList = createMockProductList();
        var purchaseDTO = createMockPurchaseDTOList().get(1);

        when(purchasesAdapter.getPurchases()).thenReturn(customerPurchases);
        when(productAdapter.getProduct()).thenReturn(productList);
        when(modelMapper.map(customerPurchases.get(1), PurchaseDTO.class)).thenReturn(purchaseDTO);

        var result = purchaseService.getLargestPurchaseOfTheYear(year);

        verify(purchasesAdapter, only()).getPurchases();
        verify(modelMapper, times(1)).map(customerPurchases.get(1), PurchaseDTO.class);

        assertEquals(new BigDecimal("200.00"), result.getPurchasesItems().get(0).getTotalValue());
        assertEquals("12345678900", result.getCustomer().getCpf());
    }

    @Test
    void shouldThrowExceptionWhenNoPurchasesFoundForYear() {
        int year = 2025;
        when(purchasesAdapter.getPurchases()).thenReturn(Collections.emptyList());

        var exception = assertThrows(RuntimeException.class, () -> purchaseService.getLargestPurchaseOfTheYear(year));
        assertEquals("Nenhuma compra encontrada para o ano " + year, exception.getMessage());

        verify(purchasesAdapter, only()).getPurchases();
    }

    // Métodos privados reutilizáveis
    private List<CustomerPurchase> createMockCustomerPurchases() {
        var customer = Customer.builder().cpf("12345678900").name("Cliente Teste").build();
        var product1 = ProductPurchase.builder().id(1L).quantity(2).build();
        var product2 = ProductPurchase.builder().id(2L).quantity(3).build();

        var purchase1 = CustomerPurchase.builder()
                .customer(customer)
                .purchasesItems(List.of(PurchasesItem.builder()
                        .date(LocalDate.of(2025, 3, 31))
                        .totalValue(BigDecimal.TEN)
                        .productPurchaseList(List.of(product1))
                        .build()))
                .build();

        var purchase2 = CustomerPurchase.builder()
                .customer(customer)
                .purchasesItems(List.of(PurchasesItem.builder()
                        .date(LocalDate.of(2025, 3, 31))
                        .totalValue(new BigDecimal("200.00"))
                        .productPurchaseList(List.of(product2))
                        .build()))
                .build();

        return List.of(purchase1, purchase2);
    }

    private List<Product> createMockProductList() {
        var product1 = Product.builder().id(1L).name("teste").category("teste").price(BigDecimal.TEN).build();
        var product2 = Product.builder().id(2L).name("teste").category("teste").price(BigDecimal.ONE).build();
        return List.of(product1, product2);
    }

    private List<PurchaseDTO> createMockPurchaseDTOList() {
        var customerDTO = CustomerDTO.builder().cpf("12345678900").name("Cliente Teste").build();
        var productDTO = ProductDTO.builder().id(1L).quantity(2).price(BigDecimal.TEN).category("teste").name("teste").build();

        var purchasesItemDTO1 = PurchasesItemDTO.builder()
                .date(LocalDate.of(2025, 3, 31))
                .totalValue(BigDecimal.TEN)
                .productList(List.of(productDTO))
                .build();

        var purchasesItemDTO2 = PurchasesItemDTO.builder()
                .date(LocalDate.of(2025, 3, 31))
                .totalValue(new BigDecimal("200.00"))
                .productList(List.of(productDTO))
                .build();

        var purchaseDTO1 = PurchaseDTO.builder().customer(customerDTO).purchasesItems(List.of(purchasesItemDTO1)).build();
        var purchaseDTO2 = PurchaseDTO.builder().customer(customerDTO).purchasesItems(List.of(purchasesItemDTO2)).build();

        return List.of(purchaseDTO1, purchaseDTO2);
    }

}