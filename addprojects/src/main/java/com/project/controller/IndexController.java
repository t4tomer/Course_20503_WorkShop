package com.project.controller;

// $ this class is used for regisertation/log in of users 
//the code that saves the user in the db after he passed the validation
import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.project.service.UserService;
import com.project.service.CartProductService;

import javax.mail.MessagingException;
import com.project.service.UserService;

import com.project.model.User;
import com.project.repository.UserRepository;
import com.project.service.ImageService;

import javax.servlet.http.HttpSession;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Controller
public class IndexController {

	@Autowired
	private EmailController senderService;// used to send mail to new users

	private String Email; // email of the logging user
	private String NewTempPswd;
	private String NewFirstName;
	private String NewLastName;
	private String NewEmail;
	private String NewPass;
	private String NewDateOfB;
	private String gender;
	private int NumberOfLoginAttempts = 3;
	private String NewBalance;
	User newUser;

	@Autowired
	private UserService userService;

	@Autowired
	private CartProductService cartService;

	@GetMapping("/PrefumePage")
	public String PrefumePage(@RequestParam("FirstName") String name, Model model) {
		model.addAttribute("FirstName", name); // Add name attribute to the model
		return "PrefumePage";
	}

	@GetMapping("/")
	public String index() {
		return "LogIn/RegistrationPage"; // ! orignal line
		// return "Product1/pageTest"; // ! works
		// return "Product1/PrefumePage"; // ! works
		// return "PrefumePage";
		// return "PrefumePage";

	}

	// $ used for testing
	@PostMapping("/page1")
	public String page1Post(String name, RedirectAttributes redirectAttributes) {
		redirectAttributes.addAttribute("FirstName", name); // Add name as a URL parameter
		return "redirect:/page2"; // Redirect to Page 2
	}

	// $ used for testing
	@GetMapping("/page2")
	public String page2(@RequestParam("FirstName") String name, Model model) {
		model.addAttribute("FirstName", name); // Add name attribute to the model
		return "page2";
	}

	// $ when the user registers to the site
	@PostMapping("/register")
	public String userRegistration(@ModelAttribute User user, Model model) {
		System.out.println(user.toString());
		// validate
		NewFirstName = user.getFname();
		NewLastName = user.getLname();
		NewEmail = user.getEmail();
		NewPass = user.getPasswd();
		NewDateOfB = user.getDob();
		gender = user.getGender();
		NewTempPswd = "123";// ! test password
		// tempral password that is sent to email
		// NewTempPswd = senderService.generateRandomString();//! generate random
		// password that is sent to email
		NewBalance = user.getBalance();

		// !Check if the email exists in UserDB
		User existingUser = userService.getUserByEmail(NewEmail);
		if (existingUser != null) {
			model.addAttribute("userExists", true); // Set userExists Failed attribute to
			System.out.println("User is allready registered in userService");
			// TODO to fix the problem why I can not write return"LogIn/RegistrationPage"
			return "LogIn/RegistrationPage";
		}

		// ! Create a new User instance using the first constructor
		newUser = new User(
				NewFirstName,
				NewLastName,
				NewEmail,
				NewPass,
				NewDateOfB,
				gender,
				NewBalance);
		// eRepo.saveAll(Arrays.asList(newUser));

		// ! send verefication mail-by sending NewTempPswd
		/*
		 * try {
		 * senderService.sendSimpleEmail(NewEmail, "authorization code", NewTempPswd);
		 * } catch (MessagingException e) {
		 * System.out.println("Messagin Excpection");
		 * e.printStackTrace();
		 * }
		 */

		return "LogIn/ValidationPage";
		// return "1Registration_page";
	}

