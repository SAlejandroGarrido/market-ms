package br.com.market.adapter.in.web.controller;

import br.com.market.adapter.in.web.controller.api.CustomersApi;
import br.com.market.application.domain.dto.in.CustomerLoyalDTO;
import br.com.market.application.domain.dto.in.RecommendationProduct;
import br.com.market.application.port.in.CustomersAndPurchasesUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${base.path}" +"/customers")
public class CustomersController implements CustomersApi {

    private final CustomersAndPurchasesUseCase useCase;

    @Override
    @GetMapping("/loyal")
    public List<CustomerLoyalDTO> getTopCustomers() {
        return useCase.getTopCustomer();
    }

    @Override
    @GetMapping("/recommendation/type")
    public List<RecommendationProduct> recommendationTypeBasedOnRecurringPurchases() {
        return useCase.recommendationTypeBasedOnRecurringPurchases();
    }
}