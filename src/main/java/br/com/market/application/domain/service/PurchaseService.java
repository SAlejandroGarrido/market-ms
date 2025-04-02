package br.com.market.application.domain.service;

import br.com.market.adapter.out.web.feing.adapter.ProductAdapter;
import br.com.market.adapter.out.web.feing.adapter.PurchasesAdapter;
import br.com.market.application.domain.dto.in.CustomerDTO;
import br.com.market.application.domain.dto.in.CustomerLoyalDTO;
import br.com.market.application.domain.dto.in.PurchaseDTO;
import br.com.market.application.domain.dto.in.PurchasesItemDTO;
import br.com.market.application.domain.model.CustomerPurchase;
import br.com.market.application.domain.model.Product;
import br.com.market.application.domain.model.PurchasesItem;
import br.com.market.application.port.in.CustomersAndPurchasesUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PurchaseService implements CustomersAndPurchasesUseCase {

    private final PurchasesAdapter purchasesAdapter;
    private final ProductAdapter productAdapter;
    private final ModelMapper modelMapper;

    @Override
    public List<PurchaseDTO> getPurchasesOrderedByValue() {
        log.info("Fetching purchases ordered by value");
        var purchaseDTOS = getPurchasesDTOOrderByTotalValue(purchasesAdapter.getPurchases());

        purchaseDTOS.forEach(purchases -> enrichPurchaseWithProduct(purchases,  productAdapter.getProduct()));
        purchaseDTOS.forEach(PurchaseDTO::calculeteTotalValueAndSetValue);

        return purchaseDTOS;
    }

    @Override
    public PurchaseDTO getLargestPurchaseOfTheYear(int year) {
        var largePurchases = getLargePurchases(year, purchasesAdapter.getPurchases());

        enrichPurchaseWithProduct(largePurchases, productAdapter.getProduct());
        largePurchases.calculeteTotalValueAndSetValue();

        return largePurchases;
    }

    @Override
    public List<CustomerLoyalDTO> getTopCustomer() {
        log.info("Fetching top 3 clients with the most frequent and high-value purchases");
        var purchasesList = purchasesAdapter.getPurchases();
        var purchaseDTOList = convertToPurchaseDTO(purchasesList);

        Map<String, BigDecimal> clientTotalPurchaseValue = new HashMap<>();
        Map<String, CustomerDTO> mapCustomer= new HashMap<>();

        purchaseDTOList.forEach(purchaseDTO -> {
            purchaseDTO.calculeteTotalValueAndSetValue();
            mapCustomer.put(purchaseDTO.getCustomer().getCpf(), purchaseDTO.getCustomer());
            clientTotalPurchaseValue.merge(purchaseDTO.getCustomer().getCpf(), purchaseDTO.getTotalValuePurchases(), BigDecimal::add);
        });

        return clientTotalPurchaseValue.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry<String, BigDecimal>::getValue).reversed())
                .limit(3)
                .map(entry -> getBuildCustomerLoyal(entry, mapCustomer))
                .collect(Collectors.toList());
    }

    private static CustomerLoyalDTO getBuildCustomerLoyal(Map.Entry<String, BigDecimal> entry, Map<String, CustomerDTO> mapCustomer) {
        return CustomerLoyalDTO.builder()
                .totalValuePurchase(entry.getValue())
                .cpf(entry.getKey())
                .name(mapCustomer.get(entry.getKey()).getName())
                .build();
    }

    private List<PurchaseDTO> convertToPurchaseDTO(List<CustomerPurchase> purchases) {
        return purchases.stream()
                .map(purchaseItem -> modelMapper.map(purchaseItem, PurchaseDTO.class))
                .toList();
    }

    private PurchaseDTO getLargePurchases(int year, List<CustomerPurchase> purchases) {
        var largePurchases = purchases.stream()
                .filter(compra -> compra.getPurchasesItems().stream()
                        .map(PurchasesItem::getDate)
                        .allMatch(localDate -> localDate.getYear() == year))

                .max(Comparator.comparing(p -> p.getPurchasesItems()
                        .stream()
                        .map(PurchasesItem::getTotalValue)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)))

                .map(purchaseItem -> modelMapper.map(purchaseItem, PurchaseDTO.class))

                .orElseThrow(() -> new RuntimeException("Nenhuma compra encontrada para o ano " + year));

        largePurchases.calculeteTotalValueAndSetValue();
        log.info("Maior compra do ano {}: {}", year, largePurchases);

        return largePurchases;
    }

    private List<PurchaseDTO> getPurchasesDTOOrderByTotalValue(List<CustomerPurchase> customerPurchases) {
        return customerPurchases.stream()
                .sorted(Comparator.comparing(p -> p.getPurchasesItems()
                        .stream()
                        .map(PurchasesItem::getTotalValue)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)))
                .map(purchaseItem -> modelMapper.map(purchaseItem, PurchaseDTO.class))
                .collect(Collectors.toList());
    }

    private void enrichPurchaseWithProduct(PurchaseDTO largePurchases, List<Product> productList) {
        Map<Long, Product> productMap = productList.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        largePurchases.getPurchasesItems().stream().map(PurchasesItemDTO::getProductList)
                .forEach(productDTOS -> productDTOS.stream()
                        .filter(prod -> productMap.containsKey(prod.getId()))
                        .forEach(productDTO -> {
                            var product = productMap.get(productDTO.getId());
                            modelMapper.map(product, productDTO);
                        }));
    }
}
