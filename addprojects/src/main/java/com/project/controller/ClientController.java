package com.project.controller;

import com.project.model.CartProduct;
import com.project.model.Image;
import com.project.model.Product;
import com.project.model.User;
import com.project.repository.ProductRepository;
import com.project.repository.UserRepository;
import com.project.service.ImageService;
import com.project.service.ProductService;
import com.project.service.UserService;
import com.project.service.CartProductService;

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
    private CartProductService cartService; // Inject your UserRepository

    @Autowired
    private UserService userService;

    private String chosenProductCatagory; // the current category of a product that was just added
    // ----------------> used for the clients that want to register/login to the
    // site
    String clientEmail = "test";
    boolean insufficientFunds = false;
    User currentUser = null;
    List<Product> productListByCategory;

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
        ModelAndView mav = new ModelAndView("ProductPages/ProductDetails"); // This should match the HTML file name
        mav.addObject("currentProduct", currentProduct); // Add the product object to the model
        return mav;
    }

    RedirectAttributes redirectAttributes;

    // method that shows the product of store by catagory
    @GetMapping("/allImagesPage")
    public ModelAndView home() {
        String pageName = "";
        String FirstName = currentUser.getFname();
        String Balance = currentUser.getBalance();
        String Title = currentUser.getTitle();
        System.out.println("the title is :" + Title);
        if (chosenProductCatagory.equals("Perfumes"))
            pageName = "Prefumes";
        else if (chosenProductCatagory.equals("Vitamins"))
            pageName = "Vitamins";

        productListByCategory = productService.getAllProductByCatagory(chosenProductCatagory);
        // productListByCategory =
        // productService.removeZeroQunantityProducts(productListByCategory);
        ModelAndView mv = new ModelAndView("ProductPages/AllProductsPage");
        mv.addObject("productListByCategory", productListByCategory);
        mv.addObject("pageName", pageName); // Add pageName to the model
        mv.addObject("FirstName", FirstName); // Add pageName to the model
        mv.addObject("Balance", Balance); // Add pageName to the model
        mv.addObject("Title", Title); // Add Title to the model

        return mv;
    }

    // add image - get
    @GetMapping("/add")
    public ModelAndView AddProduct() {
        String pageName = "ProductPages/AddProduct";
        ModelAndView mv = new ModelAndView(pageName);
        String FirstName = currentUser.getFname();
        String Balance = currentUser.getBalance();
        mv.addObject("FirstName", FirstName); // FirstName paramater in AddProduct.html
        mv.addObject("Balance", Balance); // Balance paramter in AddProduct.html
        mv.addObject("Email", clientEmail);// Email paramater in AddProduct.html
        return mv;
        // return new ModelAndView();
    }

    Product updateProductQuantity(int update, Product currentProduct) {

        String currentQuantityStr = currentProduct.getProductQuantity();
        int currentQuantityInt = Integer.parseInt(currentQuantityStr) - update;// cast String to int
        currentQuantityStr = currentQuantityInt + "";
        currentProduct.setProductQuantity(currentQuantityStr);
        return currentProduct;
    }

    // this method is used to culculate the of single buy blance of the user
    // after he made single prouch
    public void updateBalanceAftereSingleBuy(String currentBalance, String productCode, int quantity) {

        String productPrice = productService.getProductPriceByProductCode(productCode);
        int purchaseAmount = quantity * Integer.parseInt(productPrice);// calculate single but

        int newBalance = Integer.parseInt(currentBalance) - purchaseAmount;
        String newBalanceStr = newBalance + "";
        currentUser.setBalance(newBalanceStr);
        // update the user's balance in the Users_Table database
        userService.addNewUser(currentUser);
    }

    public int convertToInt(String num) {
        return Integer.parseInt(num);
    }

    // $ method that is used to add products to cart
    @PostMapping("/buyProductNow")
    public String buyProductNow(@RequestParam String productCode, @RequestParam int quantity, Model model) {
        System.out.println("Pressed the add to cart");

        Product currentProduct = productService.getProductByProductCode(productCode);
        int quantityCurrentProduct = convertToInt(currentProduct.getProductQuantity());
        // TODO fix the problem when quantity is zero of product
        // productListByCategory =
        // productService.removeZeroQunantityProducts(productListByCategory);

        if (quantityCurrentProduct - quantity >= 0) {
            // ---> buy currentProduct
            // update the quantity of the product in the Product Table
            currentProduct = updateProductQuantity(quantity, currentProduct);

            productService.addNewProduct(currentProduct);
            String currentBalance = currentUser.getBalance();
            updateBalanceAftereSingleBuy(currentBalance, productCode, quantity);
        } else
            model.addAttribute("outOfStockError", true);

        model.addAttribute("currentProduct", currentProduct);

        return "ProductPages/ProductDetails"; // Return the view name for the current page

    }

    // $ method that is used to add products to cart
    @PostMapping("/add2Cart")
    public String add2Cart(@RequestParam String productCode, @RequestParam int quantity, Model model) {
        System.out.println("Pressed the add to cart");

        Product currentProduct = productService.getProductByProductCode(productCode);
        int quantityCurrentProduct = convertToInt(currentProduct.getProductQuantity());
        if (quantityCurrentProduct - quantity >= 0) {
            // ---> add currentProduct to the Cart Table
            CartProduct p1 = new CartProduct(clientEmail, currentProduct, quantity + "");
            cartService.addProductToCart(p1);
        } else
            model.addAttribute("outOfStockError", true);

        // // update the quantity of the product in the Product Table
        // currentProduct = updateProductQuantity(quantity, currentProduct);

        // // update the sql database with the currentPrdouct with the updated quantity
        // productService.addNewProduct(currentProduct);
        model.addAttribute("currentProduct", currentProduct);

        return "ProductPages/ProductDetails"; // Return the view name for the current page
    }

    // this method is used to update the quantity of the products in the user's cart
    @PostMapping("/changeProductQuantityInCart")
    public ModelAndView changeProductQuantityInCart(@RequestParam String productCodeInCart, @RequestParam int quantity,
            Model model) {
        // TODO need to fix the problem os user adding quantity of products more than
        // stock !
        // ---> update the product quantity in the cart of the user
        Product currentProduct = productService.getProductByProductCode(productCodeInCart);
        int quantityCurrentProduct = convertToInt(currentProduct.getProductQuantity());
        System.out.println("quantityCurrentProduct" + quantityCurrentProduct + ",quantity:" + quantity);

        if (quantityCurrentProduct - quantity >= 0) {
            CartProduct p1 = new CartProduct(clientEmail, currentProduct, quantity + "");
            cartService.addProductToCart(p1);

        } else {
            model.addAttribute("outOfStockError", true);
            System.out.println("Error!!!!");
            model.addAttribute("currentProduct", currentProduct);

        }

        if (quantity == 0) {// remove the product if the quantity is zero
            Product currentProductToRemove = productService.getProductByProductCode(productCodeInCart);
            CartProduct pRemove = new CartProduct(clientEmail, currentProductToRemove, quantity + "");
            cartService.removeProductFromCart(pRemove);
        }

        return cart(clientEmail);
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
    public ModelAndView toVitaminsPage(@RequestParam("Email") String email) {
        clientEmail = email;
        currentUser = userService.getUserByEmail(email);
        System.out.println("redirecting to vitamins page");
        chosenProductCatagory = "Vitamins";
        return home();
    }

    // method that rediects to PrefumesPage after pressing the Perfumes Page button
    @PostMapping("/Redirect2PerfumesPage")
    public ModelAndView toPrefumesPage(@RequestParam("Email") String email) {
        clientEmail = email;
        currentUser = userService.getUserByEmail(email);
        System.out.println("rederciting to prefumes page");
        chosenProductCatagory = "Perfumes";
        return home();
    }

    @PostMapping("/RedirectToCart")
    public ModelAndView toCartPage() {
        System.out.println(" \\t\\t--> rederciting to Cart page");
        // chosenProductCatagory = "Perfumes";
        return cart(clientEmail);
    }

    @PostMapping("/RedirectToAddProduct")
    public ModelAndView buttonAddProduct() {
        System.out.println(" \\t\\t--> redercting to Add Product page!!!");
        return cart(clientEmail);
    }

    // Is the financial balance positive?
    // IsFinancialBalancePositive
    public int calculateBlanceBeforeProuch(User currentUser, int purchase_Value) {
        String currentBalanceStr = currentUser.getBalance();
        int currentBalance = Integer.parseInt(currentBalanceStr) - purchase_Value;
        return currentBalance;
    }

    public boolean FinancialBalancePositive(int currentBalance) {
        if (0 <= currentBalance)
            return true;
        return false;
    }

    @PostMapping("/GetToCheackOut")
    public ModelAndView calculateSubTotal() {
        List<CartProduct> productsInCart = cartService.getAllProductsInCartOfUser(clientEmail);
        int subTotal = cartService.getSubTotal(productsInCart);
        System.out.println("\t\t---> the sub total is :" + subTotal);
        User currentCartUser = userService.getUserByEmail(clientEmail);
        int currentBalance = calculateBlanceBeforeProuch(currentCartUser, subTotal);
        // if the balance of the user is positive,update the use'rs balnce after the
        // purchase
        if (FinancialBalancePositive(currentBalance)) {

            currentCartUser.setBalance(currentBalance + "");
            // update the user in the users table
            userService.addNewUser(currentCartUser);

            // update the quantity of the products after user did cheackout
            cartService.updateQuantityInProductTable(productsInCart);

            // delete the products in the cart
            cartService.deleteAll();
            // TODO fix the removing quantity 0 of the products
            // remove prdoucts with 0 quantity from the products list
            // productListByCategory =
            // productService.removeZeroQunantityProducts(productListByCategory);

        }
        return cart(clientEmail);
    }

    // getProductQuantityAdded
    // method that shows the product of store by catagory
    @GetMapping("/allPrdocutInCart")
    public ModelAndView cart(@RequestParam("Email") String Email) {
        clientEmail = Email;
        String pageName = "cart";
        User currentCartUser = userService.getUserByEmail(clientEmail);

        System.out.println("\t\t--> PAGE CART!!!");
        // List<CartProduct> cartProductsList = cartService.viewAll();// orginal code
        List<CartProduct> cartProductsList = cartService.getAllProductsInCartOfUser(clientEmail);

        String clientName = userService.getUserFirstNameByEmail(clientEmail);
        String userBalance = currentCartUser.getBalance();
        int subTotal = cartService.getSubTotal(cartProductsList);

        ModelAndView mv = new ModelAndView("ProductPages/CartPage");
        mv.addObject("cartProductsList", cartProductsList);
        mv.addObject("pageName", pageName); // Add pageName to the model
        mv.addObject("clientName", "hi " + clientName); // Add pageName to the model
        mv.addObject("subTotal", subTotal);// show the subtotal of the cart
        mv.addObject("userBalance", userBalance);// show the balance of the user
        return mv;
    }

}
