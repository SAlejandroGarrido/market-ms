package br.com.market.application.util;

import br.com.market.application.domain.dto.in.CustomerDTO;
import br.com.market.application.domain.dto.in.CustomerLoyalDTO;
import br.com.market.application.domain.dto.in.PurchaseDTO;
import br.com.market.application.domain.dto.in.PurchasesProductDTO;
import br.com.market.application.domain.model.CustomerPurchase;
import br.com.market.application.domain.model.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Component
public class PurchaseUtil {

    public static List<PurchaseDTO> convertToPurchaseDTO(List<CustomerPurchase> purchases) {
       try{
           return purchases.stream()
                   .filter(purchase -> purchase.getPurchaseProducts() != null)
                   .map(purchase -> PurchaseDTO.builder()
                           .customer(CustomerDTO.builder().cpf(purchase.getCpf()).name(purchase.getName()).build())
                           .purchasesProducts(purchase.getPurchaseProducts().stream().map(productPurchase ->
                                   PurchasesProductDTO.builder()
                                           .id(productPurchase.getId())
                                           .quantity(productPurchase.getQuantity())
                                           .build()).toList())
                           .build())
                   .toList();
       }catch (Exception ex){
           return Collections.emptyList();
       }
    }

    public static void enrichPurchaseWithProduct(PurchaseDTO purchase, List<PurchasesProductDTO> purchasesProductDTO) {
        Map<Long, PurchasesProductDTO> productMap = purchase.getPurchaseProductDTOMap(purchasesProductDTO);

        purchase.getPurchasesProducts().forEach(item -> {
            var product = productMap.get(item.getId());
            if (product != null) {
                item.setId(product.getId());
                item.setPurchaseYear(product.getPurchaseYear());
                item.setHarvest(product.getHarvest());
                item.setPrice(product.getPrice());
                item.setWineType(product.getWineType());
                item.updateTotalValue();
            }
        });
    }

    public static CustomerLoyalDTO getBuildCustomerLoyal(Map.Entry<String, BigDecimal> entry, Map<String, CustomerDTO> mapCustomer) {
        return CustomerLoyalDTO.builder()
                .totalValuePurchase(entry.getValue())
                .cpf(entry.getKey())
                .name(mapCustomer.get(entry.getKey()).getName())
                .build();
    }

    public static List<PurchasesProductDTO> convertToProductDTO(List<Product> products) {
       try {
           return products.stream().map(product -> PurchasesProductDTO.builder()
                           .id(product.getId())
                           .price(product.getPrice())
                           .wineType(product.getWineType())
                           .harvest(product.getHarvest())
                           .purchaseYear(product.getPurchaseYear())
                           .build())
                   .collect(Collectors.toList());
       }catch (Exception ex){
           return Collections.emptyList();
       }
    }

    public static Map<String, CustomerDTO> getMapCustomer(List<PurchaseDTO> purchaseDTOList) {
        Map<String, CustomerDTO> mapCustomer = new HashMap<>();
        purchaseDTOList.forEach(purchaseDTO -> mapCustomer.put(purchaseDTO.getCustomer().getCpf(),
                purchaseDTO.getCustomer()));
        return mapCustomer;
    }

    public static Map<String, BigDecimal> mapCpfWithPurchaseValue(List<PurchaseDTO> purchaseDTOList) {
        Map<String, BigDecimal> mapCpfWithPurchaseValue = new HashMap<>();

        purchaseDTOList.forEach(purchaseDTO -> mapCpfWithPurchaseValue.merge(purchaseDTO.getCustomer().getCpf(),
                purchaseDTO.getPurchasesTotalValue(), BigDecimal::add));
        return mapCpfWithPurchaseValue;
    }

    public static Map<CustomerDTO, Map<String, Long>> mapWineRecurringByCustomer(List<PurchaseDTO> purchaseDTOList) {
        return purchaseDTOList.stream()
                .collect(Collectors.groupingBy(
                        PurchaseDTO::getCustomer,
                        Collectors.flatMapping(
                                purchase -> purchase.getPurchasesProducts().stream()
                                        .map(PurchasesProductDTO::getWineType)
                                        .filter(Objects::nonNull),
                                Collectors.groupingBy(
                                        wineType -> wineType,
                                        Collectors.counting()
                                )
                        )
                ));
    }

    public static Map<CustomerDTO, String> mapTypeWineByCustomer(Map<CustomerDTO, Map<String, Long>> mapCpfWithWine) {
        return mapCpfWithWine.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().entrySet().stream()
                                .max(Map.Entry.comparingByValue())
                                .map(Map.Entry::getKey)
                                .orElse("Sem recomendação")
                ));
    }
}
