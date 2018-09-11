package no.sparebank1.sb1fs.app;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import no.sparebank1.sb1fs.api.accounts.Account;
import no.sparebank1.sb1fs.api.accounts.Sb1Accounts;
import no.sparebank1.sb1fs.fs.DirNode;
import no.sparebank1.sb1fs.fs.FileNode;
import no.sparebank1.sb1fs.fs.Node;


import no.sparebank1.sb1fs.saul.http.BankApiMockServer;
import no.sparebank1.sb1fs.transactions.APITransactions;
import no.sparebank1.sb1fs.transactions.Transaction;
import no.sparebank1.sb1fs.util.Java8Util;
import no.sparebank1.sb1fs.util.UniRest;
import org.apache.commons.cli.CommandLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static no.sparebank1.sb1fs.util.CommandLine.*;


public class Sb1FsApplication {

    private static Logger LOG = LoggerFactory.getLogger(Sb1FsApplication.class);

    public static void main(String[] args) throws IOException {



        BankApiMockServer.start();


        CommandLine commandLine = generateCommandLine(args);

        String token = commandLine.getOptionValue(OPTION_TOKEN, "tokentokentoken");
        String mountPath = commandLine.getOptionValue(OPTION_MOUNT_PATH, "t");

        if (commandLine.hasOption(OPTION_HELP) || token == null) {
            printUsage();
            System.exit(0);
        }

        LOG.info("Token to use: {}", token.substring(0, token.length() - 10) + "**********");
        LOG.info("Mount path:   {}", mountPath);


        UniRest.configureUnirest();


        HttpResponse<Sb1Accounts> response = Java8Util.propagate(() -> Unirest
                //.get("https://developer-api.sparebank1.no/open/personal/banking/accounts/all")
                .get("http://localhost:8080//open/personal/banking/accounts/all")
                .header("Accept", "application/vnd.sparebank1.v1+json")
                .header("Authorization", "Bearer " + token)
                .asObject(Sb1Accounts.class));

        Sb1Accounts sb1Accounts = response.getBody();

        DirNode root = new DirNode("/", Arrays.stream(sb1Accounts.getAccounts()).map(account ->
                new DirNode((account.getAccountNumber().getFormatted()+ " " + account.getName()).replaceAll("\\W", "_"), getNodesForAccount(account, token))
        ).collect(Collectors.toList()));


        new no.sparebank1.sb1fs.fs.Sb1fs(root, mountPath).mount();


        System.in.read();

    }

    private static List<Node> getSubnodes(Account account) {

        /**
         * TODO: Implement something more interesting here!
         *
         * Check no.sparebank1.sb1fs.saul.SaulFs for some inspiration.
         *
         */

        return Collections.emptyList();
    }


    private static List<Node> getNodesForAccount(Account account, String token) {
        return asList(getBalanceNode(account), getTransactionFileNodes(account, token)).stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
    private static List<Node> getTransactionFileNodes(Account account, String token) {
        HttpResponse<APITransactions> response = Java8Util.propagate(() -> Unirest
                .get(account.getLinks().getTransactions().getHref())
                .header("Accept", "application/vnd.sparebank1.v1+json")
                .header("Authorization", "Bearer " + token)
                .asObject(APITransactions.class));
        return  Arrays.stream(response.getBody().getTransactions()).map(transaction ->
                new FileNode(transaction.getArchiveReference() + "_" + transaction.getDescription() + ".csv",
                        getFileContent(transaction),
                        getTimestamp(transaction)))
                .collect(Collectors.toList());
    }
    private static List<Node> getBalanceNode(Account account) {
        return asList(new DirNode("saldo", asList(new FileNode(new DecimalFormat("#.00").format(account.getAvailableBalance().getAmount()) + ".kr", "Available balance"))));
    }
    private static String getFileContent(Transaction t) {
        StringBuilder sb = new StringBuilder();
       // sb.append("accounting_date,remote_account,transaction_type,amount").append('\n');
        sb.append(t.getAccountingDate()).append(",").append((t.getDescription())).append(",")
                .append(t.getAmount().getAmount())
                .append('\n');
        return sb.toString();
    }
    private static long getTimestamp(Transaction t) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(t.getAccountingDate()).getTime();
        } catch (ParseException e) {
            return 0;
        }
    }

}
