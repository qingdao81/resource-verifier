package com.qingdao81.testresourceverifier.cli;

import com.qingdao81.testresourceverifier.annotation.ResourceVerifier;
import org.apache.commons.cli.*;

/**
 * Parser for command line properties.
 */
class CLIParser {

    CLIProperties parse(String[] args) throws ParseException {
        CommandLine cmd = new DefaultParser().parse(buildOptions(), args);
        return ImmutableCLIProperties.builder()
            .host(cmd.getOptionValue("host"))
            .port(cmd.hasOption("port") ? Integer.parseInt(cmd.getOptionValue("port")) : -1)
            .type(ResourceVerifier.Type.fromName(cmd.getOptionValue("type")))
            .timeout(cmd.hasOption("timeout") ? Long.parseLong(cmd.getOptionValue("timeout")) : 120_000)
            .uri(cmd.hasOption("uri") ? cmd.getOptionValue("uri") : "")
            .logDetails(cmd.hasOption("logDetails") ? true : false)
            .build();
    }

    private static Options buildOptions() {
        Option host = Option.builder().longOpt("host").hasArg().required()
                .desc("host of the resource").build();
        Option port = Option.builder().longOpt("port").hasArg()
                .desc("port of the resource").build();
        Option type = Option.builder().longOpt("type").hasArg().required()
                .desc("type of the resource").build();
        Option timeout = Option.builder().longOpt("timeout").hasArg()
                .desc("timeout of the resource").build();
        Option uri = Option.builder().longOpt("uri").hasArg()
                .desc("uri of the resource").build();
        Option logDetails = Option.builder().longOpt("logDetails")
                .desc("logDetails of the resource").build();
        Options options = new Options();
        options.addOption(host).addOption(port).addOption(type).addOption(timeout).addOption(uri).addOption(logDetails);
        return options;
    }

}
