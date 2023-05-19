package tqs.estore.backend.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import tqs.estore.backend.datamodel.Order;
public interface OrderRepository extends JpaRepository<Order, Long> { }