package io.project.KitapChooseBot.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends JpaRepository<User, Long>{
	
	
	User findBychatId(long chatId);
	
	Optional<User> findById(Long long1);
	
//	List<User> findBychatId(Long userId);// or findByUser_Id
	
	

}
