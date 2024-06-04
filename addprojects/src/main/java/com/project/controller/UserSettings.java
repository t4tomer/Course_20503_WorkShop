package com.project.controller;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.model.User;
import com.project.service.CartProductService;
import com.project.service.UserService;

public class UserSettings {

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
    public String validateForChangingUserSettings(@RequestParam("email") String email,
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
            NewTempPswd = senderService.generateRandomString();// ! generate random

            System.out.println("THIS IS TEST!");
            //// ! send mail
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
            // NewTempPswd = "123";
            System.out.println("THIS IS TEST!");
            return "LogIn/SettingsPage";
        }

        model.addAttribute("test1", true); // Ensure attribute is set correctly

        return "LogIn/SettingsValidate"; // Ensure this returns the correct view
    }

}
