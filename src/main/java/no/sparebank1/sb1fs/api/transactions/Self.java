package no.sparebank1.sb1fs.api.transactions;

import java.util.*;
import com.fasterxml.jackson.annotation.*;

public class Self {
    private String href;
    private String method;

    @JsonProperty("href")
    public String getHref() { return href; }
    @JsonProperty("href")
    public void setHref(String value) { this.href = value; }

    @JsonProperty("method")
    public String getMethod() { return method; }
    @JsonProperty("method")
    public void setMethod(String value) { this.method = value; }
}
