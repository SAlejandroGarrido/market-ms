package br.com.market.application.domain.dto.in;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
@Schema(description = "Informações do cliente")
public class CustomerDTO {

    @Schema(description = "Nome do cliente", example = "Carlos Souza")
    private String name;

    @Schema(description = "CPF do cliente", example = "321.654.987-00")
    private String cpf;
}