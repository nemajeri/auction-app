package com.atlantbh.auctionappbackend.controller;

import com.atlantbh.auctionappbackend.enums.SortBy;
import com.atlantbh.auctionappbackend.exception.UnprocessableCSVFileException;
import com.atlantbh.auctionappbackend.request.NewProductRequest;
import com.atlantbh.auctionappbackend.response.AppUserProductsResponse;
import com.atlantbh.auctionappbackend.response.HighlightedProductResponse;
import com.atlantbh.auctionappbackend.response.ProductsResponse;
import com.atlantbh.auctionappbackend.response.SingleProductResponse;
import com.atlantbh.auctionappbackend.service.ProductService;
import io.swagger.annotations.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import static org.springframework.http.HttpStatus.*;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/products")
@Api("Product Controller")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    @GetMapping("/search-suggestions")
    @ApiOperation(value = "Get search suggestions")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved search suggestions"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<String> searchSuggestions(
            @ApiParam(value = "The search query", required = true)
            @RequestParam("query") String query) {
        String bestSuggestion = productService.getSuggestion(query);
        return ResponseEntity.ok(bestSuggestion);
    }

    @GetMapping("/items")
    @ApiOperation(value = "Get all filtered products", notes = "This endpoint is responsible for both filtering and sorting of the product items")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved filtered products"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<Page<ProductsResponse>> getAllProducts(
            @ApiParam(value = "Number of the page to retrieve", defaultValue = "0")
            @RequestParam(defaultValue = "0") int pageNumber,

            @ApiParam(value = "Size of the page to retrieve", defaultValue = "9")
            @RequestParam(defaultValue = "9") int pageSize,

            @ApiParam(value = "Term to search products by name", defaultValue = "")
            @RequestParam(defaultValue = "") String searchTerm,

            @ApiParam(value = "id of the category to filter products")
            @RequestParam(required = false) Long categoryId,

            @ApiParam(value = "Sorting method for the products", allowableValues = "DEFAULT_SORTING, PRICE_HIGH_TO_LOW, PRICE_LOW_TO_HIGH, END_DATE, START_DATE", defaultValue = "DEFAULT_SORTING")
            @RequestParam(defaultValue = "DEFAULT_SORTING", required = false) String sort) {

        Page<ProductsResponse> productsList = productService.getAllFilteredProducts(pageNumber, pageSize, searchTerm, categoryId, SortBy.fromName(sort));
        return ResponseEntity.ok(productsList);
    }

    @GetMapping("/recommended")
    @ApiOperation(value = "Get recommended products")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved recommended products"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<List<ProductsResponse>> getRecommendedProducts(HttpServletRequest request) {
        List<ProductsResponse> recommendedProducts = productService.getRecommendedProducts(request);
        return new ResponseEntity<>(recommendedProducts, HttpStatus.OK);
    }


    @ApiOperation(value = "Retrieve users products", notes = "Retrieves products associated with a given user. The type of the products to be retrieved is determined by the 'type' parameter, which can be either 'SOLD' for sold products, or any other value for unsold products.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Products successfully retrieved.", response = AppUserProductsResponse.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Bad request if parameters are missing or invalid."),
            @ApiResponse(code = 500, message = "Unexpected error.")
    })
    @GetMapping("/items/app-user")
    public ResponseEntity<List<AppUserProductsResponse>> retrieveProductsFromUser(
            @ApiParam(value = "The id of the user for whom products are to be retrieved", required = true) @RequestParam Long userId,
            @ApiParam(value = "The type of products to retrieve, either 'SOLD' for sold products, or any other value for unsold products.", required = true) @RequestParam String type) {
        SortBy sortBy = SortBy.fromValue(type);
        List<AppUserProductsResponse> products = productService.retrieveUserProductsByType(userId, sortBy);
        return new ResponseEntity<>(products, OK);
    }

    @ApiOperation(value = "Add a new product item", notes = "This endpoint allows a user to add a new product item. The product details and images are provided as form data.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The product was successfully created."),
            @ApiResponse(code = 400, message = "Validation error in the request."),
            @ApiResponse(code = 406, message = "All images should be of a valid format."),
            @ApiResponse(code = 500, message = "Product creation failed due to an internal server error."),
            @ApiResponse(code = 500, message = "Unexpected error.")
    })
    @PostMapping(path = "/add-item", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addNewItem(@RequestPart("productDetails") @Valid NewProductRequest request,
                                             @RequestPart("images") List<MultipartFile> images,
                                             BindingResult bindingResult,
                                             HttpServletRequest httpServletRequest) {
        List<String> allowedContentTypes = List.of("image/jpeg", "image/png", "image/jpg");

        if (!images.stream().allMatch(image -> allowedContentTypes.contains(image.getContentType()))) {
            return ResponseEntity.status(NOT_ACCEPTABLE).body("All images should be of a valid format");
        }

        if (bindingResult.hasErrors()) {
            log.error("Product creation failed with errors: {}", bindingResult.getAllErrors());
            return new ResponseEntity<>(BAD_REQUEST);
        }
        try {
            productService.createProduct(request, images, httpServletRequest);
            return new ResponseEntity<>(CREATED);
        } catch (Exception e) {
            log.error("Product creation failed.");
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Uploads a CSV file", notes = "This endpoint is used to upload a CSV file containing product details. Each line of the CSV file should represent a product attribute. If the CSV file is correctly formatted, new products will be created and stored.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "CSV file successfully processed and products created."),
            @ApiResponse(code = 406, message = "Invalid file format. Only CSV files are accepted."),
            @ApiResponse(code = 422, message = "Unable to process CSV file. Check if the file is correctly formatted."),
            @ApiResponse(code = 500, message = "Unexpected error.")
    })
    @PostMapping("/upload-csv-file")
    public ResponseEntity<String> uploadCSVFile(
            @ApiParam(value = "The CSV file to upload", required = true) @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        String contentType = file.getContentType();
        if (file.isEmpty() || contentType == null || !contentType.equalsIgnoreCase("text/csv")) {
            return ResponseEntity.status(NOT_ACCEPTABLE).body("File not accepted");
        } else {
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                productService.processCsvFileToCreateProducts(reader, request);
                return new ResponseEntity<>(HttpStatus.CREATED);
            } catch (Exception ex) {
                log.error("Error while parsing CSV file: ", ex);
                throw new UnprocessableCSVFileException(ex.getMessage());
            }
        }
    }

    @GetMapping(path = "/{productId}")
    @ApiOperation(value = "Get product by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved product"),
            @ApiResponse(code = 400, message = "Invalid product id"),
            @ApiResponse(code = 404, message = "Product not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<SingleProductResponse> getProductById(
            @ApiParam(value = "The id of the product to retrieve", required = true)
            @PathVariable("productId") Long productId, HttpServletRequest request) {
        return ResponseEntity.ok(productService.getProductById(productId, request));
    }

    @ApiOperation(value = "Retrieves filtered products",
            notes = "This endpoint is used to retrieve a page of products based on a filter. The supported filters are 'new-arrival' and 'last-chance'. The response is a page of ProductsResponse objects.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved a page of products according to the filter"),
            @ApiResponse(code = 400, message = "Invalid filter specified. Supported filters are 'new-arrival' and 'last-chance'"),
            @ApiResponse(code = 500, message = "Unexpected error occurred.")
    })
    @GetMapping("/filtered-products")
    public ResponseEntity<Page<ProductsResponse>> getFilteredProducts(
            @ApiParam(value = "Filter to be applied on the products. Supported values are 'new-arrival' and 'last-chance'", required = true) @RequestParam String filter,
            @ApiParam(value = "Page number for the products", required = false, defaultValue = "0") @RequestParam(defaultValue = "0") int pageNumber,
            @ApiParam(value = "Size of the page", required = false, defaultValue = "8") @RequestParam(defaultValue = "8") int size) {
        Page<ProductsResponse> products;
        switch (filter) {
            case "new-arrival" -> products = productService.getNewProducts(pageNumber, size);
            case "last-chance" -> products = productService.getLastProducts(pageNumber, size);
            default -> {
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/highlighted-products")
    @ApiOperation(value = "Get highlighted products")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved highlighted products"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<List<HighlightedProductResponse>> getHighlightedProducts() {
        List<HighlightedProductResponse> products = productService.getHighlightedProducts();
        return ResponseEntity.ok(products);
    }
}
