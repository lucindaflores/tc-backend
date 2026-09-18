package dev.lucindaflores.tcbackend;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Sql("/usersTesting.sql")
@AutoConfigureMockMvc
class UserControllerTests {

    private final static String URL = "/users";

    private final static String USERS_TABLE = "users";
    private final MockMvcTester mockMvcTester;

    private final JdbcClient jdbcClient;
    private final EntityManager entityManager;

    @Autowired
    UserControllerTests(MockMvcTester mockMvcTester, JdbcClient jdbcClient, EntityManager entityManager) {
        this.mockMvcTester = mockMvcTester;
        this.jdbcClient = jdbcClient;
        this.entityManager = entityManager;
    }

    /* Helper methods */
    private int idVanTestUser1() {
        return jdbcClient.sql("select id from users where email = 'email1@test.com'")
                .query(Integer.class)
                .single();
    }

    @Test
    @DisplayName("GET /users/{id} returns the correct user")
    void findByIdReturnsCorrectUser() {
        var userId = idVanTestUser1();

        var response = mockMvcTester.get()
                .uri(URL + "/" + userId);

        assertThat(response)
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.email")
                .isEqualTo("email1@test.com");
    }

    @Test
    @DisplayName("GET /users/{id} with unknown ID returns 404")
    void findByIdWithUnknownIdReturnsNotFound() {
        var response = mockMvcTester.get()
                .uri(URL + Long.MAX_VALUE);

        assertThat(response)
                .hasStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("GET /users?email returns the correct user")
    void findByEmailReturnsCorrectUser() {
        var response = mockMvcTester.get()
                .uri(URL + "?email=email1@test.com");

        assertThat(response)
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.email")
                .isEqualTo("email1@test.com");
    }

    @Test
    @DisplayName("GET /users?email with unknown email returns 404")
    void findByEmailWithUnknownEmailReturnsNotFound() {
        var response = mockMvcTester.get()
                .uri(URL + "?email=unknown@test.com");

        assertThat(response)
                .hasStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("POST /users creates a valid user")
    void createAddsUser() throws Exception {
        var jsonData = new ClassPathResource("jsonTesting/user/userCorrect.json")
                .getContentAsString(StandardCharsets.UTF_8);

        var numberOfUsersBefore = JdbcTestUtils.countRowsInTable(jdbcClient, USERS_TABLE);
        IO.println("numberOfUsersBefore: " + numberOfUsersBefore);

       mockMvcTester.post()
                .uri(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData)
                .assertThat()
                .hasStatusOk();

        entityManager.flush();

        var userId = jdbcClient.sql("""
                select count(*)
                from users
                where email = 'email@test.com'
                """)
                .query(Integer.class)
                .single();

        assertThat(userId).isEqualTo(1);
    }

    @ParameterizedTest(name = "Invalid user data")
    @ValueSource(strings = {
            "userWithEmptyEmail.json",
            "userWithEmptyFirstName.json",
            "userWithEmptyLastName.json",
            "userWithInvalidEmailNoAt.json",
            "userWithoutEmail.json",
            "userWithoutFirstName.json",
            "userWithoutLastName.json"
    })
    @DisplayName("POST /users with invalid data returns 400")
    void createWithWrongDataFails(String fileName) throws Exception {
        var jsonData = new ClassPathResource("jsonTesting/user/" + fileName)
                .getContentAsString(StandardCharsets.UTF_8);

        var numberOfUsersBefore =
                JdbcTestUtils.countRowsInTable(jdbcClient, USERS_TABLE);

        var response = mockMvcTester.post()
                .uri(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData);

        assertThat(response)
                .hasStatus(HttpStatus.BAD_REQUEST);

        assertThat(JdbcTestUtils.countRowsInTable(jdbcClient, USERS_TABLE))
                .isEqualTo(numberOfUsersBefore);
    }

}
