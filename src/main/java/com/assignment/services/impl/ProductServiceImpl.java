package com.assignment.services.impl;

import com.assignment.dto.ProductRequestDto;
import com.assignment.dto.ResponseDto;
import com.assignment.dto.UpdateProductRequestDto;
import com.assignment.enumuration.DiscountType;
import com.assignment.exception.NotFoundException;
import com.assignment.exception.UnProcessableEntityException;
import com.assignment.model.Product;
import com.assignment.repository.ProductRepository;
import com.assignment.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;


@Component
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Product createProduct(ProductRequestDto productRequestDto) {

        if(Objects.isNull(productRequestDto)){
            throw new UnProcessableEntityException("Request dto is null.");
        }

        if(!StringUtils.hasLength(productRequestDto.getName())){
            throw new UnProcessableEntityException("Create Product failed ! Name can be Null or can not have zero length." );
        }

        if(Objects.isNull(productRequestDto.getPrice()))
        {
            throw new UnProcessableEntityException("Create Product failed ! Price required!");
        }

        if(Objects.isNull(productRequestDto.getQuantityAvailable())){
            throw new UnProcessableEntityException("Create Product failed ! Available quantity required!");
        }

        Product product = new Product();
        product.setName(productRequestDto.getName());
        product.setDescription(Objects.nonNull(productRequestDto.getDescription())? productRequestDto.getDescription() : null);
        product.setPrice(productRequestDto.getPrice());
        product.setQuantityAvailable(productRequestDto.getQuantityAvailable());
        return productRepository.save(product);
    }

    @Override
    public Product getProduct(Long id) {
        return productRepository.findById(id).orElseThrow(
                ()->new NotFoundException("No Product found by this id :: " + id));
    }

    @Override
    public ResponseDto updateProduct(Long id, UpdateProductRequestDto productRequestDto, DiscountType discountType) {

        if(Objects.isNull(productRequestDto)){
            return ResponseDto.builder().message("Updated the product failed.").build();
        }

        Product product = productRepository.findById(id).orElseThrow(
                ()->new NotFoundException("Update product failed ! No product found with given id: "+id));

        if(StringUtils.hasLength(productRequestDto.getName())){
            product.setName(productRequestDto.getName());
        }

        if(StringUtils.hasLength(productRequestDto.getDescription())){
            product.setDescription(productRequestDto.getDescription());
        }

        if(Objects.nonNull(productRequestDto.getQuantityAvailable())) {
            product.setQuantityAvailable(product.getQuantityAvailable());
        }

        if(Objects.nonNull(productRequestDto.getPrice())){
            product.setPrice(product.getPrice());
        }

        if(Objects.nonNull(productRequestDto.getPercentDiscountOrTax())){
        Double updatedPrice =  product.getPrice();
        switch (discountType){
            case TAX -> updatedPrice = applyTax(productRequestDto.getPercentDiscountOrTax(), updatedPrice) ;
            case DISCOUNT -> updatedPrice = applyDiscount(productRequestDto.getPercentDiscountOrTax(), updatedPrice);
            default -> ResponseDto.builder().message("Update product failed!");
        }
        product.setPrice(updatedPrice);
        }

        productRepository.save(product);
        return ResponseDto.builder().message("Updated the product successfully.").build();
    }

    @Override
    public ResponseDto deleteProduct(Long id) {

        if(Objects.isNull(id)){
            return ResponseDto.builder().message("Delete product failed.").build();
        }

        productRepository.deleteById(id);
        return ResponseDto.builder().message("Deleted product successfully").build();
    }

    private Double applyTax(Integer percentBy, Double price){
        return price+price*((double)percentBy/100);
    }
    private Double applyDiscount(Integer percentBy, Double price){
        return price-price*((double)percentBy/100);
    }
}
