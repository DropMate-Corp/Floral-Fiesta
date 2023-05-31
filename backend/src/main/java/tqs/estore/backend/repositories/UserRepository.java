package tqs.estore.backend.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import tqs.estore.backend.datamodel.User;
public interface UserRepository extends JpaRepository<User, Long>{
    User findByEmail(String email);

}