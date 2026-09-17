package dev.lucindaflores.tcbackend;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Sql("/categoriesTesting.sql")
class CategoryControllerTests {

    private static final String URL = "/categories";
    private static final String CATEGORIES_TABLE = "categories";

    private final MockMvcTester mockMvcTester;
    private final JdbcClient jdbcClient;

    @Autowired
    CategoryControllerTests(MockMvcTester mockMvcTester,
                            JdbcClient jdbcClient) {
        this.mockMvcTester = mockMvcTester;
        this.jdbcClient = jdbcClient;
    }

    /* Tests */
    @Test
    @DisplayName("GET /categories returns all categories")
    void findAllReturnsAllCategories() {
        int expectedCount =
                JdbcTestUtils.countRowsInTable(jdbcClient, CATEGORIES_TABLE);

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
    @DisplayName("GET /categories includes the category added by the test")
    void findAllContainsTestCategory1() {
        var response = mockMvcTester.get()
                .uri(URL);

        assertThat(response)
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$[*].name")
                .asList()
                .contains("Test Category 1");
    }

    @Test
    @DisplayName("GET /categories returns an empty list when no categories exist")
    void findAllWithoutCategoriesReturnsEmptyList() {
        JdbcTestUtils.deleteFromTables(
                jdbcClient,
                "order_details",
                "product_materials",
                "products",
                "categories"
        );

        var response = mockMvcTester.get()
                .uri(URL);

        assertThat(response)
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$")
                .asList()
                .isEmpty();
    }
}