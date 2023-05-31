package tqs.estore.backend.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import tqs.estore.backend.datamodel.PlantCategory;
public interface CategoryRepository extends JpaRepository<PlantCategory, Long> { }