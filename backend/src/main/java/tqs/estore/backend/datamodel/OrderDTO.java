package tqs.estore.backend.datamodel;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderDTO {
    private Double totalPrice;
    private Integer acpID;
    private Long userId;
    private Map<Long, Integer> plantQuantityMap;
}