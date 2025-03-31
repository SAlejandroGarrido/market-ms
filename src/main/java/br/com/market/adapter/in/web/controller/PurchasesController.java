package br.com.market.adapter.in.web.controller;


import br.com.market.application.domain.dto.in.PurchaseDTO;
import br.com.market.application.port.in.PurchasesUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/purchases")
public class PurchasesController {

    private final PurchasesUseCase purchasesUseCase;

    @GetMapping
    public Page<PurchaseDTO> getPurchases(@RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "1") int size) throws Exception {
        log.info("Received request to get all purchases ordered by value");
        return purchasesUseCase.getPurchasesOrderedByValue(page,size);
    }
}
