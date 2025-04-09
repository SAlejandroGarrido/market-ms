package br.com.market.application.domain.service;

import br.com.market.adapter.out.web.feing.adapter.ProductAdapter;
import br.com.market.adapter.out.web.feing.adapter.PurchasesAdapter;
import br.com.market.application.domain.dto.in.CustomerDTO;
import br.com.market.application.domain.dto.in.CustomerLoyalDTO;
import br.com.market.application.domain.dto.in.PurchaseDTO;
import br.com.market.application.domain.dto.in.PurchasesProductDTO;
import br.com.market.adapter.in.web.controller.exception.PurchaseNotFoundException;
import br.com.market.application.domain.dto.in.RecommendationProduct;
import br.com.market.application.port.in.CustomersAndPurchasesUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static br.com.market.application.util.PurchaseUtil.convertToProductDTO;
import static br.com.market.application.util.PurchaseUtil.convertToPurchaseDTO;
import static br.com.market.application.util.PurchaseUtil.enrichPurchaseWithProduct;
import static br.com.market.application.util.PurchaseUtil.getBuildCustomerLoyal;
import static br.com.market.application.util.PurchaseUtil.getMapCustomer;
import static br.com.market.application.util.PurchaseUtil.mapTypeWineByCustomer;
import static br.com.market.application.util.PurchaseUtil.mapCpfWithPurchaseValue;
import static br.com.market.application.util.PurchaseUtil.mapWineRecurringByCustomer;

@Slf4j
@RequiredArgsConstructor
@Service
public class PurchaseService implements CustomersAndPurchasesUseCase {

    private final PurchasesAdapter purchasesAdapter;
    private final ProductAdapter productAdapter;

    @Override
    public List<PurchaseDTO> getPurchasesOrderedByValue() {
        log.info("Fetching purchases ordered by value");
        var customerPurchases = purchasesAdapter.getPurchases();
        var purchaseDTOS = convertToPurchaseDTO(customerPurchases);

        var productsDTOS = convertToProductDTO(productAdapter.getProducts());

        purchaseDTOS.forEach(purchase -> enrichPurchaseWithProduct(purchase, productsDTOS));
        purchaseDTOS.forEach(PurchaseDTO::setTotalValuePurchase);

        var sortedPurchases = getPurchaseByValueAsc(purchaseDTOS);
        log.info("Sorted purchases by value: {}", sortedPurchases);
        return sortedPurchases;
    }

    @Override
    public PurchaseDTO getLargestPurchaseOfTheYear(int year) {
        log.info("Fetching largest purchase of the year {}", year);
        var purchaseDTOS = convertToPurchaseDTO(purchasesAdapter.getPurchases());
        var productsDTO = convertToProductDTO(productAdapter.getProducts());

        purchaseDTOS.forEach(purchases -> enrichPurchaseWithProduct(purchases, productsDTO));

        var purchaseDTOFilter = filterPurchaserByYear(year, purchaseDTOS);

        purchaseDTOS.forEach(PurchaseDTO::setTotalValuePurchase);

        var largestPurchase = getLargestPurchase(purchaseDTOFilter, year);
        log.info("Largest purchase of the year {}: {}", year, largestPurchase);
        return largestPurchase;
    }

    @Override
    public List<CustomerLoyalDTO> getTopCustomer() {
        log.info("Fetching top 3 clients with the most frequent and high-value purchases");
        var purchaseDTOList = convertToPurchaseDTO(purchasesAdapter.getPurchases());
        var productsDTO = convertToProductDTO(productAdapter.getProducts());

        purchaseDTOList.forEach(purchases -> enrichPurchaseWithProduct(purchases, productsDTO));
        purchaseDTOList.forEach(PurchaseDTO::setTotalValuePurchase);

        var mapCpfWithPurchaseValue = mapCpfWithPurchaseValue(purchaseDTOList);
        var topPurchase = getTopPurchase(mapCpfWithPurchaseValue);
        log.info("Top 3 clients: {}", topPurchase);

        return topPurchase.entrySet().stream()
                .map(entry -> getBuildCustomerLoyal(entry, getMapCustomer(purchaseDTOList)))
                .collect(Collectors.toList());
    }

    @Override
    public List<RecommendationProduct> recommendationTypeBasedOnRecurringPurchases() {
        log.info("Iniciando recomendação de tipos de vinho com base em compras recorrentes");
        var purchaseDTOList = convertToPurchaseDTO(purchasesAdapter.getPurchases());
        var productsDTO = convertToProductDTO(productAdapter.getProducts());

        purchaseDTOList.forEach(purchases -> enrichPurchaseWithProduct(purchases, productsDTO));

        var mapWineRecurringByCustomer = mapWineRecurringByCustomer(purchaseDTOList);

        var mapTypeWineByCustomer = mapTypeWineByCustomer(mapWineRecurringByCustomer);

        var recommendationProductList = getRecommendationProductList(mapTypeWineByCustomer, productsDTO);
        log.info("Lista final de produtos recomendados: {}", recommendationProductList);

        return recommendationProductList;
    }

    private List<RecommendationProduct> getRecommendationProductList(Map<CustomerDTO, String> recommendation, List<PurchasesProductDTO> productsDTO) {
        return recommendation.entrySet().stream().map(entry ->
                RecommendationProduct.builder()
                        .cpf(entry.getKey().getCpf())
                        .name(entry.getKey().getName())
                        .wineType(productsDTO.stream().map(PurchasesProductDTO::getWineType)
                                .filter(wineType -> wineType.equals(entry.getValue()))
                                .findFirst()
                                .orElse("Sem recomendação"))
                        .build()).toList();
    }

    private PurchaseDTO getLargestPurchase(List<PurchaseDTO> purchaseDTOS, int year) {
        var purchaseDTO = purchaseDTOS.stream()
                .max(Comparator.comparing(purchase -> purchase.getPurchasesProducts().stream()
                        .map(PurchasesProductDTO::getTotalValue)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)))
                .orElseThrow(() -> new PurchaseNotFoundException("Nenhuma compra encontrada para o ano " + year));
        log.info("Maior compra do ano {}: {}", year, purchaseDTO);

        return purchaseDTO;
    }

    private List<PurchaseDTO> getPurchaseByValueAsc(List<PurchaseDTO> purchaseDTOS) {
        return purchaseDTOS.stream()
                .sorted(Comparator.comparing(PurchaseDTO::getPurchasesTotalValue))
                .toList();
    }

    private List<PurchaseDTO> filterPurchaserByYear(int year, List<PurchaseDTO> purchaseDTOS) {
        return purchaseDTOS.stream()
                .filter(purchaseDTO -> purchaseDTO.getPurchasesProducts().stream()
                        .allMatch(purchaseProductDTO -> purchaseProductDTO.getPurchaseYear() == year))
                .toList();
    }

    private Map<String, BigDecimal> getTopPurchase(Map<String, BigDecimal> clientTotalPurchaseValue) {
        return clientTotalPurchaseValue.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry<String, BigDecimal>::getValue).reversed())
                .limit(3)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

}