package tqs.estore.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tqs.estore.backend.datamodel.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
