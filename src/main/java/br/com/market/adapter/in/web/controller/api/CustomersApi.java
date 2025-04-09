package br.com.market.adapter.in.web.controller.api;

import br.com.market.adapter.in.web.controller.exception.ExceptionBody;
import br.com.market.application.domain.dto.in.CustomerLoyalDTO;
import br.com.market.application.domain.dto.in.RecommendationProduct;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Tag(name = "Customers", description = "Endpoints relacionados a clientes")
public interface CustomersApi {

        @GetMapping("/loyal")
        @Operation(summary = "Obter clientes fiéis", description = "Retorna uma lista dos clientes mais fiéis.")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "Lista de clientes fiéis retornada com sucesso"),
                @ApiResponse(responseCode = "500", description = "Erro ao consumir a API de clientes",
                        content = @Content(schema = @Schema(implementation = ExceptionBody.class)))
        })
        List<CustomerLoyalDTO> getTopCustomers() ;

        @GetMapping("/recommendation/type")
        @Operation(summary = "Recomendações de tipo de vinho", description = "Retorna recomendações de tipo de vinho com base em compras recorrentes.")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "Recomendações retornadas com sucesso"),
                @ApiResponse(responseCode = "500", description = "Erro ao consumir a API de clientes",
                        content = @Content(schema = @Schema(implementation = ExceptionBody.class)))
        })
        List<RecommendationProduct> recommendationTypeBasedOnRecurringPurchases() ;
}
