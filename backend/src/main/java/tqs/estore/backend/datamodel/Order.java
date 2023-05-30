package tqs.estore.backend.datamodel;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.Map;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long orderId;

    @Column(nullable = false)
    private Double totalPrice;

    @Column(nullable = false)
    private String pickupCode;

    @Column(nullable = false)
    private String description;

    @Convert(converter = StatusConverter.class)
    private Status status;

    @Column
    @Temporal(TemporalType.DATE)
    private Date deliveryDate;

    @Column
    @Temporal(TemporalType.DATE)
    private Date pickupDate;

    @Column
    private Integer acpID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ElementCollection
    @CollectionTable(name = "order_plant_quantity", joinColumns = @JoinColumn(name="orderId"))
    @MapKeyJoinColumn(name = "quantity")
    private Map<Plant, Integer> plantQuanityMap;

}
