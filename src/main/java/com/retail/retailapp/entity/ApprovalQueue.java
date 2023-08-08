package com.retail.retailapp.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "approval_queue")
public class ApprovalQueue {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO )
    Long approvalId;
    Date date;
    @OneToOne
    @JoinColumn(name = "productId")
    private Product product;
}
