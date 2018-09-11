package no.sparebank1.sb1fs.saul.http;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BankApiMockServer {

    private static Logger LOG = LoggerFactory.getLogger(BankApiMockServer.class);

    public static void start() {
        Undertow server = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(new HttpHandler() {
                    @Override
                    public void handleRequest(final HttpServerExchange exchange) throws Exception {
                        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");

                        String uri = exchange.getRequestURI();
                        LOG.info("URI: " + uri);

                        if(uri.contains("accounts") && uri.contains("transactions")) {
                            exchange.getResponseSender().send(getResourceFileAsString("/jz/transactions-"+ extractAccount(uri)+".json"));
                        } else if (uri.contains("accounts")) {
                            exchange.getResponseSender().send(getResourceFileAsString("/jz/accounts.json"));
                        }
                    }
                }).build();
        server.start();
    }


    public static String getResourceFileAsString(String fileName) {
        InputStream is = BankApiMockServer.class.getResourceAsStream(fileName);
        if (is != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        }
        return null;
    }

    private static String extractAccount(String url) {
        Pattern p = Pattern.compile("accounts/([0-9]+)/transactions$");
        Matcher matcher = p.matcher(url);
        boolean b= matcher.find();
        if (!b) throw new IllegalArgumentException("Account not matched in url: " + url);
        return matcher.group(1);
    }


    public static void main(String[] args) {
        String s= "/open/personal/banking/accounts/18000192569/transactions";

        Pattern p = Pattern.compile("accounts/([0-9]+)/transactions$");
        Matcher matcher = p.matcher(s);
        matcher.find();
        matcher.group(1);


    }
}