package com.assignment.controllers;

import com.assignment.dto.ProductRequestDto;
import com.assignment.dto.ResponseDto;
import com.assignment.dto.UpdateProductRequestDto;
import com.assignment.enumuration.DiscountType;
import com.assignment.model.Product;
import com.assignment.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v0")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;


    @PostMapping("/create/product")
    public Product createProduct(@RequestBody ProductRequestDto productRequestDto){
        return productService.createProduct(productRequestDto);
    }

    @GetMapping("/get/product/{id}")
    public Product getProduct(@PathVariable(value = "id") Long id){
        return productService.getProduct(id);
    }

    @PutMapping("/update/product/{id}")
    public ResponseDto updateProduct(
            @PathVariable(value = "id") Long id,
            @RequestParam(value = "Discount/Tax") DiscountType discountType,
            @RequestBody UpdateProductRequestDto productRequestDto)
    {
        return productService.updateProduct(id, productRequestDto, discountType);
    }

    @DeleteMapping("/delete/product/{id}")
    public ResponseDto deleteProduct(@PathVariable(value = "id") Long id){
        return productService.deleteProduct(id);
    }

}
