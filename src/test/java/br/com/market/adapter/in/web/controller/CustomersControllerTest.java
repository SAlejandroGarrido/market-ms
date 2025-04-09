package br.com.market.adapter.in.web.controller;

import br.com.market.application.domain.dto.in.CustomerLoyalDTO;
import br.com.market.application.domain.dto.in.RecommendationProduct;
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

@WebMvcTest(CustomersController.class)
class CustomersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomersAndPurchasesUseCase customersAndPurchasesUseCase;

    @Test
    void getTopCustomers_ShouldReturnListOfLoyalCustomers() throws Exception {
        List<CustomerLoyalDTO> expectedCustomers = List.of(
                createCustomerLoyalDTO("Carlos Souza", "321.654.987-00", BigDecimal.valueOf(500)),
                createCustomerLoyalDTO("Ana Silva", "123.456.789-00", BigDecimal.valueOf(300))
        );

        when(customersAndPurchasesUseCase.getTopCustomer()).thenReturn(expectedCustomers);

        mockMvc.perform(get("/v1/api/market/customers/loyal"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(expectedCustomers.size()))
                .andExpect(jsonPath("$[0].name").value("Carlos Souza"))
                .andExpect(jsonPath("$[0].cpf").value("321.654.987-00"))
                .andExpect(jsonPath("$[0].totalValuePurchase").value(500))
                .andExpect(jsonPath("$[1].name").value("Ana Silva"))
                .andExpect(jsonPath("$[1].cpf").value("123.456.789-00"))
                .andExpect(jsonPath("$[1].totalValuePurchase").value(300));
    }

    @Test
    void recommendationTypeBasedOnRecurringPurchases_ShouldReturnListOfRecommendations() throws Exception {
        List<RecommendationProduct> expectedRecommendations = List.of(
                RecommendationProduct.builder()
                        .cpf("321.654.987-00")
                        .name("Carlos Souza")
                        .wineType("Tinto")
                        .build(),
                RecommendationProduct.builder()
                        .cpf("123.456.789-00")
                        .name("Ana Silva")
                        .wineType("Branco")
                        .build()
        );

        when(customersAndPurchasesUseCase.recommendationTypeBasedOnRecurringPurchases()).thenReturn(expectedRecommendations);

        mockMvc.perform(get("/v1/api/market/customers/recommendation/type"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(expectedRecommendations.size()))
                .andExpect(jsonPath("$[0].cpf").value("321.654.987-00"))
                .andExpect(jsonPath("$[0].name").value("Carlos Souza"))
                .andExpect(jsonPath("$[0].wineType").value("Tinto"))
                .andExpect(jsonPath("$[1].cpf").value("123.456.789-00"))
                .andExpect(jsonPath("$[1].name").value("Ana Silva"))
                .andExpect(jsonPath("$[1].wineType").value("Branco"));
    }

    private CustomerLoyalDTO createCustomerLoyalDTO(String name, String cpf, BigDecimal totalValuePurchase) {
        return CustomerLoyalDTO.builder()
                .name(name)
                .cpf(cpf)
                .totalValuePurchase(totalValuePurchase)
                .build();
    }
}