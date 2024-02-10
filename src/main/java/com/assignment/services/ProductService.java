package com.assignment.services;

import com.assignment.dto.ProductRequestDto;
import com.assignment.dto.ResponseDto;
import com.assignment.dto.UpdateProductRequestDto;
import com.assignment.enumuration.DiscountType;
import com.assignment.model.Product;

public interface ProductService {

    Product createProduct(ProductRequestDto productRequestDto);

    Product getProduct(Long id);

    ResponseDto updateProduct(Long id, UpdateProductRequestDto productRequestDto, DiscountType discountType);

    ResponseDto deleteProduct(Long id);


}
