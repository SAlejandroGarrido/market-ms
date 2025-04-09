package br.com.market.adapter.in.web.controller.api;

import br.com.market.adapter.in.web.controller.exception.ExceptionBody;
import br.com.market.application.domain.dto.in.PurchaseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Purchases", description = "Endpoints relacionados a compras")
public interface PurchasesApi {

    @Operation(summary = "Obter todas as compras", description = "Retorna uma lista de compras ordenadas pelo valor total.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de compras retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao consumir a API de compras",
                    content = @Content(schema = @Schema(implementation = ExceptionBody.class)))
    })
    List<PurchaseDTO> getPurchases();

    @Operation(summary = "Obter a maior compra do ano", description = "Retorna a maior compra realizada no ano especificado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Maior compra do ano retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhuma compra encontrada para o ano especificado", content =
            @Content(schema = @Schema(implementation = ExceptionBody.class))),
            @ApiResponse(responseCode = "500", description = "Erro ao consumir a API de compras",content =
            @Content(schema = @Schema(implementation = ExceptionBody.class)))
    })
    PurchaseDTO getLargestPurchaseOfTheYear(int year);
}
