package br.com.market.application.domain.service;

import br.com.market.adapter.out.web.feing.adapter.PurchasesAdapter;
import br.com.market.application.domain.dto.in.PurchaseDTO;
import br.com.market.application.domain.model.PurchasesItem;
import br.com.market.application.port.in.PurchasesUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PurchaseService implements PurchasesUseCase {

    private final PurchasesAdapter purchasesAdapter;
    private final ModelMapper modelMapper;

    @Override
    public List<PurchaseDTO> getPurchasesOrderedByValue() {
        log.info("Fetching purchases ordered by value");
        var customerPurchases = purchasesAdapter.getPurchases();

        return customerPurchases.stream()
                .sorted(Comparator.comparing(p -> p.getPurchasesItems()
                        .stream()
                        .map(PurchasesItem::getTotalValue)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)))
                .map(purchaseItem -> modelMapper.map(purchaseItem, PurchaseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public PurchaseDTO getLargestPurchaseOfTheYear(int year) {
        var purchases = purchasesAdapter.getPurchases();

        var largePurchases = purchases.stream()
                .filter(compra -> compra.getPurchasesItems().stream()
                        .map(PurchasesItem::getDate)
                        .allMatch(localDate -> localDate.getYear() == year))
                .max(Comparator.comparing(p -> p.getPurchasesItems()
                        .stream()
                        .map(PurchasesItem::getTotalValue)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)))
                .map(purchaseItem -> modelMapper.map(purchaseItem, PurchaseDTO.class))
                .orElseThrow(() ->  new RuntimeException("Nenhuma compra encontrada para o ano " + year));
        log.info("Maior compra do ano {}: {}", year, largePurchases);

        largePurchases.setTotalValuePurchases();

        return largePurchases;
    }
}
