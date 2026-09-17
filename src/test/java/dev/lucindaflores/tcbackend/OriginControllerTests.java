package dev.lucindaflores.tcbackend;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Sql("/originsTesting.sql")
class OriginControllerTests {

    private static final String URL = "/origins";
    private static final String ORIGINS_TABLE = "origins";

    private final MockMvcTester mockMvcTester;
    private final JdbcClient jdbcClient;

    @Autowired
    OriginControllerTests(MockMvcTester mockMvcTester,
                          JdbcClient jdbcClient) {
        this.mockMvcTester = mockMvcTester;
        this.jdbcClient = jdbcClient;
    }

    /* Helpers */
    // If the schema cannot be selected:
    // FIX: Settings → Languages & Frameworks → SQL Resolution Scopes (top dropdown)
    private long idOfTestOrigin1() {
        return jdbcClient.sql("""
                SELECT id
                FROM origins
                WHERE name = 'Test Origin 1'
                """)
                .query(Long.class)
                .single();
    }

    /* Tests */
    @Test
    @DisplayName("GET /origins returns the count of all origins")
    void findAllReturnsAllOrigins() {
        int expectedCount =
                JdbcTestUtils.countRowsInTable(jdbcClient, ORIGINS_TABLE);

        var response = mockMvcTester.get()
                .uri(URL);

        assertThat(response)
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$[*].name")
                .asList()
                .hasSize(expectedCount);
    }

    @Test
    @DisplayName("GET /origins/{id} returns the correct origin")
    void findByIdReturnsTestOrigin1() {
        var originId = idOfTestOrigin1();

        var response = mockMvcTester.get()
                .uri(URL + "/" + originId);

        assertThat(response)
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.name")
                .isEqualTo("Test Origin 1");
    }

    @Test
    @DisplayName("GET /origins/{id} with unknown ID returns 404")
    void findByIdWithUnknownIdReturnsNotFound() {
        var response = mockMvcTester.get()
                .uri(URL + "/999999");

        assertThat(response)
                .hasStatus(HttpStatus.NOT_FOUND);
    }

}