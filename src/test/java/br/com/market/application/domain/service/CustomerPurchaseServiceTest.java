package br.com.market.application.domain.service;

import br.com.market.adapter.out.web.feing.adapter.PurchasesAdapter;
import br.com.market.application.domain.dto.in.CustomerDTO;
import br.com.market.application.domain.dto.in.ProductDTO;
import br.com.market.application.domain.dto.in.PurchaseDTO;
import br.com.market.application.domain.dto.in.PurchasesItemDTO;
import br.com.market.application.domain.model.Customer;
import br.com.market.application.domain.model.CustomerPurchase;
import br.com.market.application.domain.model.Product;
import br.com.market.application.domain.model.PurchasesItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    private ModelMapper modelMapper;

    @InjectMocks
    private PurchaseService purchaseService;


    @Test
    void shouldReturnPurchasesOrderedByValueAscending() {
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

        var customerPurchases = List.of(purchase1, purchase2);

        var productDTO1 = ProductDTO.builder().id(1L).quantity(2).build();
        var productDTO2 = ProductDTO.builder().id(2L).quantity(3).build();

        var purchasesItemDTO1 = PurchasesItemDTO.builder()
                .date(datePurchaseLocalDate)
                .totalValue(BigDecimal.TEN)
                .productList(List.of(productDTO1)).build();
        var purchasesItemDTO2 = PurchasesItemDTO.builder()
                .date(datePurchaseLocalDate)
                .productList(List.of(productDTO2))
                .totalValue(BigDecimal.TEN)
                .productList(List.of(productDTO2)).build();

        var customerDTO1 = CustomerDTO.builder().cpf("test").name("test").build();

        var purchaseDTO1 = PurchaseDTO.builder().purchasesItems(List.of(purchasesItemDTO1)).customer(customerDTO1).build();
        var purchaseDTO2 = PurchaseDTO.builder().purchasesItems(List.of(purchasesItemDTO2)).customer(customerDTO1).build();

        when(purchasesAdapter.getOrderedByValue()).thenReturn(customerPurchases);
        when(modelMapper.map(purchase1, PurchaseDTO.class)).thenReturn(purchaseDTO1);
        when(modelMapper.map(purchase2, PurchaseDTO.class)).thenReturn(purchaseDTO2);

        Page<PurchaseDTO> result = purchaseService.getPurchasesOrderedByValue(0, 3);

        verify(purchasesAdapter, only()).getOrderedByValue();
        verify(modelMapper, times(1)).map(purchase1, PurchaseDTO.class);
        verify(modelMapper, times(1)).map(purchase2, PurchaseDTO.class);
        assertEquals(2, result.getTotalElements());
        // Verificar que as compras estão em ordem crescente com base no valor
        assertTrue(result.getContent().get(0).getPurchasesItems().get(0).getTotalValue()
                        .compareTo(result.getContent().get(1).getPurchasesItems()
                                .get(0).getTotalValue()) <= 0,
                "As compras não estão em ordem crescente pelo valor total.");
    }

    @Test
    void shouldReturnEmptyPageWhenNoPurchases() {
        when(purchasesAdapter.getOrderedByValue()).thenReturn(Collections.emptyList());

        Page<PurchaseDTO> result = purchaseService.getPurchasesOrderedByValue(0, 3);

        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
    }

}
