package no.sparebank1.sb1fs.util;

import org.apache.commons.cli.*;

public class CommandLine {
    public static final String OPTION_TOKEN = "token";
    public static final String OPTION_MOUNT_PATH = "mountPath";
    public static final String OPTION_HELP = "help";

    public static org.apache.commons.cli.CommandLine generateCommandLine(final String[] commandLineArguments) {

        final CommandLineParser cmdLineParser = new DefaultParser();
        org.apache.commons.cli.CommandLine commandLine = null;
        try {
            commandLine = cmdLineParser.parse(generateOptions(), commandLineArguments);
        } catch (ParseException parseException) {
            printUsage();
            System.exit(1);
        }
        return commandLine;
    }


    private static Options generateOptions() {
        final Option mountPath = Option.builder("m")

                .longOpt(OPTION_MOUNT_PATH)
                .hasArg()
                .desc("Mount point/path for the sb1fs filesystem")
                .build();
        final Option token = Option.builder("t")
                .longOpt(OPTION_TOKEN)
                .hasArg()
                .desc("Access token")
                .build();
        final Option help = Option.builder("h")
                .required(false)
                .longOpt(OPTION_HELP)
                .hasArg(false)
                .desc("Show help")
                .build();


        final Options options = new Options();
        options.addOption(mountPath);
        options.addOption(token);
        options.addOption(help);
        return options;
    }


    public static void printUsage() {
        final HelpFormatter formatter = new HelpFormatter();
        final String syntax = "sb1fs";

        formatter.printHelp(syntax, generateOptions(), true);
    }
}
