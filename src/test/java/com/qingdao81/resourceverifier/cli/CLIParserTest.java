package com.qingdao81.resourceverifier.cli;

import com.qingdao81.resourceverifier.annotation.ResourceVerifier;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@RunWith(DataProviderRunner.class)
public class CLIParserTest {

    private CLIParser parser;

    @Test
    @UseDataProvider("parseDP")
    public void testParse(String[] args, CLIProperties expected) throws Exception {
        // when
        CLIProperties result = parser.parse(args);
        // then
        assertThat(result).isEqualTo(expected);
    }

    @DataProvider
    public static Object[][] parseDP() {
        return new Object[][] {
                { new String[]{"--host", "host", "--type", "kafka"},
                        ImmutableCLIProperties.builder()
                                .host("host")
                                .type(ResourceVerifier.Type.KAFKA)
                                .port(-1)
                                .timeout(120_000L)
                                .uri("")
                                .logDetails(false)
                                .build() },
                { new String[]{"--host", "host", "--type", "kafka", "--port", "9092"},
                        ImmutableCLIProperties.builder()
                                .host("host")
                                .type(ResourceVerifier.Type.KAFKA)
                                .port(9092)
                                .timeout(120_000L)
                                .uri("")
                                .logDetails(false)
                                .build() },
                { new String[]{"--host", "host", "--type", "kafka", "--port", "9092", "--timeout", "10000"},
                        ImmutableCLIProperties.builder()
                                .host("host")
                                .type(ResourceVerifier.Type.KAFKA)
                                .port(9092)
                                .timeout(10000L)
                                .uri("")
                                .logDetails(false)
                                .build() },
                { new String[]{"--host", "host", "--type", "kafka", "--port", "9092", "--timeout", "10000",
                        "--uri", "ping"},
                        ImmutableCLIProperties.builder()
                                .host("host")
                                .type(ResourceVerifier.Type.KAFKA)
                                .port(9092)
                                .timeout(10000L)
                                .uri("ping")
                                .logDetails(false)
                                .build() },
                { new String[]{"--host", "host", "--type", "kafka", "--port", "9092", "--timeout", "10000",
                        "--uri", "ping", "--logDetails", "true"},
                        ImmutableCLIProperties.builder()
                                .host("host")
                                .type(ResourceVerifier.Type.KAFKA)
                                .port(9092)
                                .timeout(10000L)
                                .uri("ping")
                                .logDetails(true)
                                .build() },
        };
    }

    @Test
    @UseDataProvider("parseErrorDP")
    public void testParseError(String[] args) throws Exception {
        try {
            // when
            CLIProperties result = parser.parse(args);
            fail("exception expected");
        } catch (Exception ex) {
            // then
        }

    }

    @DataProvider
    public static Object[][] parseErrorDP() {
        return new Object[][]{
                { new String[]{} },
                { new String[]{"--host", "host"} },
                { new String[]{"--host", "host", "--type", "kafka", "--invalid"} },

        };
    }

    @Before
    public void setUp() throws Exception {
        parser = new CLIParser();
    }
}
