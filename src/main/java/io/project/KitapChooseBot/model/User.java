package io.project.KitapChooseBot.model;

import java.sql.Timestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;


@Entity(name = "usersDataTable")

public class User {
	
	
	@Id
	private Long chatId;
	
	private String firstName;
	
	private String lastName;
	
	private String userName;
	
	private Timestamp registeredAt;
	
	@OneToOne(cascade = CascadeType.ALL,mappedBy = "head")
	private Club clubHead;
	
	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "club_id")
	private Club club;
	
	
	

	public Timestamp getRegisteredAt() {
		return registeredAt;
	}

	public void setRegisteredAt(Timestamp registeredAt) {
		this.registeredAt = registeredAt;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public Long getChatId() {
		return chatId;
	}
	
	public void setChatId(Long chatId) {
		this.chatId = chatId;
	}

	@Override
	public String toString() {
		return "User [chatId=" + chatId + ", firstName=" + firstName + ", lastName=" + lastName + ", userName="
				+ userName + ", registeredAt=" + registeredAt + "]";
	}
	
	
	
	
	

}
