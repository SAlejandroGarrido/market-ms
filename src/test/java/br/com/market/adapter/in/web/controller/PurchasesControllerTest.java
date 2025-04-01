package br.com.market.adapter.in.web.controller;

import br.com.market.application.domain.dto.in.CustomerDTO;
import br.com.market.application.domain.dto.in.ProductDTO;
import br.com.market.application.domain.dto.in.PurchaseDTO;
import br.com.market.application.domain.dto.in.PurchasesItemDTO;
import br.com.market.application.port.in.PurchasesUseCase;
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
    private PurchasesUseCase purchasesUseCase;

    @Test
    void getPurchases_ShouldReturnListOfPurchases() throws Exception {
        List<PurchaseDTO> expectedPurchases = List.of(new PurchaseDTO(), new PurchaseDTO());
        when(purchasesUseCase.getPurchasesOrderedByValue()).thenReturn(expectedPurchases);

        mockMvc.perform(get("/purchases"))
                .andExpect(status().isOk()) // Verifica se o status HTTP é 200
                .andExpect(jsonPath("$.length()").value(expectedPurchases.size())); // Verifica o tamanho da lista retornada
    }

    @Test
    void getLargestPurchaseOfTheYear_ShouldReturnCompletePurchaseDTO() throws Exception {
        // Arrange
        int year = 2024;

        // Criando um cliente fictício
        CustomerDTO customer = new CustomerDTO();
        customer.setName("Carlos Souza");
        customer.setCpf("321.654.987-00");

        // Criando os produtos e os itens de compra
        ProductDTO product1 = new ProductDTO();
        product1.setId(2L);
        product1.setQuantity(3);

        ProductDTO product2 = new ProductDTO();
        product2.setId(5L);
        product2.setQuantity(5);

        PurchasesItemDTO item1 = new PurchasesItemDTO();
        item1.setTotalValue(BigDecimal.valueOf(270));
        item1.setDate(LocalDate.of(2024, 3, 15));
        item1.setProductList(List.of(product1));

        PurchasesItemDTO item2 = new PurchasesItemDTO();
        item2.setTotalValue(BigDecimal.valueOf(750));
        item2.setDate(LocalDate.of(2024, 1, 5));
        item2.setProductList(List.of(product2));

        // Criando o DTO de resposta
        PurchaseDTO purchaseDTO = new PurchaseDTO();
        purchaseDTO.setCustomer(customer);
        purchaseDTO.setPurchasesItems(List.of(item1, item2));
        purchaseDTO.setTotalValuePurchases(BigDecimal.valueOf(1020));

        when(purchasesUseCase.getLargestPurchaseOfTheYear(year)).thenReturn(purchaseDTO);

        // Act & Assert
        mockMvc.perform(get("/purchases/{year}", year))
                .andExpect(status().isOk())
                // Validando Customer
                .andExpect(jsonPath("$.customer.name").value("Carlos Souza"))
                .andExpect(jsonPath("$.customer.cpf").value("321.654.987-00"))
                // Validando lista de compras
                .andExpect(jsonPath("$.purchasesItems.length()").value(2))
                .andExpect(jsonPath("$.purchasesItems[0].totalValue").value(270))
                .andExpect(jsonPath("$.purchasesItems[0].date").value("2024-03-15"))
                .andExpect(jsonPath("$.purchasesItems[0].productList[0].id").value(2))
                .andExpect(jsonPath("$.purchasesItems[0].productList[0].quantity").value(3))
                .andExpect(jsonPath("$.purchasesItems[1].totalValue").value(750))
                .andExpect(jsonPath("$.purchasesItems[1].date").value("2024-01-05"))
                .andExpect(jsonPath("$.purchasesItems[1].productList[0].id").value(5))
                .andExpect(jsonPath("$.purchasesItems[1].productList[0].quantity").value(5))
                // Validando valor total da compra
                .andExpect(jsonPath("$.totalValuePurchases").value(1020));
    }
}
