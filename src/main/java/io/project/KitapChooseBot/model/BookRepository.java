package io.project.KitapChooseBot.model;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Integer>{
	
	
	List<Book> findBynameOfBook(String nameOfBook);
	
	
	Boolean existsBynameOfBook(String nameOfBook);
	
	Long deleteBynameOfBook(String nameOfBook);
	
	Book findByNameOfBook(String nameOfBook);

//    @Query("select b from booksDataTable b where b.nameOfBook = :nameOfBook")
//    Stream<Book> findByNameReturnStream(@Param("nameOfBook") String nameOfBook);
	
	

}
