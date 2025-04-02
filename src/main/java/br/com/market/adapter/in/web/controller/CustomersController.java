package br.com.market.adapter.in.web.controller;


import br.com.market.application.domain.dto.in.CustomerLoyalDTO;
import br.com.market.application.port.in.CustomersAndPurchasesUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/customers")
public class CustomersController {

    private final CustomersAndPurchasesUseCase customersAndPurchasesUseCase;

    @GetMapping("/loyal")
    public List<CustomerLoyalDTO> getLargestPurchaseOfTheYear() {
        return customersAndPurchasesUseCase.getTopCustomer();
    }
}
