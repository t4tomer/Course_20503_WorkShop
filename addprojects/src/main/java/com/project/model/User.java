package com.project.model;

// import jakarta.persistence.email;
import java.util.Random;
import javax.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.springframework.data.annotation.CreatedDate;

@Entity
// create table called Product_Table in the Project_Data_Base
@Table(name = "Users_Table")

public class User {

	private String fname;// user first name
	private String lname;// user last name
	@Id // make email the primery key
	private String email;// user email(used as the primary key for the Users_Table)
	private String passwd;// user passwrod to log in the site
	private String dob;// date of birth of the user
	private String gender; // gender of the user
	private String balance;// amount of meony in the user account
	private String title;// the title of the user, is it manager or client

	public User() {
		System.out.println("empty constructor");
	}

	// object used for users of type vistor
	public User(String fname) {
		System.out.println("Visitor object created!!!");
		this.fname = "vistor";
		this.lname = "nonvalid";
		this.email = "nonvalid";
		this.passwd = "123";
		this.dob = "visitor@gmail.com";
		this.gender = "male";
		this.balance = "0";
		this.title = "visitor";

		System.out.println("empty constructor");
	}

	public User(
			String fname,
			String lname,
			String email,
			String passwd,
			String dob,
			String gender,
			String balance,
			String title) {
		super();
		this.fname = fname;
		this.lname = lname;
		this.email = email;
		this.passwd = passwd;
		this.dob = dob;
		this.gender = gender;
		this.balance = balance;
		this.title = title;
		if (title.equals("manager"))
			this.balance = "XXX";
	}

	public boolean cheackDateIsInValid(String NewDateOfB) {
		// Check if the date of birth is valid
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate currentDate = LocalDate.now();
		LocalDate minValidDate = LocalDate.of(1924, 1, 1); // Minimum valid date
		LocalDate maxValidDate = currentDate; // Maximum valid date

		LocalDate dob;
		try {
			dob = LocalDate.parse(NewDateOfB, formatter);
			if (dob.isBefore(minValidDate) || dob.isAfter(maxValidDate)) {
				// Date of birth is not within the valid range
				return false;
			}
		} catch (DateTimeParseException e) {
			// Date format is invalid
			return false;
		}
		return true;
	}

	public String getUserRole(String input) {
		if (input.contains("@TbuyManager")) {
			this.setBalance("XXX");
			return "manager";
		} else {
			return "client";
		}
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "User [fname=" + fname + ", lname=" + lname + ", email=" + email + ", passwd=" + passwd + ", dob=" + dob
				+ ", gender=" + gender + "]";
	}

}
