package br.com.market.adapter.in.web.controller;

import br.com.market.application.domain.dto.in.CustomerDTO;
import br.com.market.application.domain.dto.in.PurchaseDTO;
import br.com.market.application.domain.dto.in.PurchasesProductDTO;
import br.com.market.application.port.in.CustomersAndPurchasesUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PurchasesController.class)
class PurchasesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomersAndPurchasesUseCase customersAndPurchasesUseCase;

    @Test
    void getPurchases_ShouldReturnListOfPurchases() throws Exception {
        List<PurchaseDTO> expectedPurchases = List.of(
                createPurchaseDTO("Carlos Souza", "321.654.987-00", 2L, 3, 270),
                createPurchaseDTO("Ana Silva", "123.456.789-00", 5L, 5, 750)
        );

        when(customersAndPurchasesUseCase.getPurchasesOrderedByValue()).thenReturn(expectedPurchases);

        mockMvc.perform(get("/v1/api/market/purchases"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(expectedPurchases.size()));
    }

    @Test
    void getLargestPurchaseOfTheYear_ShouldReturnCompletePurchaseDTO() throws Exception {
        int year = 2024;

        PurchaseDTO purchaseDTO = createPurchaseDTO(
                List.of(
                        createPurchasesProductDTO(2L, 3, 270),
                        createPurchasesProductDTO(5L, 5, 750)
                ),
                BigDecimal.valueOf(1020)
        );

        when(customersAndPurchasesUseCase.getLargestPurchaseOfTheYear(year)).thenReturn(purchaseDTO);

        mockMvc.perform(get("/v1/api/market/purchases/{year}", year))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customer.name").value("Carlos Souza"))
                .andExpect(jsonPath("$.customer.cpf").value("321.654.987-00"))
                .andExpect(jsonPath("$.purchasesProducts.length()").value(2))
                .andExpect(jsonPath("$.purchasesProducts[0].totalValue").value(270))
                .andExpect(jsonPath("$.purchasesProducts[0].id").value(2))
                .andExpect(jsonPath("$.purchasesProducts[0].quantity").value(3))
                .andExpect(jsonPath("$.purchasesProducts[1].totalValue").value(750))
                .andExpect(jsonPath("$.purchasesProducts[1].id").value(5))
                .andExpect(jsonPath("$.purchasesProducts[1].quantity").value(5))
                .andExpect(jsonPath("$.purchasesTotalValue").value(1020));
    }

    private PurchaseDTO createPurchaseDTO(String customerName, String customerCpf, Long productId, int quantity, double totalValue) {
        CustomerDTO customer = new CustomerDTO(customerName, customerCpf);
        PurchasesProductDTO product = createPurchasesProductDTO(productId, quantity, totalValue);
        return PurchaseDTO.builder()
                .customer(customer)
                .purchasesProducts(List.of(product))
                .purchasesTotalValue(BigDecimal.valueOf(totalValue))
                .build();
    }

    private PurchaseDTO createPurchaseDTO(List<PurchasesProductDTO> products, BigDecimal totalValue) {
        CustomerDTO customer = new CustomerDTO("Carlos Souza", "321.654.987-00");
        return PurchaseDTO.builder()
                .customer(customer)
                .purchasesProducts(products)
                .purchasesTotalValue(totalValue)
                .build();
    }

    private PurchasesProductDTO createPurchasesProductDTO(Long productId, int quantity, double totalValue) {
        return PurchasesProductDTO.builder()
                .id(productId)
                .quantity(quantity)
                .totalValue(BigDecimal.valueOf(totalValue))
                .build();
    }
}