package cz.itnetwork.dto.Person;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonStatisticsDTO {

    private Integer personId;

    private String personName;

    private Double revenue;
}
