package com.project.model;

// import jakarta.persistence.email;
import java.util.Random;
import javax.persistence.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.data.annotation.CreatedDate;

@Entity
// create table called Product_Table in the Project_Data_Base
@Table(name = "Users_Table")

public class User {

	private String fname;
	private String lname;
	@Id // make email the primery key
	private String email;
	private String passwd;
	private String dob;
	private String gender;
	private String temp_passwd;// tempral password that server sends to the login email for user authentication
	private String balance;// amount of meony in the user account

	// private
	public User() {
		System.out.println("empty constructor");
	}

	public User(
			String fname,
			String lname,
			String email,
			String passwd,
			String dob,
			String gender,
			String temp_passwd,
			String balance) {
		super();
		this.fname = fname;
		this.lname = lname;
		this.email = email;
		this.passwd = passwd;
		this.dob = dob;
		this.gender = gender;
		this.temp_passwd = temp_passwd;
		// ! testing
		Random random = new Random();
		// Generate a random number between 0 and 100 (inclusive)
		int randomNumber = random.nextInt(101); // Generates a random integer between 0 (inclusive) and 101 (exclusive)
		System.out.println(" new entity created!" + randomNumber);
		this.balance = balance;

	}

	// get the current date and time
	public String getTime() {
		// Get the current local date and time
		LocalDateTime now = LocalDateTime.now();

		// Define date and time formats
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

		// Format date and time as strings
		String formattedDate = now.format(dateFormat);
		String formattedTime = now.format(timeFormat);

		// Print formatted date and time
		System.out.println("Formatted Date: " + formattedDate);
		System.out.println("Formatted Time: " + formattedTime);

		// Optionally, you can also save them into strings
		String dateString = formattedDate;
		String timeString = formattedTime;
		String registrationTime = dateString + "," + timeString;
		// Print saved strings
		System.out.println("Date String: " + dateString);
		System.out.println("Time String: " + timeString);
		System.out.println("registrationTime:" + registrationTime);
		return registrationTime;

	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
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

	public String getTemp_passwd() {
		return temp_passwd;
	}

	public void setTemp_passwd(String temp_passwd) {
		this.temp_passwd = temp_passwd;
	}

	@Override
	public String toString() {
		return "User [fname=" + fname + ", lname=" + lname + ", email=" + email + ", passwd=" + passwd + ", dob=" + dob
				+ ", gender=" + gender + "]";
	}

}
