package br.com.market.application.domain.service;

import br.com.market.adapter.out.web.feing.adapter.PurchasesAdapter;
import br.com.market.application.domain.dto.in.PurchaseDTO;
import br.com.market.application.domain.model.PurchasesItem;
import br.com.market.application.port.in.PurchasesUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PurchaseService implements PurchasesUseCase {

    private final PurchasesAdapter purchasesAdapter;
    private final ModelMapper modelMapper;

    @Override
    public Page<PurchaseDTO> getPurchasesOrderedByValue(int page, int size) {
        log.info("Fetching purchases ordered by value");
        var customerPurchases = purchasesAdapter.getOrderedByValue();

        var purchaseDTOs = customerPurchases.stream()
                .sorted(Comparator.comparing(p -> p.getPurchasesItems()
                        .stream()
                        .map(PurchasesItem::getTotalValue)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)))
                .map(purchaseItem -> modelMapper.map(purchaseItem, PurchaseDTO.class))
                .collect(Collectors.toList());

        return new PageImpl<>(purchaseDTOs, PageRequest.of(page, size), customerPurchases.size());

    }
}
