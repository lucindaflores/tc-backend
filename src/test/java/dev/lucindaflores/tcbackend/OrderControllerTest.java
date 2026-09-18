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
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@Transactional
@Sql({"/categoriesTesting.sql",
        "/originsTesting.sql",
        "/materialsTesting.sql",
        "/productsTesting.sql",
        "/usersTesting.sql",
        "/addressesTesting.sql",
        "/ordersTesting.sql",
        "/orderDetailsTesting.sql"})
@AutoConfigureMockMvc
class OrderControllerTest {

    private final static String URL = "/orders";

    private final static String ORDERS_TABLE = "orders";
    private final MockMvcTester mockMvcTester;

    private final JdbcClient jdbcClient;
    private final EntityManager entityManager;

    @Autowired
    OrderControllerTest(MockMvcTester mockMvcTester, JdbcClient jdbcClient, EntityManager entityManager) {
        this.mockMvcTester = mockMvcTester;
        this.jdbcClient = jdbcClient;
        this.entityManager = entityManager;
    }

    /* Helper methods */
    // If the schema cannot be selected:
    // FIX: Settings → Languages & Frameworks → SQL Resolution Scopes (top dropdown)
    private int idVanTestUser1() {
        return  jdbcClient.sql("""
                        select id from users where email = 'email1@test.com'
                        """)
                .query(Integer.class)
                .single();
    }

    private int idVanTestOrder1() {
        return jdbcClient.sql("select id from orders where user_id = " + idVanTestUser1())
                .query(Integer.class)
                .single();
    }

    /* Tests */
    @Test
    @DisplayName("GET /orders/{id} returns the correct order with details")
    void findByIdReturnsCorrectOrder() {
        var orderId = idVanTestOrder1();
        IO.println("orderId:" + orderId);

        var response = mockMvcTester.get()
                .uri(URL + "/" + orderId);

        assertThat(response)
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.id")
                .isEqualTo(orderId);

        assertThat(response)
                .bodyJson()
                .extractingPath("$.orderDetails")
                .asList()
                .isNotEmpty();
    }


    @Test
    @DisplayName("GET /orders/{id} with unknown ID returns 404")
    void findByIdWithUnknownIdReturnsNotFound() {
        var response = mockMvcTester.get()
                .uri(URL + "/" + Long.MAX_VALUE);

        assertThat(response)
                .hasStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("GET /orders?userId returns orders for the user")
    void findByUserIdReturnsOrdersForUser() {
        var userId = idVanTestUser1();

        var response = mockMvcTester.get()
                .uri(URL + "?userId=" + userId);

        assertThat(response)
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$[*].customerId")
                .asList()
                .containsOnly(userId);
    }

    @Test
    @DisplayName("GET /orders?userId with unknown user returns 404")
    void findByUserIdWithUnknownUserReturnsNotFound() {
        var response = mockMvcTester.get()
                .uri(URL + "?userId=" + Long.MAX_VALUE);

        assertThat(response)
                .hasStatus(HttpStatus.NOT_FOUND);
    }

}
