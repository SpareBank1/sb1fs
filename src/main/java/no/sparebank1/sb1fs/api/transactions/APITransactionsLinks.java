package no.sparebank1.sb1fs.api.transactions;

import java.util.*;
import com.fasterxml.jackson.annotation.*;

public class APITransactionsLinks {
    private Self self;

    @JsonProperty("self")
    public Self getSelf() { return self; }
    @JsonProperty("self")
    public void setSelf(Self value) { this.self = value; }
}
