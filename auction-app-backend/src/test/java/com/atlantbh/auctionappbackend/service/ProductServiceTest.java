package com.atlantbh.auctionappbackend.service;

import com.atlantbh.auctionappbackend.enums.SortBy;
import com.atlantbh.auctionappbackend.exception.ProductNotFoundException;
import com.atlantbh.auctionappbackend.model.*;
import com.atlantbh.auctionappbackend.repository.*;
import com.atlantbh.auctionappbackend.request.NewProductRequest;
import com.atlantbh.auctionappbackend.dto.ProductCsvImport;
import com.atlantbh.auctionappbackend.dto.UserMaxBidRecord;
import com.atlantbh.auctionappbackend.response.AppUserProductsResponse;
import com.atlantbh.auctionappbackend.response.ProductsResponse;
import com.atlantbh.auctionappbackend.response.SingleProductResponse;
import com.atlantbh.auctionappbackend.utils.CsvToBeanParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.Reader;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.atlantbh.auctionappbackend.utils.Constants.S3_KEY_PREFIX;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Captor
    ArgumentCaptor<Product> productCaptor;
    @Captor
    ArgumentCaptor<String> stringCaptor;
    @Captor
    ArgumentCaptor<MultipartFile> fileCaptor;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private BidRepository bidRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private SubcategoryRepository subcategoryRepository;
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private S3Service s3Service;
    @Mock
    private TokenService tokenService;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private CsvToBeanParser productCsvParser;
    @Mock
    private HttpServletRequest httpServletRequest;
    @InjectMocks
    private ProductService underTest;

    @Test
    @DisplayName("Test should return filtered products")
    void testGetAllFilteredProducts_ShouldReturnFilteredProducts() {
        List<Product> products = new ArrayList<>();
        Category category1 = new Category(2L, "Shoes");
        Subcategory subcategory1 = new Subcategory(3L, "Sport Shoes", 4, category1);
        AppUser appUser = new AppUser();

        products.add(
                Product.builder().id(1L)
                        .productName("Shoes Collection")
                        .description("New shoes collection")
                        .startPrice(59.99f)
                        .images(Collections.singletonList(
                                Image.builder()
                                        .id(1L)
                                        .imageUrl("./images/shoe-1.jpg")
                                        .build()
                        ))
                        .startDate(ZonedDateTime.now(ZoneId.of("Europe/Paris")))
                        .endDate(ZonedDateTime.now(ZoneId.of("Europe/Paris")).plusDays(10))
                        .isHighlighted(false)
                        .category(category1)
                        .subcategory(subcategory1)
                        .user(appUser)
                        .info(new ShippingInfo("123 Paris Street", "Paris", "75000", "France", "+1134567890"))
                        .sold(false)
                        .build()
        );

        products.add(
                Product.builder()
                        .id(2L)
                        .productName("Shoes Collection")
                        .description("New shoes collection")
                        .startPrice(59.99f)
                        .images(Collections.singletonList(
                                Image.builder()
                                        .id(2L)
                                        .imageUrl("./images/shoe-1.jpg")
                                        .build()
                        ))
                        .startDate(ZonedDateTime.now(ZoneId.of("Europe/Paris")))
                        .endDate(ZonedDateTime.now(ZoneId.of("Europe/Paris")).plusDays(10))
                        .isHighlighted(false)
                        .category(category1)
                        .subcategory(subcategory1)
                        .user(appUser)
                        .info(new ShippingInfo("456 Paris Street", "Paris", "75001", "France", "+1187654321"))
                        .sold(false)
                        .build()
        );
        int pageNumber = 0;
        int pageSize = 9;
        String searchTerm = "";
        Long categoryId = 2L;
        SortBy sortBy = SortBy.DEFAULT_SORTING;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.unsorted());

        when(productRepository.findAll(ArgumentMatchers.<Specification<Product>>any(), ArgumentMatchers.<Pageable>any())).thenReturn(new PageImpl<>(products, pageable, 1));

        List<ProductsResponse> expectedProductsResponses = new ArrayList<>();
        expectedProductsResponses.add(new ProductsResponse(1L, "Shoes Collection", 59.99f, "./images/shoe-1.jpg", category1.getId()));
        expectedProductsResponses.add(new ProductsResponse(2L, "Shoes Collection", 59.99f, "./images/shoe-1.jpg", category1.getId()));

        Page<ProductsResponse> expectedProductsResponsePage = new PageImpl<>(expectedProductsResponses, pageable, 1);

        Page<ProductsResponse> actualProductsResponsePage = underTest.getAllFilteredProducts(pageNumber, pageSize, searchTerm, categoryId, sortBy);

        assertEquals(expectedProductsResponsePage.getContent(), actualProductsResponsePage.getContent());
    }

    @Test
    @DisplayName("Test should return sold products for user")
    void testProductRetrievalForUserByType_ShouldReturnSoldProductsRelatedToUser() {
        Long userId = 1L;

        Category category1 = new Category(2L, "Shoes");
        AppUser appUser = new AppUser();
        appUser.setId(userId);
        Product product1 = Product.builder()
                .id(1L)
                .productName("Shoes Collection")
                .description("New shoes collection")
                .startPrice(59.99f)
                .images(Collections.singletonList(
                        Image.builder()
                                .id(1L)
                                .imageUrl("./images/shoe-1.jpg")
                                .build()
                ))
                .startDate(ZonedDateTime.now(ZoneOffset.UTC))
                .endDate(ZonedDateTime.now(ZoneOffset.UTC).plusDays(10))
                .isHighlighted(false)
                .category(category1)
                .user(appUser)
                .info(new ShippingInfo("123 Paris Street", "Paris", "75000", "France", "+1134567890"))
                .sold(false)
                .build();

        Product product2 = Product.builder()
                .id(1L)
                .productName("Shoes Collection")
                .description("New shoes collection")
                .startPrice(69.99f)
                .images(Collections.singletonList(
                        Image.builder()
                                .id(1L)
                                .imageUrl("./images/shoe-1.jpg")
                                .build()
                ))
                .startDate(ZonedDateTime.now(ZoneOffset.UTC))
                .endDate(ZonedDateTime.now(ZoneOffset.UTC).plusDays(10))
                .isHighlighted(false)
                .category(category1)
                .user(appUser)
                .info(new ShippingInfo("123 Tierra del Fuego", "Ushuaia", "86000", "Argentina", "+1756195793"))
                .sold(false)
                .build();
        SortBy sort = SortBy.SOLD;

        when(productRepository.findAllByUserIdAndEndDateBeforeAndSoldIsTrue(eq(userId), any(ZonedDateTime.class), eq(Sort.by(Sort.Direction.DESC, SortBy.END_DATE.getSort())))).thenReturn(List.of(product1, product2));

        List<AppUserProductsResponse> actualProducts = underTest.retrieveUserProductsByType(userId, sort);

        List<AppUserProductsResponse> expectedProducts = List.of(
                new AppUserProductsResponse(product1.getId(), product1.getProductName(), product1.getStartPrice(), product1.getImages().get(0).getImageUrl(), product1.getEndDate(), product1.getNumberOfBids(), product1.getHighestBid()),
                new AppUserProductsResponse(product2.getId(), product2.getProductName(), product2.getStartPrice(), product2.getImages().get(0).getImageUrl(), product2.getEndDate(), product2.getNumberOfBids(), product2.getHighestBid())
        );

        assertEquals(expectedProducts, actualProducts);

        verify(productRepository, times(1)).findAllByUserIdAndEndDateBeforeAndSoldIsTrue(eq(userId), any(ZonedDateTime.class), eq(Sort.by(Sort.Direction.DESC, SortBy.END_DATE.getSort())));
        verify(productRepository, never()).findAllByUserIdAndEndDateAfterAndSoldIsFalse(anyLong(), any(ZonedDateTime.class), any(Sort.class));
    }

    @Test
    @DisplayName("Test should return products that are not sold for user")
    void testProductRetrievalForUserByType_ShouldReturnProductsThatAreNotSoldButAreRelatedToUser() {
        Long userId = 1L;

        Category category1 = new Category(2L, "Shoes");
        AppUser appUser = new AppUser();
        Product product1 = Product.builder()
                .id(1L)
                .productName("Shoes Collection")
                .description("New shoes collection")
                .startPrice(59.99f)
                .images(Collections.singletonList(
                        Image.builder()
                                .id(1L)
                                .imageUrl("./images/shoe-1.jpg")
                                .build()
                ))
                .startDate(ZonedDateTime.now(ZoneOffset.UTC))
                .endDate(ZonedDateTime.now(ZoneOffset.UTC).plusDays(10))
                .isHighlighted(false)
                .category(category1)
                .user(appUser)
                .info(new ShippingInfo("123 Paris Street", "Paris", "75000", "France", "+1134567890"))
                .sold(false)
                .build();

        Product product2 = Product.builder()
                .id(1L)
                .productName("Shoes Collection")
                .description("New shoes collection")
                .startPrice(69.99f)
                .images(Collections.singletonList(
                        Image.builder()
                                .id(1L)
                                .imageUrl("./images/shoe-1.jpg")
                                .build()
                ))
                .startDate(ZonedDateTime.now(ZoneOffset.UTC))
                .endDate(ZonedDateTime.now(ZoneOffset.UTC).plusDays(10))
                .isHighlighted(false)
                .category(category1)
                .user(appUser)
                .info(new ShippingInfo("123 Tierra del Fuego", "Ushuaia", "86000", "Argentina", "+1756195793"))
                .sold(false)
                .build();
        SortBy sort = SortBy.ACTIVE;

        when(productRepository.findAllByUserIdAndEndDateAfterAndSoldIsFalse(eq(userId), any(ZonedDateTime.class), eq(Sort.by(Sort.Direction.DESC, SortBy.START_DATE.getSort())))).thenReturn(List.of(product1, product2));

        List<AppUserProductsResponse> actualProducts = underTest.retrieveUserProductsByType(userId, sort);

        List<AppUserProductsResponse> expectedProducts = List.of(
                new AppUserProductsResponse(product1.getId(), product1.getProductName(), product1.getStartPrice(), product1.getImages().get(0).getImageUrl(), product1.getEndDate(), product1.getNumberOfBids(), product1.getHighestBid()),
                new AppUserProductsResponse(product2.getId(), product2.getProductName(), product2.getStartPrice(), product2.getImages().get(0).getImageUrl(), product2.getEndDate(), product2.getNumberOfBids(), product2.getHighestBid())
        );

        assertEquals(expectedProducts, actualProducts);

        verify(productRepository, times(1)).findAllByUserIdAndEndDateAfterAndSoldIsFalse(eq(userId), any(ZonedDateTime.class), eq(Sort.by(Sort.Direction.DESC, SortBy.START_DATE.getSort())));
        verify(productRepository, never()).findAllByUserIdAndEndDateBeforeAndSoldIsTrue(anyLong(), any(ZonedDateTime.class), any(Sort.class));
    }


    @Test
    @DisplayName("Test should return recommended products for a user")
    void testGetRecommendedProducts_ReturnsAllRecommendedProducts() {

        Long userId = 1L;

        Category category1 = new Category(2L, "Shoes");
        AppUser appUser = new AppUser();
        appUser.setId(userId);

        Product product1 = Product.builder()
                .id(1L)
                .productName("Shoes Collection")
                .description("New shoes collection")
                .startPrice(59.99f)
                .images(Collections.singletonList(
                        Image.builder()
                                .id(1L)
                                .imageUrl("./images/shoe-1.jpg")
                                .build()
                ))
                .startDate(ZonedDateTime.now(ZoneOffset.UTC))
                .endDate(ZonedDateTime.now(ZoneOffset.UTC).plusDays(10))
                .isHighlighted(false)
                .category(category1)
                .user(appUser)
                .info(new ShippingInfo("123 Paris Street", "Paris", "75000", "France", "+1134567890"))
                .sold(false)
                .build();
        when(tokenService.getAuthenticatedUser(httpServletRequest)).thenReturn(appUser);
        when(productRepository.findRecommendedProducts(userId, PageRequest.of(0, 3))).thenReturn(new PageImpl<>(List.of(product1), PageRequest.of(0, 3), 1));

        List<ProductsResponse> actualProducts = underTest.getRecommendedProducts(httpServletRequest);

        List<ProductsResponse> expectedProducts = List.of(new ProductsResponse(product1.getId(), product1.getProductName(), product1.getStartPrice(), product1.getImages().get(0).getImageUrl(), product1.getCategory().getId()));

        assertEquals(expectedProducts, actualProducts);

        verify(productRepository, times(1)).findRecommendedProducts(userId, PageRequest.of(0, 3));
        verify(productRepository, never()).findFirstActiveProducts(PageRequest.of(0, 3));
    }


    @Test
    @DisplayName("Test should return a product with the given Id and User Id")
    void testGetProductById_ReturnsProduct() throws ProductNotFoundException {
        Long id = 1L;
        Long userId = 2L;

        AppUser appUser = new AppUser();
        appUser.setId(userId);
        appUser.setEmail("testuser@gmail.com");

        Product product = new Product();
        product.setId(id);
        product.setProductName("Shoes Collection");
        product.setDescription("New shoes collection");
        product.setStartPrice(10.00f);
        product.setImages(Collections.singletonList(new Image(id, "/images/shoe-4.jpg", product)));
        product.setStartDate(ZonedDateTime.of(LocalDateTime.of(2023, 3, 23, 0, 0), ZoneId.of("Europe/Paris")));
        product.setEndDate(ZonedDateTime.of(LocalDateTime.of(2023, 3, 23, 0, 0), ZoneId.of("Europe/Paris")));
        product.setNumberOfBids(5);
        product.setHighestBid(25.00f);
        product.setUser(appUser);

        when(productRepository.findById(eq(id))).thenReturn(Optional.of(product));
        when(tokenService.getAuthenticatedUser(any(HttpServletRequest.class))).thenReturn(appUser);
        when(bidRepository.findHighestBidAndUserByProduct(any(Long.class), any(PageRequest.class)))
                .thenReturn(Collections.emptyList());

        SingleProductResponse actualProduct = underTest.getProductById(id, httpServletRequest);

        SingleProductResponse expectedProduct = SingleProductResponse.builder()
                .id(id)
                .productName("Shoes Collection")
                .description("New shoes collection")
                .startPrice(10.00f)
                .images(Collections.singletonList("/images/shoe-4.jpg"))
                .startDate(ZonedDateTime.of(LocalDateTime.of(2023, 3, 23, 0, 0), ZoneId.of("Europe/Paris")))
                .endDate(ZonedDateTime.of(LocalDateTime.of(2023, 3, 23, 0, 0), ZoneId.of("Europe/Paris")))
                .numberOfBids(5)
                .highestBid(25.00f)
                .userId(userId)
                .isOwner(true)
                .isUserHighestBidder(false)
                .build();

        assertEquals(expectedProduct, actualProduct);
    }

    @Test
    @DisplayName("Test should return a product with the given Id for user who is not authenticated")
    void testGetProductByIdForUnauthenticatedUser_ReturnsProduct() throws ProductNotFoundException {
        Long id = 1L;
        Long userId = 2L;

        AppUser owner = new AppUser();
        owner.setId(userId);
        owner.setEmail("testuser@gmail.com");

        Product product = new Product();
        product.setId(id);
        product.setProductName("Shoes Collection");
        product.setDescription("New shoes collection");
        product.setStartPrice(10.00f);
        product.setImages(Collections.singletonList(new Image(id, "/images/shoe-4.jpg", product)));
        product.setStartDate(ZonedDateTime.of(LocalDateTime.of(2023, 3, 23, 0, 0), ZoneId.of("Europe/Paris")));
        product.setEndDate(ZonedDateTime.of(LocalDateTime.of(2023, 3, 23, 0, 0), ZoneId.of("Europe/Paris")));
        product.setNumberOfBids(5);
        product.setHighestBid(25.00f);
        product.setUser(owner);

        when(productRepository.findById(eq(id))).thenReturn(Optional.of(product));
        when(tokenService.getAuthenticatedUser(any(HttpServletRequest.class))).thenReturn(null);

        SingleProductResponse actualProduct = underTest.getProductById(id, httpServletRequest);

        SingleProductResponse expectedProduct = SingleProductResponse.builder()
                .id(id)
                .productName("Shoes Collection")
                .description("New shoes collection")
                .startPrice(10.00f)
                .images(Collections.singletonList("/images/shoe-4.jpg"))
                .startDate(ZonedDateTime.of(LocalDateTime.of(2023, 3, 23, 0, 0), ZoneId.of("Europe/Paris")))
                .endDate(ZonedDateTime.of(LocalDateTime.of(2023, 3, 23, 0, 0), ZoneId.of("Europe/Paris")))
                .numberOfBids(5)
                .highestBid(25.00f)
                .userId(userId)
                .isOwner(false)
                .isUserHighestBidder(false)
                .build();

        assertEquals(expectedProduct, actualProduct);
    }

    @Test
    @DisplayName("Test should return new arrival products")
    void testGetNewProducts_ReturnsProductPage() {
        Category category = new Category(1L, "Shoes");
        Subcategory subcategory = new Subcategory(1L, "Men", 5, category);
        AppUser appUser = new AppUser();

        Product product1 = Product.builder()
                .id(1L)
                .productName("Shoes Collection")
                .description("New shoes collection")
                .startPrice(59.99f)
                .images(Collections.singletonList(new Image(1L, "./images/shoe-1.jpg", null)))
                .startDate(ZonedDateTime.now(ZoneId.of("Europe/Paris")))
                .endDate(ZonedDateTime.now(ZoneId.of("Europe/Paris")).plusDays(10))
                .numberOfBids(5)
                .highestBid(59.99f)
                .isHighlighted(false)
                .category(category)
                .subcategory(subcategory)
                .user(appUser)
                .info(new ShippingInfo("123 Paris Street", "Paris", "75000", "France", "+1134567890"))
                .sold(false)
                .build();

        Product product2 = Product.builder()
                .id(2L)
                .productName("Shoes Collection")
                .description("New shoes collection")
                .startPrice(59.99f)
                .images(Collections.singletonList(new Image(2L, "./images/shoe-1.jpg", null)))
                .startDate(ZonedDateTime.now(ZoneId.of("Europe/Paris")))
                .endDate(ZonedDateTime.now(ZoneId.of("Europe/Paris")).plusDays(10))
                .numberOfBids(5)
                .highestBid(59.99f)
                .isHighlighted(false)
                .category(category)
                .subcategory(subcategory)
                .user(appUser)
                .info(new ShippingInfo("456 Paris Street", "Paris", "75001", "France", "+1187654321"))
                .sold(false)
                .build();

        List<Product> products = List.of(product1, product2);
        Page<Product> productPage = new PageImpl<>(products);

        List<ProductsResponse> expectedProductResponses = products.stream()
                .map(p -> new ProductsResponse(p.getId(), p.getProductName(), p.getStartPrice(), p.getImages().get(0).getImageUrl(), p.getCategory().getId()))
                .collect(Collectors.toList());

        int pageNumber = 0;
        int size = 2;
        Pageable pageable = PageRequest.of(pageNumber, size);
        when(productRepository.getNewArrivalsProducts(pageable)).thenReturn(productPage);

        Page<ProductsResponse> productResponsePage = underTest.getNewProducts(pageNumber, size);

        assertNotNull(productResponsePage);
        assertEquals(expectedProductResponses, productResponsePage.getContent());
    }


    @Test
    @DisplayName("Test should return new arrival products")
    void testGetNewProducts_ReturnsProductList() {
        List<Product> products = new ArrayList<>();
        Category category = new Category(1L, "Shoes");
        Subcategory subcategory = new Subcategory(1L, "Men", 5, category);
        AppUser appUser = new AppUser();

        products.add(Product.builder()
                .id(1L)
                .productName("Shoes Collection")
                .description("New shoes collection")
                .startPrice(59.99f)
                .images(Collections.singletonList(new Image(1L, "./images/shoe-1.jpg", null)))
                .startDate(ZonedDateTime.now(ZoneOffset.UTC))
                .endDate(ZonedDateTime.now(ZoneOffset.UTC).plusDays(10))
                .numberOfBids(5)
                .highestBid(59.99f)
                .isHighlighted(false)
                .category(category)
                .subcategory(subcategory)
                .user(appUser)
                .info(new ShippingInfo("123 Paris Street", "Paris", "75000", "France", "+1134567890"))
                .sold(false)
                .build());

        products.add(Product.builder()
                .id(2L)
                .productName("Shoes Collection")
                .description("New shoes collection")
                .startPrice(59.99f)
                .images(Collections.singletonList(new Image(2L, "./images/shoe-1.jpg", null)))
                .startDate(ZonedDateTime.now(ZoneOffset.UTC))
                .endDate(ZonedDateTime.now(ZoneOffset.UTC).plusDays(10))
                .numberOfBids(5)
                .highestBid(59.99f)
                .isHighlighted(false)
                .category(category)
                .subcategory(subcategory)
                .user(appUser)
                .info(new ShippingInfo("456 Paris Street", "Paris", "75001", "France", "+1187654321"))
                .sold(false)
                .build());

        int pageNumber = 0;
        int size = 2;
        Pageable pageable = PageRequest.of(pageNumber, size);

        when(productRepository.getNewArrivalsProducts(pageable)).thenReturn(new PageImpl<>(products));

        List<ProductsResponse> expectedProductResponses = new ArrayList<>();
        expectedProductResponses.add(new ProductsResponse(1L, "Shoes Collection", 59.99f, "./images/shoe-1.jpg", 1L));
        expectedProductResponses.add(new ProductsResponse(2L, "Shoes Collection", 59.99f, "./images/shoe-1.jpg", 1L));

        Page<ProductsResponse> productResponsePage = underTest.getNewProducts(pageNumber, size);
        List<ProductsResponse> actualProductResponses = productResponsePage.getContent();

        assertEquals(expectedProductResponses, actualProductResponses);
    }

    @Test
    @DisplayName("Make new auction for the product")
    void createProduct_ShouldSaveProduct() {
        Long appUserId = 2L;
        String userEmail = "testuser@gmail.com";
        AppUser appUser = new AppUser();
        appUser.setId(appUserId);
        appUser.setEmail(userEmail);

        Category category = new Category(2L, "Shoe");
        Subcategory subcategory = new Subcategory(3L, "Boots", 5, category);

        NewProductRequest request = NewProductRequest.builder()
                .productName("Shoe Collection")
                .description("New shoe collection in May")
                .categoryId(category.getId())
                .subcategoryId(subcategory.getId())
                .startPrice(56.33f)
                .startDate(ZonedDateTime.now())
                .endDate(ZonedDateTime.now().plusDays(30))
                .address("123 Manchester Street")
                .city("Manchester")
                .zipCode("1145645")
                .country("United Kingdom")
                .phone("+1174556834")
                .build();

        MockMultipartFile image1 = new MockMultipartFile(
                "image",
                "hello.png",
                MediaType.IMAGE_PNG_VALUE,
                "Image content".getBytes());

        MockMultipartFile image2 = new MockMultipartFile(
                "image",
                "world.png",
                MediaType.IMAGE_PNG_VALUE,
                "Image content".getBytes());

        List<MultipartFile> images = List.of(image1, image2);


        when(tokenService.getAuthenticatedUser(httpServletRequest)).thenReturn(appUser);
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(subcategoryRepository.findById(subcategory.getId())).thenReturn(Optional.of(subcategory));


        when(productRepository.save(any(Product.class))).thenAnswer(i -> {
            Product p = (Product) i.getArguments()[0];
            return p;
        });

        when(imageRepository.save(any(Image.class))).thenAnswer(i -> {
            Image im = (Image) i.getArguments()[0];
            im.setId(1L);
            return im;
        });

        underTest.createProduct(request, images, httpServletRequest);

        productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(productCaptor.capture());
        Product savedProduct = productCaptor.getValue();

        verify(imageRepository, times(images.size())).save(any(Image.class));

        assertThat(savedProduct.getProductName()).isEqualTo(request.getProductName());
        assertThat(savedProduct.getDescription()).isEqualTo(request.getDescription());
        assertThat(savedProduct.getStartPrice()).isEqualTo(request.getStartPrice());
        assertThat(savedProduct.getStartDate()).isEqualTo(request.getStartDate());
        assertThat(savedProduct.getEndDate()).isEqualTo(request.getEndDate());
        assertThat(savedProduct.getInfo().getAddress()).isEqualTo(request.getAddress());
        assertThat(savedProduct.getInfo().getCity()).isEqualTo(request.getCity());
        assertThat(savedProduct.getInfo().getZipCode()).isEqualTo(request.getZipCode());
        assertThat(savedProduct.getInfo().getCountry()).isEqualTo(request.getCountry());
        assertThat(savedProduct.getInfo().getPhone()).isEqualTo(request.getPhone());
        assertThat(savedProduct.getUser()).isEqualTo(appUser);

        fileCaptor = ArgumentCaptor.forClass(MultipartFile.class);
        stringCaptor = ArgumentCaptor.forClass(String.class);

        verify(s3Service, times(images.size())).uploadFile(fileCaptor.capture(), stringCaptor.capture());

        List<MultipartFile> capturedFiles = fileCaptor.getAllValues();
        List<String> capturedStrings = stringCaptor.getAllValues();

        for (int i = 0; i < images.size(); i++) {
            assertThat(capturedFiles.get(i)).isEqualTo(images.get(i));
            assertThat(capturedStrings.get(i)).startsWith(S3_KEY_PREFIX + savedProduct.getId() + "/");
        }
    }

    @Test
    void processCsvFileToCreateProducts_ValidInput_ProcessesCorrectly() throws Exception {
        Reader reader = new StringReader("Csv input");

        AppUser user = new AppUser();
        user.setId(1L);

        ProductCsvImport productCsvImport = ProductCsvImport.builder()
                .productName("Shoe Collection")
                .description("New shoes collection")
                .categoryName("Shoes")
                .subcategoryName("Men")
                .startPrice(String.valueOf(1000.00f))
                .startDate(String.valueOf(ZonedDateTime.now()))
                .endDate(String.valueOf(ZonedDateTime.now().plusDays(10)))
                .address("123 New Atlantic")
                .city("New Orleans")
                .zipCode("71856")
                .country("US")
                .phone("+78345438914")
                .images(Arrays.asList("image1.png", "image2.png", "image3.png"))
                .build();

        List<ProductCsvImport> productsFromCsvFile = List.of(productCsvImport);
        Category category = new Category();
        Subcategory subcategory = new Subcategory();
        byte[] imageBytes = "image bytes".getBytes();
        String s3Url = "http://s3Url";

        when(tokenService.getAuthenticatedUser(httpServletRequest)).thenReturn(user);
        when(productCsvParser.parse(any(Reader.class), eq(ProductCsvImport.class))).thenReturn(productsFromCsvFile);
        when(categoryRepository.findByCategoryName(any())).thenReturn(Optional.of(category));
        when(subcategoryRepository.findBysubCategoryName(any())).thenReturn(Optional.of(subcategory));
        when(restTemplate.getForObject(anyString(), eq(byte[].class))).thenReturn(imageBytes);
        when(s3Service.uploadFile(any(), any())).thenReturn(s3Url);
        when(productRepository.save(any(Product.class))).thenAnswer(i -> {
            Product p = i.getArgument(0);
            p.setId(1L);
            return p;
        });

        underTest.processCsvFileToCreateProducts(reader, httpServletRequest);

        verify(productRepository, times(2)).save(any());
    }

}


