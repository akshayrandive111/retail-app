package com.retail.retailapp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ProductDto {
    Long productId;
    String name;
    Long price;
    Date lastModifiedDate;
}
