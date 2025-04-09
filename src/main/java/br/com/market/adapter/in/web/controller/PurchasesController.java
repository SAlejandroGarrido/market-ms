package br.com.market.adapter.in.web.controller;

import br.com.market.adapter.in.web.controller.api.PurchasesApi;
import br.com.market.application.domain.dto.in.PurchaseDTO;
import br.com.market.application.port.in.CustomersAndPurchasesUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

    @Slf4j
    @RequiredArgsConstructor
    @RestController
    @RequestMapping("${base.path}" + "/purchases")
    public class PurchasesController implements PurchasesApi {

        private final CustomersAndPurchasesUseCase useCase;

        @Override
        @GetMapping
        public List<PurchaseDTO> getPurchases() {
            log.info("Received request to get all purchases ordered by value");
            return useCase.getPurchasesOrderedByValue();
        }

        @Override
        @GetMapping("/{year}")
        public PurchaseDTO getLargestPurchaseOfTheYear(@PathVariable int year) {
            return useCase.getLargestPurchaseOfTheYear(year);
        }
    }