package com.retail.retailapp.repository;

import com.retail.retailapp.entity.ApprovalQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprovalQueueRepository extends JpaRepository<ApprovalQueue, Long> {
    @Query("SELECT i FROM ApprovalQueue i order by date desc")
    List<ApprovalQueue> findAllByDate();

    @Modifying
    @Query(
            value = "DELETE FROM approval_queue u WHERE u.product_id = :productId",
            nativeQuery = true)
    void deleteByProductId(Long productId);
}
