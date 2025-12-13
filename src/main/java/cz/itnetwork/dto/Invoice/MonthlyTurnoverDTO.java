package cz.itnetwork.dto.Invoice;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthlyTurnoverDTO {

    private String month;

    private Double turnover;
}
