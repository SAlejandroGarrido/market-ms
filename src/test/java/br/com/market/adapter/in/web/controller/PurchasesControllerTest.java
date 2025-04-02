package br.com.market.adapter.in.web.controller;

import br.com.market.application.domain.dto.in.CustomerDTO;
import br.com.market.application.domain.dto.in.ProductDTO;
import br.com.market.application.domain.dto.in.PurchaseDTO;
import br.com.market.application.domain.dto.in.PurchasesItemDTO;
import br.com.market.application.port.in.CustomersAndPurchasesUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
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
        List<PurchaseDTO> expectedPurchases = List.of(new PurchaseDTO(), new PurchaseDTO());
        when(customersAndPurchasesUseCase.getPurchasesOrderedByValue()).thenReturn(expectedPurchases);

        mockMvc.perform(get("/purchases"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(expectedPurchases.size()));
    }

    @Test
    void getLargestPurchaseOfTheYear_ShouldReturnCompletePurchaseDTO() throws Exception {
        int year = 2024;
        PurchaseDTO purchaseDTO = createMockPurchaseDTO();
        when(customersAndPurchasesUseCase.getLargestPurchaseOfTheYear(year)).thenReturn(purchaseDTO);

        mockMvc.perform(get("/purchases/{year}", year))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customer.name").value("Carlos Souza"))
                .andExpect(jsonPath("$.customer.cpf").value("321.654.987-00"))
                .andExpect(jsonPath("$.purchasesItems.length()").value(2))
                .andExpect(jsonPath("$.purchasesItems[0].totalValue").value(270))
                .andExpect(jsonPath("$.purchasesItems[0].date").value("2024-03-15"))
                .andExpect(jsonPath("$.purchasesItems[0].productList[0].id").value(2))
                .andExpect(jsonPath("$.purchasesItems[0].productList[0].quantity").value(3))
                .andExpect(jsonPath("$.purchasesItems[1].totalValue").value(750))
                .andExpect(jsonPath("$.purchasesItems[1].date").value("2024-01-05"))
                .andExpect(jsonPath("$.purchasesItems[1].productList[0].id").value(5))
                .andExpect(jsonPath("$.purchasesItems[1].productList[0].quantity").value(5))
                .andExpect(jsonPath("$.totalValuePurchases").value(1020));
    }

    private PurchaseDTO createMockPurchaseDTO() {
        CustomerDTO customer = new CustomerDTO("Carlos Souza", "321.654.987-00");
        PurchasesItemDTO item1 = createMockPurchasesItem(270, LocalDate.of(2024, 3, 15), 2, 3);
        PurchasesItemDTO item2 = createMockPurchasesItem(750, LocalDate.of(2024, 1, 5), 5, 5);

        return new PurchaseDTO(customer, List.of(item1, item2), BigDecimal.valueOf(1020));
    }

    private PurchasesItemDTO createMockPurchasesItem(double totalValue, LocalDate date, long productId, int quantity) {
        ProductDTO product =  ProductDTO.builder().id(productId).quantity(quantity).build();
        return new PurchasesItemDTO(BigDecimal.valueOf(totalValue), date, List.of(product));
    }
}
