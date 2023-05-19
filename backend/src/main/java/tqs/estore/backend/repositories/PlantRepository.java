package tqs.estore.backend.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import tqs.estore.backend.datamodel.Plant;
public interface PlantRepository extends JpaRepository<Plant, Long> { }