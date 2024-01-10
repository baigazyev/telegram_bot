package io.project.KitapChooseBot.model;

import java.util.ArrayList;
import java.util.Set;
import java.util.List;


//import org.hibernate.mapping.List;
//import org.hibernate.mapping.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity(name = "ClubsDataTable")
public class Club {
	
	@Id
	private int number;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "head_id")
	private User head;
	
	
	@OneToMany(targetEntity = User.class, fetch = FetchType.LAZY, mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<User> members = new ArrayList<>();
	
	private String password = "admin";

	@OneToMany(targetEntity = User.class, fetch = FetchType.LAZY,mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Book> books = new ArrayList<>();
	
	public Club() {
		
	}
	
	
	public Club(int number, String password) {
		this.number = number;
		this.password = password;
	
	}
	
	public boolean checkReaderOrNot(User user) {
		
		return this.members.contains(user);
	}
	
//	public User getHead() {
//		return head;
//	}
	
	
	public int getNumber() {
		return number;
	}
	
	public String getPassword() {
		return password;
	}
	
	
	public void setHead(User head) {
		this.head = head;
	}
	
	
	
	public void setNumber(int number) {
		this.number = number;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
//	public void setMember(User user) {
//		this.members.ad
//	}
	
//	public void setMember(User user) {
//		this.members.add(user);
//	}
//	
//	
//	
//	public ArrayList<User> getMembers() {
//		return members;
//	}
	
	public List<User> getMembers() {
		return members;
	}
	
	
	
	@Override
	public String toString() {
		
		return super.toString();
	}
	
//	public ArrayList<Book> getBooks() {
//		return books;
//	}
//	
//	public void setBooks(ArrayList<Book> books) {
//		this.books = books;
//	}
//	
	
	
//	public List getBooks() {
//		return books;
//	}
//	
//	public List getMembers() {
//		return members;
//	}
//	
//	public void setBooks(List books) {
//		this.books = books;
//	}
	
//	public void setMembers(List list) {
//		this.members = list;
//	}
	
	public void setMember(User user) {
		this.members.add(user);		}
	
	
//	public void setMembers(ArrayList<User> members) {
//		this.members = members;
//	}
	
	
	
	
	
	
	
	
	
	
//	public void setMembers(ArrayList<User> members) {
//		this.members = members;
//	}
//	
	
	

}
