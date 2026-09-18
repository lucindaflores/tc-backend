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
@Sql({"/usersTesting.sql", "/addressesTesting.sql"})
@AutoConfigureMockMvc
class AddressControllerTest {

    private final static String URL = "/addresses";

    private final static String ADDRESSES_TABLE = "addresses";
    private final MockMvcTester mockMvcTester;

    private final JdbcClient jdbcClient;
    private final EntityManager entityManager;

    @Autowired
    AddressControllerTest(MockMvcTester mockMvcTester, JdbcClient jdbcClient, EntityManager entityManager) {
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

    private int idVanTestAddress1() {
        return jdbcClient.sql("select id from addresses where user_id = " + idVanTestUser1())
                .query(Integer.class)
                .single();
    }

    @Test
    @DisplayName("GET /addresses/{id} returns the correct address")
    void findByIdReturnsCorrectAddress() {
        var addressId = idVanTestAddress1();

        var response = mockMvcTester.get()
                .uri(URL + "/" + addressId);

        assertThat(response)
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.street")
                .isEqualTo("Test street 1");
    }

    @Test
    @DisplayName("GET /addresses/{id} with unknown ID returns 404")
    void findByIdWithUnknownIdReturnsNotFound() {
        var response = mockMvcTester.get()
                .uri(URL + Long.MAX_VALUE);

        assertThat(response)
                .hasStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("GET /addresses?userId returns addresses for the user")
    void findByUserIdReturnsAddressesForUser() {
        var userId = idVanTestUser1();

        var response = mockMvcTester.get()
                .uri(URL + "?userId=" + userId);

        assertThat(response)
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$[*].userId")
                .asList()
                .containsOnly(userId);
    }

    @Test
    @DisplayName("GET /addresses?userId with no addresses returns an empty list")
    void findByUserIdWithoutAddressesReturnsEmptyList() {
        var response = mockMvcTester.get()
                .uri(URL + "?userId=" + Long.MAX_VALUE) ;

        assertThat(response)
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$")
                .asList()
                .isEmpty();
    }


    // POST http://localhost:8080/addresses?userId={{userId}}
    @Test
    @DisplayName("POST /addresses creates a valid address")
    void createAddsAddress() throws Exception {
        // Posting user data
        var jsonDataUser = new ClassPathResource("jsonTesting/user/userCorrect.json")
                .getContentAsString(StandardCharsets.UTF_8);

        var userResponse = mockMvcTester.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDataUser);

        var userId = userResponse
                .assertThat()
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$")
                .convertTo(Integer.class)
                .actual();

        entityManager.flush();

        // Posting address data
        var jsonData = new ClassPathResource("jsonTesting/address/addressCorrect.json")
                .getContentAsString(StandardCharsets.UTF_8);

        var response = mockMvcTester.post()
                .uri(URL + "?userId=" + userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData);

        assertThat(response)
                .hasStatusOk();

        entityManager.flush();

        assertThat(response)
                .bodyJson()
                .extractingPath("$")
                .satisfies(newId ->
                        assertThat(JdbcTestUtils.countRowsInTableWhere(
                                jdbcClient,
                                ADDRESSES_TABLE,
                                "user_id = " + userId))
                                .isOne());
    }

    @Test
    @DisplayName("POST /addresses with unknown user returns 404")
    void createWithUnknownUserReturnsNotFound() throws Exception {
        var jsonData = new ClassPathResource("jsonTesting/address/addressCorrect.json")
                .getContentAsString(StandardCharsets.UTF_8);

        var response = mockMvcTester.post()
                .uri(URL + "?userId=" + Long.MAX_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData);

        assertThat(response)
                .hasStatus(HttpStatus.NOT_FOUND);
    }

    @ParameterizedTest(name = "Invalid address data")
    @ValueSource(strings = {
            "addressWithEmptyStreet.json",
            "addressWithEmptyHouseNumber.json",
            "addressWithEmptyCity.json",
            "addressWithEmptyPostalCode.json",
            "addressWithEmptyCountry.json",
            "addressWithoutStreet.json",
            "addressWithoutHouseNumber.json",
            "addressWithoutCity.json",
            "addressWithoutPostalCode.json",
            "addressWithoutCountry.json"
    })
    @DisplayName("POST /addresses with invalid data returns 400")
    void createWithWrongDataFails(String fileName) throws Exception {
        var jsonData = new ClassPathResource("jsonTesting/address/" + fileName)
                .getContentAsString(StandardCharsets.UTF_8);

        var userId = idVanTestUser1();
        var numberOfAddressesBefore =
                JdbcTestUtils.countRowsInTable(jdbcClient, ADDRESSES_TABLE);

        var response = mockMvcTester.post()
                .uri(URL + "?userId=" + userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData);

        assertThat(response)
                .hasStatus(HttpStatus.BAD_REQUEST);

        assertThat(JdbcTestUtils.countRowsInTable(jdbcClient, ADDRESSES_TABLE))
                .isEqualTo(numberOfAddressesBefore);
    }
}
