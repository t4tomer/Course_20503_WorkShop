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
	private String balance;// amount of meony in the user account
	private String title;// the title of the user is it manager or client

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
