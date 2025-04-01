package br.com.market.adapter.in.web.controller;

import br.com.market.application.domain.dto.in.PurchaseDTO;
import br.com.market.application.port.in.PurchasesUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

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

        mockMvc.perform(get("/purchases")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk()) // Verifica se o status HTTP é 200
                .andExpect(jsonPath("$.length()").value(expectedPurchases.size())); // Verifica o tamanho da lista retornada
    }
}
