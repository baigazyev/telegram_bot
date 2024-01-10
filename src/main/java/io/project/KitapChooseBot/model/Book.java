package io.project.KitapChooseBot.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.transaction.Transactional;

@Entity(name = "booksDataTable")
@Transactional

public class Book {
	

	private String whoAdd;
	
	private String author;
	@Id
	private String nameOfBook;
	
	private Integer year;
	
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "club_id")
	private Club club;
	
	public Book() {
		
	}
	
	public Book(String whoAdd, String nameOfBook, String author, Integer year) {
		this.whoAdd = whoAdd;
		this.author = author;
		this.nameOfBook = nameOfBook;
		this.year = year;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public String getNameOfBook() {
		return nameOfBook;
	}
	
	public String getWhoAdd() {
		return whoAdd;
	}
	
	public Integer getYear() {
		return year;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public void setNameOfBook(String nameOfBook) {
		this.nameOfBook = nameOfBook;
	}
	
	public void setWhoAdd(String whoAdd) {
		this.whoAdd = whoAdd;
	}
	
	public void setYear(Integer year) {
		this.year = year;
	}
	
	@Override
	public String toString() {
		return "Қосқан: " + whoAdd + "\n" + ", авторы: " + author + "\n" + ", кітап атауы: " + nameOfBook + "\n"+ ", жылы: "
				+ year + ";" + "\n" + "\n";
	}
	




}
