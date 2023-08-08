package com.retail.retailapp.util;

import com.retail.retailapp.dto.ProductDto;
import com.retail.retailapp.entity.Product;

public class MapperUtils {

    public static Product mapProductDtoToEntity(ProductDto productDto){
        Product product = new Product();
        product.setProductId(productDto.getProductId());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setLastModifiedDate(productDto.getLastModifiedDate());
        return product;
    }

    public static ProductDto mapProductEntityToDto(Product product){
        ProductDto productDto = new ProductDto();
        productDto.setProductId(product.getProductId());
        productDto.setName(product.getName());
        productDto.setPrice(product.getPrice());
        productDto.setLastModifiedDate(product.getLastModifiedDate());
        return productDto;
    }
}
