package cz.itnetwork.dto.Invoice;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthlyTurnoverDTO {
    private String month;        // např. "2025-03"
    private Double turnover;     // součet cen faktur v daném měsíci
}
