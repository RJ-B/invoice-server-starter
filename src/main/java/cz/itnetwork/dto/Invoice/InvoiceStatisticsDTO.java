package cz.itnetwork.dto.Invoice;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceStatisticsDTO {

    private Double currentYearSum;

    private Double allTimeSum;

    private Integer invoicesCount;
}
