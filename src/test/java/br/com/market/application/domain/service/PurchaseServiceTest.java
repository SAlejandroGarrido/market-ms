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
import static org.mockito.Mockito.doNothing;
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
        var customer = Customer.builder().cpf("test").name("test").build();
        var product1 = ProductPurchase.builder().id(1L).quantity(2).build();
        var product2 = ProductPurchase.builder().id(2L).quantity(3).build();
        var datePurchaseLocalDate = LocalDate.parse("2025-03-31");

        var purchase1 = CustomerPurchase.builder()
                .customer(customer)
                .purchasesItems(List.of(
                        PurchasesItem.builder()
                                .date(datePurchaseLocalDate)
                                .totalValue(BigDecimal.TEN)
                                .productPurchaseList(List.of(product1))
                                .build()
                ))
                .build();

        var purchase2 = CustomerPurchase.builder()
                .customer(customer)
                .purchasesItems(List.of(
                        PurchasesItem.builder()
                                .date(datePurchaseLocalDate)
                                .totalValue(BigDecimal.ONE)
                                .productPurchaseList(List.of(product2))
                                .build()
                ))
                .build();

        var customerPurchases = List.of(purchase1, purchase2);

        var productDTO1 = ProductDTO.builder().id(1L).quantity(2).price(BigDecimal.TEN).category("teste").name("teste").build();
        var productDTO2 = ProductDTO.builder().id(2L).quantity(3).price(BigDecimal.ONE).category("teste").name("teste").build();

        var purchasesItemDTO1 = PurchasesItemDTO.builder()
                .date(datePurchaseLocalDate)
                .totalValue(BigDecimal.TEN)
                .productList(List.of(productDTO1))
                .build();

        var purchasesItemDTO2 = PurchasesItemDTO.builder()
                .date(datePurchaseLocalDate)
                .totalValue(BigDecimal.ONE)
                .productList(List.of(productDTO2))
                .build();

        var customerDTO = CustomerDTO.builder().cpf("test").name("test").build();
        var purchaseDTO1 = PurchaseDTO.builder().purchasesItems(List.of(purchasesItemDTO1)).customer(customerDTO).build();
        var purchaseDTO2 = PurchaseDTO.builder().purchasesItems(List.of(purchasesItemDTO2)).customer(customerDTO).build();

        var productDetails1 = Product.builder().id(1L).name("teste").category("teste").price(BigDecimal.TEN).build();
        var productDetails2 = Product.builder().id(2L).name("teste").category("teste").price(BigDecimal.ONE).build();

        var productList = List.of(productDetails1, productDetails2);

        when(purchasesAdapter.getPurchases()).thenReturn(customerPurchases);
        when(productAdapter.getProduct()).thenReturn(productList);
        when(modelMapper.map(purchase1, PurchaseDTO.class)).thenReturn(purchaseDTO1);
        when(modelMapper.map(purchase2, PurchaseDTO.class)).thenReturn(purchaseDTO2);
        doNothing().when(modelMapper).map(productDetails2, productDTO2);
        doNothing().when(modelMapper).map(productDetails1, productDTO1);

        var result = purchaseService.getPurchasesOrderedByValue();

        verify(purchasesAdapter, only()).getPurchases();
        verify(modelMapper, times(1)).map(purchase1, PurchaseDTO.class);
        verify(modelMapper, times(1)).map(purchase2, PurchaseDTO.class);
        verify(modelMapper, times(1)).map(productDetails1, productDTO1);
        verify(modelMapper, times(1)).map(productDetails2, productDTO2);

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
        LocalDate purchaseDate = LocalDate.of(year, 3, 31);

        var customer = Customer.builder().cpf("12345678900").name("Cliente Teste").build();
        var product1 = ProductPurchase.builder().id(1L).quantity(2).build();
        var product2 = ProductPurchase.builder().id(2L).quantity(3).build();

        var purchase1 = CustomerPurchase.builder()
                .customer(customer)
                .purchasesItems(List.of(PurchasesItem.builder()
                        .date(purchaseDate)
                        .totalValue(new BigDecimal("100.00"))
                        .productPurchaseList(List.of(product1))
                        .build()))
                .build();

        var purchase2 = CustomerPurchase.builder()
                .customer(customer)
                .purchasesItems(List.of(PurchasesItem.builder()
                        .date(purchaseDate)
                        .totalValue(new BigDecimal("200.00"))
                        .productPurchaseList(List.of(product2))
                        .build()))
                .build();

        List<CustomerPurchase> purchases = List.of(purchase1, purchase2);

        var productDTO1 = ProductDTO.builder().id(1L).quantity(2).price(BigDecimal.TEN).category("teste").name("teste").build();

        var purchaseDTO = PurchaseDTO.builder()
                .customer(CustomerDTO.builder().cpf("12345678900").name("Cliente Teste").build())
                .purchasesItems(List.of(PurchasesItemDTO.builder()
                        .date(purchaseDate)
                        .totalValue(new BigDecimal("200.00"))
                        .productList(List.of(productDTO1))
                        .build()))
                .build();


        var productDetails1 = Product.builder().id(1L).name("teste").category("teste").price(BigDecimal.TEN).build();
        var productDetails2 = Product.builder().id(2L).name("teste").category("teste").price(BigDecimal.ONE).build();

        var productList = List.of(productDetails1, productDetails2);

        when(productAdapter.getProduct()).thenReturn(productList);
        when(purchasesAdapter.getPurchases()).thenReturn(purchases);
        when(modelMapper.map(purchase2, PurchaseDTO.class)).thenReturn(purchaseDTO);
        doNothing().when(modelMapper).map(productDetails1, productDTO1);

        var result = purchaseService.getLargestPurchaseOfTheYear(year);

        verify(purchasesAdapter, only()).getPurchases();
        verify(modelMapper, times(1)).map(purchase2, PurchaseDTO.class);
        verify(modelMapper, times(1)).map(productDetails1, productDTO1);
        verify(productAdapter, times(1)).getProduct();

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


}