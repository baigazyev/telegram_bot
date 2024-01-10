package io.project.KitapChooseBot.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository("clubRepository")
public interface ClubRepository extends JpaRepository<Club, Integer>{
	
	Boolean existsBynumber(int number);
	
	Club findBynumber(int number);
	
	
	
	
	
//	Boolean existsBychatId(long chatId);
	
//	List<User> findBychatId(Long userId);// or findByUser_Id
//	List<User> findByOrganizationId(Long orgId);// or findByOrganization_Id
	 

}
