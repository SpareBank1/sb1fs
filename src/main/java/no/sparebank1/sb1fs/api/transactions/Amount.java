package no.sparebank1.sb1fs.api.transactions;

import java.util.*;
import com.fasterxml.jackson.annotation.*;

public class Amount {
    private double amount;
    private CurrencyCode currencyCode;

    @JsonProperty("amount")
    public double getAmount() { return amount; }
    @JsonProperty("amount")
    public void setAmount(double value) { this.amount = value; }

    @JsonProperty("currencyCode")
    public CurrencyCode getCurrencyCode() { return currencyCode; }
    @JsonProperty("currencyCode")
    public void setCurrencyCode(CurrencyCode value) { this.currencyCode = value; }

    @Override
    public String toString() {
        return "Amount{" +
                "amount=" + amount +
                ", currencyCode=" + currencyCode +
                '}';
    }
}
