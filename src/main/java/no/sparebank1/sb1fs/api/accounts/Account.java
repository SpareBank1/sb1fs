package no.sparebank1.sb1fs.api.accounts;

import com.fasterxml.jackson.annotation.*;

public class Account {
    private String id;
    private AccountNumber accountNumber;
    private String name;
    private String description;
    private Balance balance;
    private Balance availableBalance;
    private Owner owner;
    private String product;
    private String type;
    private String iban;
    private AccountLinks links;

    @JsonProperty("id")
    public String getID() { return id; }
    @JsonProperty("id")
    public void setID(String value) { this.id = value; }

    @JsonProperty("accountNumber")
    public AccountNumber getAccountNumber() { return accountNumber; }
    @JsonProperty("accountNumber")
    public void setAccountNumber(AccountNumber value) { this.accountNumber = value; }

    @JsonProperty("name")
    public String getName() { return name; }
    @JsonProperty("name")
    public void setName(String value) { this.name = value; }

    @JsonProperty("description")
    public String getDescription() { return description; }
    @JsonProperty("description")
    public void setDescription(String value) { this.description = value; }

    @JsonProperty("balance")
    public Balance getBalance() { return balance; }
    @JsonProperty("balance")
    public void setBalance(Balance value) { this.balance = value; }

    @JsonProperty("availableBalance")
    public Balance getAvailableBalance() { return availableBalance; }
    @JsonProperty("availableBalance")
    public void setAvailableBalance(Balance value) { this.availableBalance = value; }

    @JsonProperty("owner")
    public Owner getOwner() { return owner; }
    @JsonProperty("owner")
    public void setOwner(Owner value) { this.owner = value; }

    @JsonProperty("product")
    public String getProduct() { return product; }
    @JsonProperty("product")
    public void setProduct(String value) { this.product = value; }

    @JsonProperty("type")
    public String getType() { return type; }
    @JsonProperty("type")
    public void setType(String value) { this.type = value; }

    @JsonProperty("iban")
    public String getIban() { return iban; }
    @JsonProperty("iban")
    public void setIban(String iban) { this.iban = iban; }

    @JsonProperty("_links")
    public AccountLinks getLinks() { return links; }
    @JsonProperty("_links")
    public void setLinks(AccountLinks value) { this.links = value; }
}
