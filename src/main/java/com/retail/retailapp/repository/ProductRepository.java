package com.retail.retailapp.repository;


import com.retail.retailapp.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT i FROM Product i WHERE (:name is null or i.name = :name)" +
            " and (:minPrice is null or i.price >= :minPrice) and (:maxPrice is null or i.price <= :maxPrice) " +
            "and (:minPostedDate is null or i.lastModifiedDate >= :minPostedDate) and (:maxPostedDate is null or i.lastModifiedDate <= :maxPostedDate)")
    List<Product> findAllByGivenFilter(String name, Long minPrice, Long maxPrice, Date minPostedDate, Date maxPostedDate);

    @Query("SELECT p FROM Product p WHERE status='Active' order by p.lastModifiedDate desc")
    List<Product> findAllActiveProducts();
}
