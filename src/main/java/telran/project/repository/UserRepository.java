package telran.project.repository;

import org.springframework.data.repository.CrudRepository;
import telran.project.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {

}