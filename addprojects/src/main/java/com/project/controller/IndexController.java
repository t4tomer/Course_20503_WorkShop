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
		// NewTempPswd = "123";// ! test password
		// tempral password that is sent to email
		// ! generate random password that is sent to email
		NewTempPswd = senderService.generateRandomString();
		
		NewBalance = user.getBalance();
		NewTitle = user.getUserRole(NewEmail);

		// cheack if birthday is valid date
		if (!user.cheackDateIsInValid(NewDateOfB)) {
			model.addAttribute("invalidDateOfB", true); // Set userExists Failed attribute to
			return "LogIn/RegistrationPage";

		}

		// !Check if the email exists in UserDB
		User existingUser = userService.getUserByEmail(NewEmail);
		if (existingUser != null) {
			model.addAttribute("userExists", true); // Set userExists Failed attribute to
			System.out.println("User is allready registered in userService");
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
			senderService.sendSimpleEmail(NewEmail, "Welcome to T-Buy ", NewTempPswd);
		} catch (MessagingException e) {
			System.out.println("Messagin Excpection");
			e.printStackTrace();
		}

		return "LogIn/ValidationPage";
		// return "1Registration_page";
	}

	@PostMapping("/VistorsOnly")
	public String vistorLogIn(@ModelAttribute User user, Model model, RedirectAttributes redirectAttributes) {
		System.out.println("\t\t-->you pressed the vistor button");
		User visitorUser = new User("Visitor");
		userService.addNewUser(visitorUser);

		System.out.println("\t-->Login passed successfully");
		model.addAttribute("loginSuccessful", true);
		model.addAttribute("FirstName", visitorUser.getFname()); // Add first name as a parameter in the
		// redirect URL
		model.addAttribute("LastName", visitorUser.getLname()); // Add last name as a parameter in the
		model.addAttribute("Balance", visitorUser.getBalance());

		model.addAttribute("Email", visitorUser.getEmail());
		String numOfInCart = "0";
		// products in cart
		model.addAttribute("cartCount", numOfInCart);
		model.addAttribute("Title", "visitor"); // Add fi

		model.addAttribute("Title", visitorUser.getTitle());
		return "LogIn/SiteMainPage";// ! the original lin

	}

	// $ validate new user via the temp password that is sent to the email new user
	@PostMapping("/validate")
	public String validateForm(@ModelAttribute User user, @RequestParam String authCode, Model model,
			RedirectAttributes redirectAttributes) {
		String email = user.getEmail();
		// User newUser = userService.getUserByEmail(email);

		System.out.println("Email html page: " + email);
		System.out.println("Authorization html page: " + authCode);
		// String NewTempPswd = "123";

		if (NewTempPswd.equals(authCode)) {
			model.addAttribute("validationSuccessful", true); // Set validation Successful attribute to true
			System.out.println("\t-->Verification passed successfully");
			// ! Adding newUSer to a SQL db
			userService.addNewUser(newUser);
			Email = email;
			model.addAttribute("loginSuccessful", true);
			model.addAttribute("FirstName", newUser.getFname()); // Add first name as a parameter in the
																	// redirect URL
			model.addAttribute("LastName", newUser.getLname()); // Add last name as a parameter in the
			model.addAttribute("Balance", newUser.getBalance());

			model.addAttribute("Email", newUser.getEmail());
			String numOfInCart = cartService.getNumberOfItemsInCart() + "";// get number of
			System.out.println("the numOfInCart:" + numOfInCart);
			// products in cart
			model.addAttribute("cartCount", numOfInCart);

			model.addAttribute("Title", newUser.getTitle());

			return "LogIn/SiteMainPage";

		} else {
			model.addAttribute("validationFailedTryAgain", true);
			NumberOfLoginAttempts--;
			model.addAttribute("NumberOfLoginAttempts", NumberOfLoginAttempts); // Add the number of tries remaining to
			if (NumberOfLoginAttempts == 0) {
				System.out.println("\t-->removing newUser object");
				NumberOfLoginAttempts = 3;
				newUser = null;
			}

		}

		return "LogIn/ValidationPage"; // Return the name of the HTML file to be loaded after form submission
	}

	// $ used for testing
	@GetMapping("/pageTest123")
	public String pageTest123(@RequestParam("FirstName") String name, Model model) {
		return "pageTest123";
	}

	@GetMapping("/LogIn/SiteMainPage")
	public String ThreeSiteMainPage(@RequestParam("FirstName") String name, @RequestParam("Title") String Title,
			Model model, RedirectAttributes redirectAttributes) {
		return "LogIn/SiteMainPage";
	}

	@GetMapping("/PerfumePage")
	public String PerfumePage(@RequestParam("FirstName") String name, Model model) {
		return "PerfumePage";
	}

	// $ when the user loges in to the server
	@PostMapping("/loginToSite")
	public String loginRegistration(@ModelAttribute User user, Model model, RedirectAttributes redirectAttributes) {
		Email = user.getEmail();
		if (!userService.userExistsInDB(Email)) {
			System.out.println("user does not exists in the db!!!!!:" + Email);
			model.addAttribute("emailIsNotInUsersTable", true);
			model.addAttribute("email", Email);

			return "LogIn/RegistrationPage";

		}
		String password = user.getPasswd();
		User login = userService.getUserByEmail(Email);
		String Title = login.getTitle();
		redirectAttributes.addAttribute("Title", Title); // Add first name as a parameter in the

		if (login != null && login.getEmail().equals(Email) && login.getPasswd().equals(password)) {
			System.out.println("\t-->Login passed successfully");
			model.addAttribute("loginSuccessful", true);
			model.addAttribute("FirstName", login.getFname()); // Add first name as a parameter in the
																// redirect URL
			model.addAttribute("LastName", login.getLname()); // Add last name as a parameter in the
			model.addAttribute("Balance", login.getBalance());

			model.addAttribute("Email", login.getEmail());
			String numOfInCart = cartService.getNumberOfItemsInCart() + "";// get number of
			System.out.println("the numOfInCart:" + numOfInCart);
			// products in cart
			model.addAttribute("cartCount", numOfInCart);
			model.addAttribute("Title", Title); // Add fi

			model.addAttribute("Title", login.getTitle());
			return "LogIn/SiteMainPage";// ! the original line

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

	// $--> methods that are used to change the user settings
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
		model.addAttribute("FirstName", user.getFname()); // show name in the validation page
		model.addAttribute("LastName", user.getLname()); // show name in the validation page
		model.addAttribute("Balance", user.getBalance());
		model.addAttribute("Email", user.getEmail());
		String numOfInCart = cartService.getNumberOfItemsInCart() + ""; // get number of products in cart
		System.out.println("the numOfInCart:" + numOfInCart);
		model.addAttribute("cartCount", numOfInCart);
		model.addAttribute("Title", user.getTitle());

		return "LogIn/siteMainPage"; // Use redirect: prefix to ensure attributes are passed
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
		String title = currentUser.getTitle();

		ModelAndView mv = new ModelAndView(pageName);
		mv.addObject("title", title); // Add user title to the model

		// mv.addObject("Email", inputEmail);// show the balance of the user
		return mv;
	}

	@PostMapping("/validateForChangingUserSettings")
	public String validateForChangingUserSettings(@RequestParam("email") String email,
			RedirectAttributes redirectAttributes, Model model, @ModelAttribute User user) {
		Email = email;

		User currentUser = userService.getUserByEmail(email);
		System.out.println("\t\t --> In Login Settings page");
		System.out.println("\t\t --> the email is :" + Email);

		User validateUser = userService.getUserByEmail(Email);
		String titleUser = validateUser.getTitle();
		String validateEmail = validateUser.getEmail();
		String validatePassWord = (validateUser.getPasswd());

		String inputEmail = user.getEmail();
		String inputPassword = (user.getPasswd());
		redirectAttributes.addAttribute("email", Email); // show name in the validation page
		System.out.println("the title of the user" + currentUser.getTitle());
		redirectAttributes.addAttribute("Title", currentUser.getTitle()); // show name in the validation page

		currentUser = userService.getUserByEmail(Email);

		if (inputEmail.equals(validateEmail) && inputPassword.equals(validatePassWord)) {
			model.addAttribute("validationConfirmed", true);
			redirectAttributes.addAttribute("Title", currentUser.getTitle()); // show name in the validation page

			// if (titleUser.equals("client")) {
			// // ! generate random verfication password that will be sent to email
			// NewTempPswd = senderService.generateRandomString();
			// } else {
			// NewTempPswd = "123";
			// System.out.println("THIS IS MANAGER!");
			// }
			NewTempPswd = "123";
			System.out.println("THIS IS TEST!");
			//// ! send mail with verifaction code
			try {
				String subject = "Verification code -Tbuy";
				String msg = "your verification code is: " + NewTempPswd + "";
				senderService.sendSimpleEmail(inputEmail, subject, msg);
			} catch (MessagingException e) {
				System.out.println("Messaging Exception");
				e.printStackTrace();
			}
			return "LogIn/SettingsValidate2";
		}
		model.addAttribute("cheackValidation", true);

		return "LogIn/SettingsValidate";
	}

	@PostMapping("/validateForChangingUserSettings2")
	public String validateForChangingUserSettings2(
			@RequestParam("email") String email,
			@RequestParam String authCode,
			RedirectAttributes redirectAttributes,
			Model model,
			@ModelAttribute User user) {

		Email = email;

		User currentUser = userService.getUserByEmail(email);
		System.out.println("\t\t --> In Login Settings page");
		System.out.println("\t\t --> the email is :" + Email);
		System.out.println("\t\t--> authCode:" + authCode);
		System.out.println("\t\t--> NewTempPswd:" + NewTempPswd);

		User validateUser = userService.getUserByEmail(Email);
		String validateEmail = validateUser.getEmail();
		String validatePassWord = (validateUser.getPasswd());

		String inputEmail = user.getEmail();
		String inputPassword = (user.getPasswd());

		if (inputEmail.equals(validateEmail) && NewTempPswd.equals(authCode)) {
			model.addAttribute("Title", currentUser.getTitle()); // Add name attribute to the model

			return "LogIn/SettingsPage";
		}

		model.addAttribute("test1", true); // Ensure attribute is set correctly

		return "LogIn/SettingsValidate"; // Ensure this returns the correct view
	}

	@GetMapping("/getEmail")
	@ResponseBody
	public String getEmail() {
		return Email; // Assuming Email is a class-level field in your controller
	}

}
