package br.com.market.application.domain.service;

import br.com.market.adapter.out.web.feing.adapter.ProductAdapter;
import br.com.market.adapter.out.web.feing.adapter.PurchasesAdapter;
import br.com.market.application.domain.dto.in.PurchaseDTO;
import br.com.market.application.domain.dto.in.PurchasesItemDTO;
import br.com.market.application.domain.model.CustomerPurchase;
import br.com.market.application.domain.model.Product;
import br.com.market.application.domain.model.PurchasesItem;
import br.com.market.application.port.in.PurchasesUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PurchaseService implements PurchasesUseCase {

    private final PurchasesAdapter purchasesAdapter;
    private final ProductAdapter productAdapter;
    private final ModelMapper modelMapper;

    @Override
    public List<PurchaseDTO> getPurchasesOrderedByValue() {
        log.info("Fetching purchases ordered by value");
        var purchaseDTOS = getPurchasesDTO(purchasesAdapter.getPurchases());

        purchaseDTOS.forEach(purchases -> enrichPurchaseWithProduct(purchases,  productAdapter.getProduct()));
        purchaseDTOS.forEach(PurchaseDTO::setTotalValuePurchases);

        return purchaseDTOS;
    }

    @Override
    public PurchaseDTO getLargestPurchaseOfTheYear(int year) {
        var largePurchases = getLargePurchases(year, purchasesAdapter.getPurchases());

        enrichPurchaseWithProduct(largePurchases, productAdapter.getProduct());
        largePurchases.setTotalValuePurchases();

        return largePurchases;
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

        largePurchases.setTotalValuePurchases();
        log.info("Maior compra do ano {}: {}", year, largePurchases);

        return largePurchases;
    }

    private List<PurchaseDTO> getPurchasesDTO(List<CustomerPurchase> customerPurchases) {
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
