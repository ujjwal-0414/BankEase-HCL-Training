package com.ujjwal.bankease.mapper;

import com.ujjwal.bankease.dto.response.*;
import com.ujjwal.bankease.entity.*;
import org.springframework.stereotype.Component;

@Component
public class BankEaseMapper {
    public UserResponse toUser(User u) {
        return UserResponse.builder().id(u.getId()).firstName(u.getFirstName()).lastName(u.getLastName()).email(u.getEmail()).phoneNumber(u.getPhoneNumber()).kycStatus(u.getKycStatus()).role(u.getRole().getName().name()).build();
    }

    public AccountResponse toAccount(BankAccount a) {
        return AccountResponse.builder().id(a.getId()).accountNumber(a.getAccountNumber()).ifscCode(a.getIfscCode()).accountType(a.getAccountType()).status(a.getStatus()).balance(a.getBalance()).dailyTransferLimit(a.getDailyTransferLimit()).build();
    }

    public BeneficiaryResponse toBeneficiary(Beneficiary b) {
        return BeneficiaryResponse.builder().id(b.getId()).name(b.getName()).accountNumber(b.getAccountNumber()).ifscCode(b.getIfscCode()).bankName(b.getBankName()).nickname(b.getNickname()).active(b.isActive()).build();
    }

    public TransferResponse toTransfer(FundTransfer t) {
        return TransferResponse.builder().id(t.getId()).referenceNumber(t.getReferenceNumber()).sourceAccountId(t.getSourceAccount().getId()).destinationAccountNumber(t.getDestinationAccountNumber()).destinationIfsc(t.getDestinationIfsc()).mode(t.getMode()).amount(t.getAmount()).status(t.getStatus()).remarks(t.getRemarks()).createdAt(t.getCreatedAt()).build();
    }

    public BillPaymentResponse toBill(BillPayment b) {
        return BillPaymentResponse.builder().id(b.getId()).referenceNumber(b.getReferenceNumber()).accountId(b.getAccount().getId()).billerName(b.getBillerName()).billerCategory(b.getBillerCategory()).consumerNumber(b.getConsumerNumber()).amount(b.getAmount()).status(b.getStatus()).createdAt(b.getCreatedAt()).build();
    }

    public LoanResponse toLoan(LoanApplication l) {
        return LoanResponse.builder().id(l.getId()).applicationNumber(l.getApplicationNumber()).loanType(l.getLoanType()).requestedAmount(l.getRequestedAmount()).tenureMonths(l.getTenureMonths()).interestRate(l.getInterestRate()).emiAmount(l.getEmiAmount()).status(l.getStatus()).decisionRemarks(l.getDecisionRemarks()).createdAt(l.getCreatedAt()).decidedAt(l.getDecidedAt()).build();
    }

    public InvestmentProductResponse toProduct(InvestmentProduct p) {
        return InvestmentProductResponse.builder().id(p.getId()).code(p.getCode()).name(p.getName()).type(p.getType()).expectedAnnualReturn(p.getExpectedAnnualReturn()).minimumInvestment(p.getMinimumInvestment()).active(p.isActive()).build();
    }

    public InvestmentResponse toInvestment(Investment i) {
        return InvestmentResponse.builder().id(i.getId()).referenceNumber(i.getReferenceNumber()).productId(i.getProduct().getId()).productName(i.getProduct().getName()).principalAmount(i.getPrincipalAmount()).currentValue(i.getCurrentValue()).status(i.getStatus()).purchasedAt(i.getPurchasedAt()).redeemedAt(i.getRedeemedAt()).build();
    }
}
