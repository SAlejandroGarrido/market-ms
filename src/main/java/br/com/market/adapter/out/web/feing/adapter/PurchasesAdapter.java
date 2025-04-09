package br.com.market.adapter.out.web.feing.adapter;

import br.com.market.adapter.out.web.feing.exception.PurchaseAdapterException;
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
    public List<CustomerPurchase> getPurchases() {
        log.info("Iniciando a busca de compras via PurchasesClient.");
        try {
            List<CustomerPurchase> purchases = purchasesClient.getPurchases();
            log.info("Busca de compras concluída com sucesso. Total de compras encontradas: {}", purchases.size());
            return purchases;
        } catch (FeignException e) {
            log.error("Erro ao consumir a API de compras: {}", e.getMessage(), e);
            throw new PurchaseAdapterException("Erro ao consumir a API: " + e.status(), e);
        } catch (Exception e) {
            log.error("Erro inesperado ao buscar compras: {}", e.getMessage(), e);
            throw new PurchaseAdapterException("Erro inesperado ao buscar compras", e);
        }
    }
}
