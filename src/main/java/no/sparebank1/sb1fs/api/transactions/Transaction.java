package no.sparebank1.sb1fs.api.transactions;

import java.util.*;
import com.fasterxml.jackson.annotation.*;

public class Transaction {
    private Amount amount;
    private String accountingDate;
    private String description;
    private String archiveReference;
    private TransactionLinks links;
    private String remoteAccount;
    private TransactionCode transactionCode;
    private TransactionType transactionType;

    @JsonProperty("amount")
    public Amount getAmount() { return amount; }
    @JsonProperty("amount")
    public void setAmount(Amount value) { this.amount = value; }

    @JsonProperty("accountingDate")
    public String getAccountingDate() { return accountingDate; }
    @JsonProperty("accountingDate")
    public void setAccountingDate(String value) { this.accountingDate = value; }

    @JsonProperty("description")
    public String getDescription() { return description; }
    @JsonProperty("description")
    public void setDescription(String value) { this.description = value; }

    @JsonProperty("archiveReference")
    public String getArchiveReference() { return archiveReference; }
    @JsonProperty("archiveReference")
    public void setArchiveReference(String value) { this.archiveReference = value; }

    @JsonProperty("_links")
    public TransactionLinks getLinks() { return links; }
    @JsonProperty("_links")
    public void setLinks(TransactionLinks value) { this.links = value; }

    @JsonProperty("remoteAccount")
    public String getRemoteAccount() { return remoteAccount; }
    @JsonProperty("remoteAccount")
    public void setRemoteAccount(String value) { this.remoteAccount = value; }

    @JsonProperty("transactionCode")
    public TransactionCode getTransactionCode() { return transactionCode; }
    @JsonProperty("transactionCode")
    public void setTransactionCode(TransactionCode value) { this.transactionCode = value; }

    @JsonProperty("transactionType")
    public TransactionType getTransactionType() { return transactionType; }
    @JsonProperty("transactionType")
    public void setTransactionType(TransactionType value) { this.transactionType = value; }

    @Override
    public String toString() {
        return "Transaction{" +
                "amount=" + amount +
                ", accountingDate='" + accountingDate + '\'' +
                ", description='" + description + '\'' +
                ", archiveReference='" + archiveReference + '\'' +
                ", links=" + links +
                ", remoteAccount='" + remoteAccount + '\'' +
                ", transactionCode=" + transactionCode +
                ", transactionType=" + transactionType +
                '}';
    }
}
