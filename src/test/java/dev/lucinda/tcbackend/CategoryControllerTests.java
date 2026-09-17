package dev.lucinda.tcbackend;

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

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Sql("/categoriesTesting.sql")
class CategoryControllerTests {

    private static String URL= "/categories";
    private final static String CATEGORIES_TABLE = "categories";

    private final MockMvcTester mockMvcTester;

    private final JdbcClient jdbcClient;

    @Autowired
    CategoryControllerTests(MockMvcTester mockMvcTester, JdbcClient jdbcClient) {
        this.mockMvcTester = mockMvcTester;
        this.jdbcClient = jdbcClient;
    }


    /* Tests */
    @Test
    @DisplayName("GET /categories the count of rows in the table")
    void findAllReturnsAllCategories() {
        int expectedCount = JdbcTestUtils.countRowsInTable(jdbcClient, CATEGORIES_TABLE);

        mockMvcTester.get()
                .uri(URL)
                .assertThat()
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$[*].name")
                .asList()
                .hasSize(expectedCount);
    }

    @Test
    @DisplayName("GET /categories includes the category added by the test")
    void findAllContainsTestCategory1() {
        mockMvcTester.get()
                .uri(URL)
                .assertThat()
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$[*].name")
                .asList()
                .contains("Category1");
    }

    @Test
    @DisplayName("Find all returns an empty list when no categories exist")
    void findAllWithoutCategoriesReturnsEmptyList() {
        JdbcTestUtils.deleteFromTables(jdbcClient,  "order_details",
                                                                "product_materials",
                                                                "products",
                                                                "categories"
        );

        mockMvcTester.get()
                .uri(URL)
                .assertThat()
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$")
                .asList()
                .isEmpty();
    }

}
