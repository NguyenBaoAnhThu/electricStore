package org.example.electricstore.DTO.transaction;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryTransactionDTO {

    private Integer id;
    @NotNull(message = "Giao dịch cần có thông tin sản phẩm !")
    private Integer product_id;
    @NotNull(message = "Không được để trống số lượng cần giao dịch !")
    private Integer quantity;
    private Integer employee_id;
    private LocalDateTime created_at;
}
