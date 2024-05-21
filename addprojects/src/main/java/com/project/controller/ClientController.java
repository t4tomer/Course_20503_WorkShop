package com.project.controller;

import com.project.model.Image;
import com.project.model.Product;
import com.project.repository.ProductRepository;
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
    @Autowired
    private ImageService imageService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepo; // Inject your UserRepository

    private String chosenProductCatagory; // the current category of a product that was just added

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

    // --->test

    @GetMapping("/buyProduct/{productCode}")
    public ModelAndView buyProduct(@PathVariable("productCode") String productCode) {
        Product currentProduct = productService.getProductByProductCode(productCode);
        ModelAndView mav = new ModelAndView("ProductDetails"); // This should match the HTML file name
        mav.addObject("currentProduct", currentProduct); // Add the product object to the model
        return mav;
    }

    // method that shows the product of store by catagory
    @GetMapping("/page_prefume123")
    public ModelAndView prefumePage(String chosenProductCatagory) {
        String pageName = "";
        ModelAndView mv;
        if (chosenProductCatagory.equals("Perfumes"))
            pageName = "PrefumePage";
        if (chosenProductCatagory.equals("Vitamins"))
            pageName = "VitaminsPage";
        List<Product> productListByCategory = productService.getAllProductByCatagory(chosenProductCatagory);
        mv = new ModelAndView(pageName);// load model and view for images in new page called .
        mv.addObject("productListByCategory", productListByCategory);// add prefumes products in the

        return mv;

    }

    // view All images
    @GetMapping("/allImagesPage")
    public ModelAndView home() {
        ModelAndView mv;
        List<Image> imageList;
        if (chosenProductCatagory.equals("Vitamins")) {
            return prefumePage(chosenProductCatagory);
            // List<Product> productVitaminsListByCategory =
            // productService.getAllProductByCatagory("Vitamins");
            // mv = new ModelAndView("VitaminsPage");// load model and view for images in
            // new page called VitaminsPage.html
            // mv.addObject("productVitaminsListByCategory",
            // productVitaminsListByCategory);// add vitamins products to the
            // // // Vitamins page //
            // return mv;
        }
        if (chosenProductCatagory.equals("Perfumes")) {
            return prefumePage(chosenProductCatagory);
            // List<Product> productPerfumesListByCategory =
            // productService.getAllProductByCatagory("Perfumes");
            // mv = new ModelAndView("PrefumePage");// load model and view for images in new
            // page called PerfumesPage.html
            // mv.addObject("productPerfumesListByCategory",
            // productPerfumesListByCategory);// add prefumes products in the
            // // // PrefumePage
            // return mv;
        }
        mv = new ModelAndView("index");
        imageList = imageService.viewAll();
        mv.addObject("imageList", imageList);
        return mv;
    }

    // add image - get
    @GetMapping("/add")
    public ModelAndView addImage() {
        return new ModelAndView("addimage");
    }

    // add image - post
    @PostMapping("/add")
    public String addImagePost(HttpServletRequest request,
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

    @PostMapping("/test123")
    public ModelAndView PrintMsg() {
        System.out.println("TEST123");
        return prefumePage("Perfumes");
    }

}
