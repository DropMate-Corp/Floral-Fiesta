package tqs.estore.backend.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import tqs.estore.backend.datamodel.Plant;
import java.util.List;
public interface PlantRepository extends JpaRepository<Plant, Long> {
    List<Plant> findByNameContaining(String name);

    List<Plant> findByCategoryCategoryId(Long categoryId);

}