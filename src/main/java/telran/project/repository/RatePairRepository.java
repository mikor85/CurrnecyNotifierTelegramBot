package telran.project.repository;

import org.springframework.data.repository.CrudRepository;
import telran.project.entity.RatePair;
import telran.project.entity.User;

import java.util.List;

public interface RatePairRepository extends CrudRepository<RatePair, String> {

    List<RatePair> findByUser(User user);
}