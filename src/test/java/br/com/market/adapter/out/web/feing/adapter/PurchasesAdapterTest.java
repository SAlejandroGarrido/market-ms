package br.com.market.adapter.out.web.feing.adapter;

import br.com.market.adapter.out.web.feing.PurchasesClient;
import br.com.market.adapter.out.web.feing.exception.PurchaseAdapterException;
import br.com.market.application.domain.model.CustomerPurchase;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PurchasesAdapterTest {

    @Mock
    private PurchasesClient purchasesClient;

    @InjectMocks
    private PurchasesAdapter purchasesAdapter;

    @Test
    void getPurchases_ShouldReturnListOfCustomerPurchases() {
        List<CustomerPurchase> expectedPurchases = List.of(
                CustomerPurchase.builder()
                        .name("John Doe")
                        .cpf("123.456.789-00")
                        .build(),
                CustomerPurchase.builder()
                        .name("Jane Doe")
                        .cpf("987.654.321-00")
                        .build()
        );

        when(purchasesClient.getPurchases()).thenReturn(expectedPurchases);

        List<CustomerPurchase> actualPurchases = purchasesAdapter.getPurchases();

        assertEquals(expectedPurchases, actualPurchases);
    }

    @Test
    void getPurchases_ShouldThrowPurchaseAdapterException_WhenFeignExceptionOccurs() {
        when(purchasesClient.getPurchases()).thenThrow(FeignException.class);

        assertThrows(PurchaseAdapterException.class, () -> purchasesAdapter.getPurchases());
    }

    @Test
    void getPurchases_ShouldThrowPurchaseAdapterException_WhenUnexpectedExceptionOccurs() {
        when(purchasesClient.getPurchases()).thenThrow(RuntimeException.class);

        assertThrows(PurchaseAdapterException.class, () -> purchasesAdapter.getPurchases());
    }
}