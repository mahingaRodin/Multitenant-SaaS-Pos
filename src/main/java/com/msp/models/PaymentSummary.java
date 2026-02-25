package com.msp.models;

import com.msp.enums.EPaymentType;
import lombok.Data;

@Data
public class PaymentSummary {

    private EPaymentType type;
    private Double totalAmount;
    private int transactionCount;
    private Double percentage;
}
