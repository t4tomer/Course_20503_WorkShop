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
	private String NewTitle;
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
		NewTitle = user.getUserRole(NewEmail);

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
				NewBalance, NewTitle);
		// eRepo.saveAll(Arrays.asList(newUser));

		// ! send verefication mail-by sending NewTempPswd

		try {
			senderService.sendSimpleEmail(NewEmail, "authorization code", NewTempPswd);
		} catch (MessagingException e) {
			System.out.println("Messagin Excpection");
			e.printStackTrace();
		}

		return "LogIn/ValidationPage";
		// return "1Registration_page";
	}

	@PostMapping("/changeUserSettings")
	public String changeUserSettings(
			@RequestParam(required = false) String updateFname,
			@RequestParam(required = false) String fname,
			@RequestParam(required = false) String updateLname,
			@RequestParam(required = false) String lname,
			@RequestParam(required = false) String updateEmail,
			@RequestParam(required = false) String email,
			@RequestParam(required = false) String updatePasswd,
			@RequestParam(required = false) String passwd,
			@RequestParam(required = false) String updateBalance,
			@RequestParam(required = false) String balance,
			RedirectAttributes redirectAttributes,
			Model model) {

		User user = userService.getUserByEmail(Email); // Assume a User object to hold the data

		if (updateFname != null) {
			user.setFname(fname);
		}
		if (updateLname != null) {
			user.setLname(lname);
		}
		if (updatePasswd != null) {
			user.setPasswd(passwd);
		}
		if (updateBalance != null) {
			System.out.println("the new balance is " + balance);
			user.setBalance(balance);
		}

		System.out.println("the new balance is " + user.getBalance());
		// Save the user or perform further processing
		userService.addNewUser(user);
		Email = email;
		redirectAttributes.addAttribute("FirstName", user.getFname()); // show name in the validation page
		redirectAttributes.addAttribute("LastName", user.getLname()); // show name in the validation page
		redirectAttributes.addAttribute("Balance", user.getBalance());
		redirectAttributes.addAttribute("Email", user.getEmail());
		String numOfInCart = cartService.getNumberOfItemsInCart() + ""; // get number of products in cart
		System.out.println("the numOfInCart:" + numOfInCart);
		redirectAttributes.addAttribute("cartCount", numOfInCart);
		redirectAttributes.addAttribute("Title", user.getTitle());

		return "redirect:/LogIn/siteMainPage"; // Use redirect: prefix to ensure attributes are passed
	}

	@PostMapping("/RedirectToSettings")
	public ModelAndView toSettingsPage(@RequestParam("Email") String email) {
		Email = email;
		User currentUser = userService.getUserByEmail(email);
		System.out.println("\t\t --> In Settigs page");
		User validateUser = userService.getUserByEmail(Email);
		String validateEmail = validateUser.getEmail();
		int validatePassWord = Integer.parseInt(validateUser.getPasswd());

		String inputEmail = currentUser.getEmail();
		int inputPassword = Integer.parseInt(currentUser.getPasswd());
		String pageName = "LogIn/SettingsValidate";

		ModelAndView mv = new ModelAndView(pageName);
		mv.addObject("Email", inputEmail);// show the balance of the user
		return mv;
	}

	@PostMapping("/validateForChangingUserSettings")
	public String validateForChangingUserSettings(@RequestParam("email") String email, @RequestParam String authCode,
			RedirectAttributes redirectAttributes, Model model, @ModelAttribute User user) {
		Email = email;

		User currentUser = userService.getUserByEmail(email);
		System.out.println("\t\t --> In Login Settings page");
		System.out.println("\t\t --> the email is :" + Email);

		User validateUser = userService.getUserByEmail(Email);
		String validateEmail = validateUser.getEmail();
		String validatePassWord = (validateUser.getPasswd());

		String inputEmail = user.getEmail();
		String inputPassword = (user.getPasswd());
		redirectAttributes.addAttribute("email", Email); // show name in the validation page
		currentUser = userService.getUserByEmail(Email);

		if (inputEmail.equals(validateEmail) && inputPassword.equals(validatePassWord)) {
			model.addAttribute("validationConfirmed", true);
			NewTempPswd = "123";
			// NewTempPswd = senderService.generateRandomString();// ! generate random

			System.out.println("THIS IS TEST!");
			//// ! send mail
			// try {
			// senderService.sendSimpleEmail(inputEmail, "authorization code", NewTempPswd);
			// } catch (MessagingException e) {
			// System.out.println("Messaging Exception");
			// e.printStackTrace();
			// }
			return "LogIn/SettingsValidate2";
		}
		model.addAttribute("cheackValidation", true);

		return "LogIn/SettingsValidate";
	}

	@PostMapping("/validateForChangingUserSettings2")
	public String validateForChangingUserSettings2(@RequestParam("email") String email, @RequestParam String authCode,
			RedirectAttributes redirectAttributes, Model model, @ModelAttribute User user) {
		Email = email;

		User currentUser = userService.getUserByEmail(email);
		System.out.println("\t\t --> In Login Settings page");
		System.out.println("\t\t --> the email is :" + Email);

		User validateUser = userService.getUserByEmail(Email);
		String validateEmail = validateUser.getEmail();
		String validatePassWord = (validateUser.getPasswd());

		String inputEmail = user.getEmail();
		String inputPassword = (user.getPasswd());
		redirectAttributes.addAttribute("email", Email); // show name in the validation page
		currentUser = userService.getUserByEmail(Email);

		if (inputEmail.equals(validateEmail) && NewTempPswd.equals(authCode)) {
			NewTempPswd = "123";
			System.out.println("THIS IS TEST!");
			return "LogIn/SettingsPage";
		}
		model.addAttribute("cheackValidation2", true);

		return "LogIn/SettingsValidate";
	}

	// $ validate new user via the temp password that is sent to the email new user
	@PostMapping("/validate")
	public String validateForm(@RequestParam String email, @RequestParam String authCode, Model model,
			RedirectAttributes redirectAttributes) {
		System.out.println("Email html page: " + email);
		System.out.println("Authorization html page: " + authCode);
		// String NewTempPswd = "123";
		if (NewTempPswd.equals(authCode)) {
			model.addAttribute("validationSuccessful", true); // Set validation Successful attribute to true
			System.out.println("\t-->Verification passed successfully");
			// ! Adding newUSer to a SQL db
			userService.addNewUser(newUser);
			Email = email;
			model.addAttribute("email", Email);
			redirectAttributes.addAttribute("FirstName", newUser.getFname()); // show name in the validation page
			redirectAttributes.addAttribute("LastName", newUser.getLname()); // show name in the validation page
			redirectAttributes.addAttribute("Balance", newUser.getBalance());
			redirectAttributes.addAttribute("Email", newUser.getEmail());
			String numOfInCart = cartService.getNumberOfItemsInCart() + "";// get number of
			System.out.println("the numOfInCart:" + numOfInCart);
			// products in cart
			redirectAttributes.addAttribute("cartCount", numOfInCart);
			redirectAttributes.addAttribute("Title", newUser.getTitle());

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
	public String ThreeSiteMainPage(@RequestParam("FirstName") String name, @RequestParam("Title") String Title,
			Model model, RedirectAttributes redirectAttributes) {
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
		String Title = login.getTitle();
		redirectAttributes.addAttribute("Title", Title); // Add first name as a parameter in the

		if (login != null && login.getEmail().equals(Email) && login.getPasswd().equals(password)) {
			System.out.println("\t-->Login passed successfully");
			model.addAttribute("loginSuccessful", true);
			redirectAttributes.addAttribute("FirstName", login.getFname()); // Add first name as a parameter in the
																			// redirect URL
			redirectAttributes.addAttribute("LastName", login.getLname()); // Add last name as a parameter in the
			redirectAttributes.addAttribute("Balance", login.getBalance());

			redirectAttributes.addAttribute("Email", login.getEmail());
			String numOfInCart = cartService.getNumberOfItemsInCart() + "";// get number of
			System.out.println("the numOfInCart:" + numOfInCart);
			// products in cart
			redirectAttributes.addAttribute("cartCount", numOfInCart);
			redirectAttributes.addAttribute("Title", Title); // Add first name as a parameter in the
			model.addAttribute("Title", Title);
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

}
