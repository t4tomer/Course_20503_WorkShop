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
    private ImageService imageServicePrefumes;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepo; // Inject your UserRepository

    private String currentProductCatagory; // the current category of a product that was just added

    @GetMapping("/ping")
    @ResponseBody
    public String hello_world() {
        return "Hello World!";
    }

    // // display image
    // @GetMapping("/display")
    // public ResponseEntity<byte[]> displayImage(@RequestParam("id") long id)
    // throws IOException, SQLException {
    // // ---> my code
    // // Product newProduct = productService.getLastProductAdded();
    // // System.out.println("\t\t--> display method,last product added is :" +
    // // newProduct.getProductName());
    // // System.out.println("\n id number in display is:" + id);

    // // ---> my code

    // Image image = imageService.viewById(id);
    // byte[] imageBytes = null;
    // imageBytes = image.getImage().getBytes(1, (int) image.getImage().length());
    // return
    // ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
    // }
    @GetMapping("/display")
    @ResponseBody
    public ResponseEntity<byte[]> displayImage(@RequestParam("id") String productCode)
            throws SQLException, IOException {
        Product product = productService.getProductByProductCode(productCode);
        Blob imageBlob = product.getBlobType();
        byte[] imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes); // Adjust media type as needed
    }

    // view All images
    @GetMapping("/allImagesPage")
    public ModelAndView home() {
        // ModelAndView mv = new ModelAndView("index");
        String pagePerfume = "PrefumePage";
        ModelAndView mv;
        List<Image> imageList;

        Product lastProduct = productService.getLastProductAdded();
        String lastProductCategory = lastProduct.getproductCategory();
        System.out.println("\t\t\t  $$$$$$---->the value of currentProductCatagory is :" + currentProductCatagory);
        System.out.println("\t\t--> last Product added (allImagesPage) : " + lastProductCategory);

        if (currentProductCatagory.equals("Vitamins")) {
            List<Product> productVitaminsListByCategory = productService.getAllProductByCatagory("Vitamins");
            List<Image> vitaminsListImages = productService.getAllProductImagesByCatagory("Vitamins");
            // ----> test
            List<Product> list = productService.getAllProductByCatagory("Vitamins");
            // Print the product names
            for (Product product : list) {
                System.out.println("\t\t--> the product code is :" + product.getProductCode());
            }

            // ----> test
            mv = new ModelAndView("VitaminsPage");// load model and view for images in new page called VitaminsPage.html
            mv.addObject("productVitaminsListByCategory", productVitaminsListByCategory);// add vitamins products in the
                                                                                         // VitaminsPage
            return mv;

        }
        if (currentProductCatagory.equals("Perfumes")) {
            List<Product> productPerfumesListByCategory = productService.getAllProductByCatagory("Perfumes");
            List<Image> prefumeList = productService.getAllProductImagesByCatagory("Perfumes");
            mv = new ModelAndView("PrefumePage");// load model and view for images in new page called PerfumesPage.html
            mv.addObject("productPerfumesListByCategory", productPerfumesListByCategory);// add prefumes products in the
                                                                                         // PrefumePage
            return mv;
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
        currentProductCatagory = productCategory;
        byte[] bytes = file.getBytes();
        Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);

        Image image = new Image();
        image.setImage(blob);
        imageService.create(image);
        System.out.println("product catagory:is" + productCategory);
        // if (productCategory.equals("Perfumes")) {
        // System.out.println("\t\t\t----->added image to ServicePrefumes!!!");
        // blob = new javax.sql.rowset.serial.SerialBlob(bytes);
        // Image imagePrefume = new Image();
        // imagePrefume.setImage(blob);
        // imageServicePrefumes.create(imagePrefume);

        // // imageServicePrefumes
        // }

        Product newProduct = new Product(
                productCode,
                productName,
                productPrice,
                productQuantity,
                productCategory,
                image.getDate(),
                image.getImage());

        System.out.println(newProduct.toString());

        // System.out.println(image.toString());
        productService.addNewProduct(newProduct);
        // ---> new code lines
        productService.addNewProduct2(newProduct);

        Image newImage = new Image(image.getId(), image.getDate(), image.getImage());
        // imageRepo.saveAll(Arrays.asList(newImage));
        productRepo.saveAll(Arrays.asList(newProduct));

        return "redirect:/allImagesPage";
    }
}
