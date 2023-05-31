package tqs.estore.backend.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import tqs.estore.backend.datamodel.Order;
import tqs.estore.backend.datamodel.Status;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserUserIdAndStatus(Long userId, Status status);
}