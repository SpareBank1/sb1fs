package no.sparebank1.sb1fs.api.transactions;

import java.util.*;
import com.fasterxml.jackson.annotation.*;

public class TransactionLinks {
    private Self details;

    @JsonProperty("details")
    public Self getDetails() { return details; }
    @JsonProperty("details")
    public void setDetails(Self value) { this.details = value; }

    @Override
    public String toString() {
        return "TransactionLinks{" +
                "details=" + details +
                '}';
    }
}
