// To use this code, add the following Maven dependency to your project:
//
//     com.fasterxml.jackson.core : jackson-databind : 2.9.0
//
// Import this package:
//
//     import no.sparebank1.sb1fs.api.transactions.Converter;
//
// Then you can deserialize a JSON string with
//
//     APITransactions data = Converter.fromJsonString(jsonString);

package no.sparebank1.sb1fs.transactions;

import java.io.ByteArrayOutputStream;
import java.util.*;
import java.io.IOException;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.JsonProcessingException;

public class Converter {
    // Serialize/deserialize helpers

    public static APITransactions fromJsonString(String json) throws IOException {
        return getObjectReader().readValue(json);
    }

    public static String toJsonString(APITransactions obj) throws JsonProcessingException {
        return getObjectWriter().writeValueAsString(obj);
    }

    private static ObjectReader reader;
    private static ObjectWriter writer;

    private static void instantiateMapper() {
        ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader(APITransactions.class);
        writer = mapper.writerFor(APITransactions.class);
    }

    private static ObjectReader getObjectReader() {
        if (reader == null) instantiateMapper();
        return reader;
    }

    private static ObjectWriter getObjectWriter() {
        if (writer == null) instantiateMapper();
        return writer;
    }



    static Amount getAmount() {
        Amount a = new Amount();
        a.setCurrencyCode("NOK");
        a.setAmount( (new Random().nextInt(1000)*20) + 2000);
        return a;
    }

    static String getAccountingDate() {

        return "2018-09-15";
    }


    static String getArchiveReference() {
        return UUID.randomUUID().toString().substring(0, 10);
    }

    static String getDescription() {
        String[] description = { "VOLVOFINANS", "BOLIGKREDITT" };

        return description[new Random().nextInt(description.length)];

    }

    public static void main(String[] args) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();




        int numberOfTransactions = new Random().nextInt(72) + 3;

        List<Transaction> l = new ArrayList<>();
        for (int i = 0; i < numberOfTransactions; i++) {
            Transaction t = new Transaction();
            t.setAmount(getAmount());
            t.setAccountingDate(getAccountingDate());
            t.setArchiveReference(getArchiveReference());
            t.setDescription(getDescription());
            l.add(t);
        }


        APITransactions transactions = new APITransactions();
        Transaction[] transactionArray = new Transaction[l.size()];
        transactionArray = l.toArray(transactionArray);
        transactions.setTransactions(transactionArray);


        getObjectWriter().writeValue(bos, transactions);

        String s = new String(bos.toByteArray());
        System.out.println(s);
    }
}
