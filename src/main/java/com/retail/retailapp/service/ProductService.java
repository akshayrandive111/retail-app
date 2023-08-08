package com.retail.retailapp.service;

import com.retail.retailapp.dto.ProductDto;
import com.retail.retailapp.entity.ApprovalQueue;
import com.retail.retailapp.entity.Product;
import com.retail.retailapp.exception.NoProductFoundException;
import com.retail.retailapp.exception.ProductPriceExhaustThresholdException;
import com.retail.retailapp.repository.ApprovalQueueRepository;
import com.retail.retailapp.repository.ProductRepository;
import com.retail.retailapp.util.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ApprovalQueueRepository approvalQueueRepository;

    public List<ProductDto> findAllProducts() {
        List<ProductDto> products = new ArrayList<>();
        productRepository.findAllActiveProducts().forEach(product -> products.add(MapperUtils.mapProductEntityToDto(product)));
        return products;
    }

    public List<ProductDto> findProductsBy(String name, Long minPrice, Long maxPrice, Date minPostedDate, Date maxPostedDate) {
        List<ProductDto> products = new ArrayList<>();
        productRepository.findAllByGivenFilter(name, minPrice, maxPrice, minPostedDate, maxPostedDate).forEach(product -> products.add(MapperUtils.mapProductEntityToDto(product)));
        return products;
    }

    public void addProduct(ProductDto productDto) throws ProductPriceExhaustThresholdException {
        Product product = MapperUtils.mapProductDtoToEntity(productDto);
        if (product.getPrice() <= 5000) {
            product.setStatus("Active");
            product.setLastModifiedDate(new Date(System.currentTimeMillis()));
            productRepository.save(product);
        } else if (product.getPrice() < 10000) {
            product.setStatus("Inactive");
            product.setLastModifiedDate(new Date(System.currentTimeMillis()));
            Product savedProduct = productRepository.save(product);
            ApprovalQueue approvalQueue = new ApprovalQueue();
            approvalQueue.setProduct(savedProduct);
            approvalQueue.setDate(new Date(System.currentTimeMillis()));
            approvalQueueRepository.save(approvalQueue);
        } else {
            throw new ProductPriceExhaustThresholdException("Product price exhausting threshold limit of 10000");
        }
    }

    @Transactional
    public String updateProduct(Long productId, ProductDto productDto) throws NoProductFoundException {
        Product product = MapperUtils.mapProductDtoToEntity(productDto);
        Product updateProduct = productRepository.findById(productId)
                .orElseThrow(() -> new NoProductFoundException("Product not exist with id: " + productId));

        Long fiftyPercentPrice = (50 * updateProduct.getPrice()) / 100;

        updateProduct.setPrice(product.getPrice());
        updateProduct.setName(product.getName());
        updateProduct.setLastModifiedDate(new Date(System.currentTimeMillis()));

        if (product.getPrice() > fiftyPercentPrice) {
            updateProduct.setStatus("Inactive");
            productRepository.save(updateProduct);
            ApprovalQueue approvalQueue = new ApprovalQueue();
            approvalQueue.setProduct(updateProduct);
            approvalQueue.setDate(new Date(System.currentTimeMillis()));
            approvalQueueRepository.save(approvalQueue);
            return "Approval_Needed";
        } else {
            updateProduct.setStatus("Active");
            productRepository.save(updateProduct);
            return "Updated";
        }

    }

    @Transactional
    public void deleteProduct(Long id) throws NoProductFoundException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoProductFoundException("Product not found with id : " + id));
        approvalQueueRepository.deleteByProductId(product.getProductId());
        productRepository.deleteById(id);
    }
}
