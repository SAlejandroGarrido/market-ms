package br.com.market.application.domain.dto.in;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Representa uma recomendação de produto para um cliente.")
public class RecommendationProduct {

    @Schema(description = "CPF do cliente", example = "321.654.987-00")
    private String cpf;

    @Schema(description = "Nome do cliente", example = "Carlos Souza")
    private String name;

    @Schema(description = "Tipo de vinho recomendado", example = "Tinto")
    private String wineType;
}