	// $ validate new user via the temp password that is sent to the email new user
	@PostMapping("/validate")
	public String validateForm(@RequestParam String email, @RequestParam String authCode, Model model,
			RedirectAttributes redirectAttributes) {
		System.out.println("Email html page: " + email);
		System.out.println("Authorization html page: " + authCode);
		User login = userService.getUserByEmail(email);// user login from the validate page

		if (NewTempPswd.equals(authCode)) {
			model.addAttribute("validationSuccessful", true); // Set validation Successful attribute to true
			System.out.println("\t-->Verification passed successfully");
			// ! Adding newUSer to a SQL db
			userService.addNewUser(newUser);
			Email = email;
			model.addAttribute("email", Email);
			redirectAttributes.addAttribute("FirstName", newUser.getFname()); // show name in the validation page
			redirectAttributes.addAttribute("LastName", newUser.getLname()); // show name in the validation page
			return "redirect:/LogIn/siteMainPage";

		} else {
			model.addAttribute("validationFailedTryAgain", true);
			NumberOfLoginAttempts--;
			model.addAttribute("NumberOfLoginAttempts", NumberOfLoginAttempts); // Add the number of tries remaining to
			if (NumberOfLoginAttempts == 0) {
				System.out.println("\t-->removing newUser object");
				NumberOfLoginAttempts = 3;
				newUser = null;
			}
			// TODO add NumberOfTries-need to add the feature that counts the number of

		}

		return "LogIn/ValidationPage"; // Return the name of the HTML file to be loaded after form submission
	}

	// $ used for testing
	@GetMapping("/pageTest123")
	public String pageTest123(@RequestParam("FirstName") String name, Model model) {
		return "pageTest123";
	}

	@GetMapping("/LogIn/siteMainPage")
	public String ThreeSiteMainPage(@RequestParam("FirstName") String name, Model model) {
		return "LogIn/siteMainPage";
	}

	@GetMapping("/PerfumePage")
	public String PerfumePage(@RequestParam("FirstName") String name, Model model) {
		return "PerfumePage";
	}

	// $ when the user loges in to the server
	@PostMapping("/login")
	public String loginRegistration(@ModelAttribute User user, Model model, RedirectAttributes redirectAttributes) {
		Email = user.getEmail();
		String password = user.getPasswd();
		User login = userService.getUserByEmail(Email);
		if (login != null && login.getEmail().equals(Email) && login.getPasswd().equals(password)) {
			System.out.println("\t-->Login passed successfully");
			model.addAttribute("loginSuccessful", true);
			redirectAttributes.addAttribute("FirstName", login.getFname()); // Add first name as a parameter in the
																			// redirect URL
			redirectAttributes.addAttribute("LastName", login.getLname()); // Add last name as a parameter in the
																			// redirect URL
			return "redirect:/LogIn/siteMainPage";// ! the original line

		} else {
			NumberOfLoginAttempts--;
			System.out.println("\t-->Login failed $$!,number of login attempts: " + NumberOfLoginAttempts);
			model.addAttribute("loginFailed", true);
			model.addAttribute("email", Email);// the value of email in the LogIn/RegistrationPage
			model.addAttribute("NumberOfLoginAttempts", NumberOfLoginAttempts); // Add the number of tries remaining to
																				// the model

			return "LogIn/RegistrationPage";
		}
	}

	@GetMapping("/getEmail")
	@ResponseBody
	public String getEmail() {
		return Email; // Assuming Email is a class-level field in your controller
	}

	@PostMapping("/test123")
	public String test123(@ModelAttribute User user, Model model, RedirectAttributes redirectAttributes) {

		User login = userService.getUserByEmail(Email);

		redirectAttributes.addAttribute("FirstName", login.getFname()); // Add first name as a parameter in the
		// redirect URL
		redirectAttributes.addAttribute("LastName", login.getLname()); // Add last name as a parameter in the
		// redirect URL
		long a = userService.getUserCount();
		System.out.println("\t\t---> user count is :" + a);

		System.out.println(" pressed test123 button ");
		return "redirect:/LogIn/siteMainPage";// ! the original line
	}

}
