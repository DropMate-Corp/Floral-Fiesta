package tqs.estore.backend.datamodel;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

}
