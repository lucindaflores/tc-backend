package dev.lucindaflores.tcbackend;

import jakarta.persistence.EntityManager;
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
@Sql({"/materialsTesting.sql",
       "/categoriesTesting.sql",
       "/originsTesting.sql",
       "/productsTesting.sql",
       "/productMaterials.sql"})
@AutoConfigureMockMvc
class ProductControllerTest {

    private final static String URL = "/products";

    private final static String PRODUCTS_TABLE = "products";
    private final MockMvcTester mockMvcTester;

    private final JdbcClient jdbcClient;

    @Autowired
    ProductControllerTest(MockMvcTester mockMvcTester, JdbcClient jdbcClient) {
        this.mockMvcTester = mockMvcTester;
        this.jdbcClient = jdbcClient;
    }

    /* Helper methods */
    // If the schema cannot be selected:
    // FIX: Settings → Languages & Frameworks → SQL Resolution Scopes (top dropdown)
    private int idOfTestProduct1() {
        return jdbcClient.sql("select id from products where name = 'Test Product 1'")
                .query(Integer.class)
                .single();
    }

    /* Tests */
    @Test
    @DisplayName("GET /products returns all products")
    void findAllReturnsAllProducts() {
        var expectedNumberOfProducts =
                JdbcTestUtils.countRowsInTable(jdbcClient, PRODUCTS_TABLE);

        var response = mockMvcTester.get()
                .uri(URL);

        assertThat(response)
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$[*].name")
                .asList()
                .hasSize(expectedNumberOfProducts);
    }


    @Test
    @DisplayName("GET /products/{id} returns the correct product")
    void findByIdReturnsTestProduct1() {
        var productId = idOfTestProduct1();

        var response = mockMvcTester.get()
                .uri(URL + "/" + productId);

        assertThat(response)
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.name")
                .isEqualTo("Test Product 1");
    }


    @Test
    @DisplayName("GET /products/{id} with unknown ID returns 404")
    void findByIdWithUnknownIdReturnsNotFound() {
        var response = mockMvcTester.get()
                .uri(URL + "/9999");

        assertThat(response)
                .hasStatus(HttpStatus.NOT_FOUND);
    }


    @Test
    @DisplayName("GET /products/count returns the number of products")
    void findCountReturnsTheNumberOfProducts() {
        var expectedNumberOfProducts =
                JdbcTestUtils.countRowsInTable(jdbcClient, PRODUCTS_TABLE);

        var response = mockMvcTester.get()
                .uri(URL + "/count");

        assertThat(response)
                .hasStatusOk()
                .bodyText()
                .isEqualTo(String.valueOf(expectedNumberOfProducts));
    }


    @Test
    @DisplayName("GET /products/names returns product names")
    void findAllNamesReturnsProductNames() {
        var response = mockMvcTester.get()
                .uri(URL + "/names");

        assertThat(response)
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$[*].name")
                .asList()
                .contains("Test Product 1");
    }


    @Test
    @DisplayName("GET /products?categoryId returns products from that category")
    void findByCategoryIdReturnsProductsOfThatCategory() {
        var categoryId = jdbcClient.sql("select id from categories where name = 'Test Category 1'")
                .query(Integer.class)
                .single();

        var response = mockMvcTester.get()
                .uri(URL + "?categoryId=" + categoryId);

        assertThat(response)
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$[*].name")
                .asList()
                .contains("Test Product 1");
    }


    @Test
    @DisplayName("GET /products?materialId returns products with that material")
    void findByMaterialIdReturnsProductsWithThatMaterial() {
        var materialId = jdbcClient.sql("select id from materials where name = 'Test Material 1'")
                .query(Integer.class)
                .single();

        var response = mockMvcTester.get()
                .uri(URL + "?materialId=" + materialId);

        assertThat(response)
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$[*].name")
                .asList()
                .contains("Test Product 1");
    }


    @Test
    @DisplayName("GET /products?originId returns products from that origin")
    void findByOriginIdReturnsProductsOfThatOrigin() {
        var originId = jdbcClient.sql("select id from origins where name = 'Test Origin 1'")
                .query(Integer.class)
                .single();

        var response = mockMvcTester.get()
                .uri(URL + "?originId=" + originId);

        assertThat(response)
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$[*].name")
                .asList()
                .contains("Test Product 1");
    }
}