package dev.lucinda.tcbackend;

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
@Sql("/materialsTesting.sql")
class MaterialsControllerTests {

    private final static String URL= "/materials";
    private final static String MATERIALS_TABLE = "materials";

    private final MockMvcTester mockMvcTester;

    private final JdbcClient jdbcClient;

    @Autowired
    MaterialsControllerTests(MockMvcTester mockMvcTester, JdbcClient jdbcClient) {
        this.mockMvcTester = mockMvcTester;
        this.jdbcClient = jdbcClient;
    }

    /* Helper methods */

    private long idOfTestMaterial1() {
        return jdbcClient.sql("""
                SELECT id
                FROM materials
                WHERE name = 'Test Material 1'
                """)
                .query(Long.class)
                .single();
    }


    /* Tests */
    @Test
    @DisplayName("GET /materials returns all materials")
    void findAllReturnsAllMaterials() {
        int expectedCount = JdbcTestUtils.countRowsInTable(jdbcClient, MATERIALS_TABLE);

        var response = mockMvcTester.get().uri(URL);

        assertThat(response)
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.length()")
                .isEqualTo(expectedCount);
    }

    @Test
    @DisplayName("GET /materials/{id} returns the requested material")
    void findByIdReturnsMaterial() {
        long materialId = idOfTestMaterial1();

        var response = mockMvcTester.get()
                .uri(URL + "/" + materialId);

        assertThat(response)
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.name")
                .isEqualTo("Test Material 1");
    }

    @Test
    @DisplayName("GET /materials/{id} with unknown ID returns 404")
    void findByIdWithUnknownIdReturnsNotFound() {
        var response = mockMvcTester.get()
                .uri(URL + "/999999");

        assertThat(response)
                .hasStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("GET /materials?name returns the requested material")
    void findByNameReturnsMaterial() {
        var response = mockMvcTester.get().uri("/materials")
                .queryParam("name", "Test Material 1");
        
        assertThat(response)
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.name")
                .isEqualTo("Test Material 1");
    }

    @Test
    @DisplayName("GET /materials?name with unknown name returns 404")
    void findByNameWithUnknownNameReturnsNotFound() {
        var response = mockMvcTester.get().uri("/materials")
                .queryParam("name", "Unknown Material");

        assertThat(response)
                .hasStatus(HttpStatus.NOT_FOUND);
    }
}
