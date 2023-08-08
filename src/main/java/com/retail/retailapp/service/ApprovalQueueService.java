package com.retail.retailapp.service;

import com.retail.retailapp.entity.ApprovalQueue;
import com.retail.retailapp.entity.Product;
import com.retail.retailapp.exception.NoApprovalQueueRecordFoundException;
import com.retail.retailapp.repository.ApprovalQueueRepository;
import com.retail.retailapp.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ApprovalQueueService {

    @Autowired
    private ApprovalQueueRepository approvalQueueRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<ApprovalQueue> findAllRecentApprovalQueue() {
        return approvalQueueRepository.findAllByDate();
    }

    @Transactional
    public void approveProduct(Long approvalId) throws NoApprovalQueueRecordFoundException {
        Optional<ApprovalQueue> approvalQueue = approvalQueueRepository.findById(approvalId);
        if (approvalQueue.isPresent()) {
            Product product = approvalQueue.get().getProduct();
            product.setStatus("Active");
            productRepository.save(product);
            approvalQueueRepository.deleteById(approvalId);
        } else {
            throw new NoApprovalQueueRecordFoundException("No record found with approval id " + approvalId);
        }
    }

    public void rejectProduct(Long approvalId) throws NoApprovalQueueRecordFoundException {
        Optional<ApprovalQueue> approvalQueue = approvalQueueRepository.findById(approvalId);
        if (approvalQueue.isPresent()) {
            approvalQueueRepository.deleteById(approvalId);
        } else {
            throw new NoApprovalQueueRecordFoundException("No record found with approval id " + approvalId);
        }
    }
}
