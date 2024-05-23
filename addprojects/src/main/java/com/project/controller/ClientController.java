package com.project.controller;

import com.project.model.Image;
import com.project.model.Product;
import com.project.model.User;
import com.project.repository.ProductRepository;
import com.project.repository.UserRepository;
import com.project.service.ImageService;
import com.project.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

@Controller
public class ClientController {

    // ---> used for the products
    @Autowired
    private ImageService imageService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepo; // Inject your UserRepository

    private String chosenProductCatagory; // the current category of a product that was just added
    // ----------------> used for the clients that want to register/login to the
    // site
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
    User newUser;

    @GetMapping("/ping")
    @ResponseBody
    public String hello_world() {
        return "Hello World!";
    }

    // display image
    @GetMapping("/display")
    @ResponseBody
    public ResponseEntity<byte[]> displayImage(@RequestParam("id") String productCode)
            throws SQLException, IOException {
        Product product = productService.getProductByProductCode(productCode);
        Blob imageBlob = product.getBlobType();
        byte[] imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes); // Adjust media type as needed
    }

    // method that used to redirect to the product details
    @GetMapping("/buyProduct/{productCode}")
    public ModelAndView buyProduct(@PathVariable("productCode") String productCode) {
        Product currentProduct = productService.getProductByProductCode(productCode);
        ModelAndView mav = new ModelAndView("ProductDetails"); // This should match the HTML file name
        mav.addObject("currentProduct", currentProduct); // Add the product object to the model
        return mav;
    }

    RedirectAttributes redirectAttributes;

    // method that shows the product of store by catagory
    @GetMapping("/allImagesPage")
    public ModelAndView home() {
        String pageName = "";
        if (chosenProductCatagory.equals("Perfumes"))
            pageName = "Prefumes";
        else if (chosenProductCatagory.equals("Vitamins"))
            pageName = "Vitamins";

        List<Product> productListByCategory = productService.getAllProductByCatagory(chosenProductCatagory);
        ModelAndView mv = new ModelAndView("AllPrdouctsPage");
        mv.addObject("productListByCategory", productListByCategory);
        mv.addObject("pageName", pageName); // Add pageName to the model

        return mv;
    }

    // add image - get
    @GetMapping("/add")
    public ModelAndView AddProduct() {
        return new ModelAndView("AddProduct");
    }

    Product updateProductQuantity(int update, Product currentProduct) {

        String currentQuantityStr = currentProduct.getProductQuantity();
        int currentQuantityInt = Integer.parseInt(currentQuantityStr) - update;// cast String to int
        currentQuantityStr = currentQuantityInt + "";
        currentProduct.setProductQuantity(currentQuantityStr);
        return currentProduct;
    }

    @PostMapping("/add2Cart")
    public String add2Cart(@RequestParam String productCode, @RequestParam int quantity, Model model) {
        System.out.println("Pressed the add to cart");

        // Retrieve the current product again to add to the model after the update of
        // the quantity.
        Product currentProduct = productService.getProductByProductCode(productCode);

        // update the quantity of the product
        currentProduct = updateProductQuantity(quantity, currentProduct);

        // update the sql database with the currentPrdouct with the updated quantity
        productService.addNewProduct(currentProduct);
        model.addAttribute("currentProduct", currentProduct);

        return "ProductDetails"; // Return the view name for the current page
    }

    // add image - post
    @PostMapping("/add")
    public String AddProductPost(HttpServletRequest request,
            Model model,
            @RequestParam("image") MultipartFile file,
            @RequestParam("productCode") String productCode,
            @RequestParam("productName") String productName,
            @RequestParam("productPrice") String productPrice,
            @RequestParam("productQuantity") String productQuantity,
            @RequestParam("productCategory") String productCategory)
            throws IOException, SerialException, SQLException {

        // cheack if the input text box is empty
        boolean blankTextbox = productCode.isEmpty() || productName.isEmpty() ||
                productPrice.isEmpty() || productCategory.isEmpty() ||
                productQuantity.isEmpty() || productQuantity.isEmpty();

        if (blankTextbox) {
            model.addAttribute("blankParamater", true);
            System.out.println("blank textbox");
            return "redirect:/add";
        }
        chosenProductCatagory = productCategory;
        byte[] bytes = file.getBytes();
        Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);

        Image image = new Image();
        image.setImage(blob);
        imageService.create(image);

        Product newProduct = new Product(
                productCode,
                productName,
                productPrice,
                productQuantity,
                productCategory,
                image.getDate(),
                image.getImage());

        // System.out.println(newProduct.toString());

        // System.out.println(image.toString());
        productService.addNewProduct(newProduct);

        return "redirect:/allImagesPage";
    }

    // method that rediects to VitaminsPage after pressing the Perfumes Page button
    @PostMapping("/Redirect2VitaminsPage")
    public ModelAndView toVitaminsPage() {
        System.out.println("rederciting to vitamins page");
        chosenProductCatagory = "Vitamins";
        return home();
    }

    // method that rediects to PrefumesPage after pressing the Perfumes Page button
    @PostMapping("/Redirect2PerfumesPage")
    public ModelAndView toPrefumesPage() {
        System.out.println("rederciting to prefumes page");
        chosenProductCatagory = "Perfumes";
        return home();
    }

}
