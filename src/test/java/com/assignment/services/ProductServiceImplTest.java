package com.assignment.services;


import com.assignment.dto.ProductRequestDto;
import com.assignment.dto.ResponseDto;
import com.assignment.dto.UpdateProductRequestDto;
import com.assignment.enumuration.DiscountType;
import com.assignment.exception.NotFoundException;
import com.assignment.exception.UnProcessableEntityException;
import com.assignment.model.Product;
import com.assignment.repository.ProductRepository;
import com.assignment.services.impl.ProductServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter =objectMapper.writer();

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;


    @Before
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        this.mockMvc= MockMvcBuilders.standaloneSetup(productService).build();
    }


    @Test
    @DisplayName("Successful creation test")
    public void createProductMethodTest(){

        ProductRequestDto productRequestDto = new ProductRequestDto("name", "description",100.0d,40);
        Product product = new Product(1l,"name", "description",100.0d,40);
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(product);
        Product product1 = productService.createProduct(productRequestDto);
        Assertions.assertInstanceOf(Product.class, product1);
    }

    @Test
    @DisplayName("Null request body test in create product")
    public void createProductWithNullRequest(){
        Assertions.assertThrows(UnProcessableEntityException.class, ()-> productService.createProduct(null));
    }

    @Test
    @DisplayName("Null name in request body test in create product")
    public void createProductWithNullName(){
        ProductRequestDto productRequestDto = new ProductRequestDto(null, "description",100.0d,40);
        Assertions.assertThrows(UnProcessableEntityException.class, ()-> productService.createProduct(productRequestDto));
    }

    @Test
    @DisplayName("Null price in request body test in create product")
    public void createProductWithNullPrice(){
        ProductRequestDto productRequestDto = new ProductRequestDto("Name", "description",null,40);
        Assertions.assertThrows(UnProcessableEntityException.class, ()-> productService.createProduct(productRequestDto));
    }

    @Test
    @DisplayName("Null quantity in request body test in create product")
    public void createProductWithNullQuantity(){
        ProductRequestDto productRequestDto = new ProductRequestDto("Name", "description",100.0,null);
        Assertions.assertThrows(UnProcessableEntityException.class, ()-> productService.createProduct(productRequestDto));
    }


    // Testing Get product methods.
    @Test
    @DisplayName("Get Successful product object test")
    public void getProductByIdTest(){
        Product product = new Product(1l,"name", "description",100.0d,40);
        Mockito.when(productRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(product));

        Product product1 = productService.getProduct(1l);
        Assertions.assertNotNull(product1);
        Assertions.assertEquals(product.getId(), product1.getId());
    }


    @Test
    @DisplayName("Get product object exception test")
    public void getProductByIdExceptionTest(){
        Mockito.when(productRepository.findById(Mockito.anyLong())).thenThrow(new NotFoundException("error"));
        Assertions.assertThrows(NotFoundException.class,()->productService.getProduct(1l));

    }


    //delete product test cases
    @Test
    @DisplayName("Delete product object test")
    public void deleteProductByIdTest(){
        ResponseDto responseDto = productService.deleteProduct(1l);
        Assertions.assertNotNull(responseDto);
        Assertions.assertEquals("Deleted product successfully", responseDto.getMessage());
        Mockito.verify(productRepository,Mockito.times(1)).deleteById(1l);

    }


    @Test
    @DisplayName("Update product test cases successful for discount")
    public void updateProductTestCaseForSuccessfulDiscount(){

        Product product = new Product(1l,"name", "description",100.0d,40);

        UpdateProductRequestDto updateProductRequestDto = new UpdateProductRequestDto();
        updateProductRequestDto.setPercentDiscountOrTax(10);
        DiscountType discountType = DiscountType.DISCOUNT;

        Mockito.when(productRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(product));

        ResponseDto responseDto = productService.updateProduct(1l, updateProductRequestDto, discountType);

        Assertions.assertNotNull(responseDto);
        Assertions.assertEquals("Updated the product successfully.", responseDto.getMessage());
        Assertions.assertEquals(90.0, product.getPrice());

    }

    @Test
    @DisplayName("Update product test cases successful for tax")
    public void updateProductTestCaseForTax(){
        Product product = new Product(1l,"name", "description",100.0d,40);

        UpdateProductRequestDto updateProductRequestDto = new UpdateProductRequestDto();
        updateProductRequestDto.setPercentDiscountOrTax(10);
        DiscountType discountType = DiscountType.TAX;

        Mockito.when(productRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(product));

        ResponseDto responseDto = productService.updateProduct(1l, updateProductRequestDto, discountType);

        Assertions.assertNotNull(responseDto);
        Assertions.assertEquals("Updated the product successfully.", responseDto.getMessage());
        Assertions.assertEquals(110.0, product.getPrice());
    }


    @Test
    @DisplayName("Update product unit test case for null request dto")
    public void updateProductTestForException(){

        ResponseDto responseDto = productService.updateProduct(1l, null, DiscountType.DISCOUNT);

        Assertions.assertNotNull(responseDto);
        Assertions.assertEquals("Updated the product failed.", responseDto.getMessage());
    }


}
