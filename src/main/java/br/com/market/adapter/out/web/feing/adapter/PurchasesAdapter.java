package br.com.market.adapter.out.web.feing.adapter;

import br.com.market.adapter.out.web.feing.PurchasesClient;
import br.com.market.application.domain.model.CustomerPurchase;
import br.com.market.application.port.out.PurchasesPort;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Component
public class PurchasesAdapter implements PurchasesPort {

    private final PurchasesClient purchasesClient;

    @Override
    public List<CustomerPurchase> getOrderedByValue() {
        try {
            return purchasesClient.getPurchases().getCustomerPurchases();
        } catch (FeignException e) {
            throw new RuntimeException("Erro ao consumir a API: " + e.status(), e);
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao buscar compras", e);
        }

    }
}
