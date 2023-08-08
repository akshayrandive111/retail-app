package com.retail.retailapp.controller;

import com.retail.retailapp.dto.ProductDto;
import com.retail.retailapp.entity.ApprovalQueue;
import com.retail.retailapp.exception.NoApprovalQueueRecordFoundException;
import com.retail.retailapp.exception.NoProductFoundException;
import com.retail.retailapp.exception.ProductPriceExhaustThresholdException;
import com.retail.retailapp.service.ApprovalQueueService;
import com.retail.retailapp.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ApprovalQueueService approvalQueueService;

    @Operation(description = "Get the list of active products sorted by the latest first.")
    @GetMapping("/api/products")
    public List<ProductDto> getProducts() {
        return productService.findAllProducts();
    }

    @Operation(description = "Search for products based on the given criteria (product name, price range, and posted date range).")
    @GetMapping("/api/products/search")
    public List<ProductDto> searchProduct(@RequestParam(required = false) String productName,
                                          @RequestParam(required = false) Long minPrice,
                                          @RequestParam(required = false) Long maxPrice,
                                          @RequestParam(required = false) Date minPostedDate,
                                          @RequestParam(required = false) Date maxPostedDate) {
        return productService.findProductsBy(productName, minPrice, maxPrice, minPostedDate, maxPostedDate);
    }

    @Operation(description = "Create a new product, but the price must not exceed $10,000. If the price is more than $5,000, " +
            "then product moved to the approval queue.")
    @PostMapping("/api/products")
    public ResponseEntity<String> saveProduct(@RequestBody ProductDto productDto) throws ProductPriceExhaustThresholdException {
        productService.addProduct(productDto);
        return ResponseEntity.ok("Product added successfully!.");
    }

    @Operation(description = "Update an existing product, but if the price is more than 50% of its previous price, " +
            "the product moved to the approval queue.")
    @PutMapping("/api/products/{productId}")
    public HttpEntity<? extends Serializable> updateProduct(@PathVariable("productId") Long productId, @RequestBody ProductDto productDto) throws NoProductFoundException {
        String response = productService.updateProduct(productId, productDto);
        if (response.equalsIgnoreCase("Updated"))
            return ResponseEntity.ok("Product updated successfully!.");
        else
            return ResponseEntity.ok("Approval request raised for product approval");
    }

    @Operation(description = "Delete a product")
    @DeleteMapping("/api/products/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable("productId") Long productId) throws NoProductFoundException {
        productService.deleteProduct(productId);
        return ResponseEntity.ok("Product deleted successfully!.");
    }

    @Operation(description = "Get all the products in the approval queue, sorted by request date (earliest first).")
    @GetMapping("/api/products/approval-queue")
    public List<ApprovalQueue> getProductsFromApprovalQueue() {
        return approvalQueueService.findAllRecentApprovalQueue();
    }

    @Operation(description = "Approve a product from the approval queue and removes record from approval queue.")
    @PutMapping("/api/products/approval-queue/{approvalId}/approve")
    public ResponseEntity<String> approveProduct(@PathVariable("approvalId") Long approvalId) throws NoApprovalQueueRecordFoundException {
        approvalQueueService.approveProduct(approvalId);
        return ResponseEntity.ok("Product approved successfully!.");
    }

    @Operation(description = ": Reject a product from the approval queue and removes record from approval queue.")
    @PutMapping("/api/products/approval-queue/{approvalId}/reject")
    public ResponseEntity<String> rejectProduct(@PathVariable("approvalId") Long approvalId) throws NoApprovalQueueRecordFoundException {
        approvalQueueService.rejectProduct(approvalId);
        return ResponseEntity.ok("Product rejected.");
    }
}
