package br.com.market.application.domain.dto.in;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@Schema(description = "Representa um cliente fiel com informações de compras.")
public class CustomerLoyalDTO {

    @Schema(description = "Nome do cliente", example = "Carlos Souza")
    private String name;

    @Schema(description = "CPF do cliente", example = "321.654.987-00")
    private String cpf;

    @Schema(description = "Valor total das compras do cliente", example = "500.00")
    private BigDecimal totalValuePurchase;
}