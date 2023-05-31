package tqs.estore.backend.datamodel;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "plantCategories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PlantCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String photo;

}
