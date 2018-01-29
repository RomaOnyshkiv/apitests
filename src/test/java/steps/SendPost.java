package steps;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import org.json.JSONObject;


import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class SendPost {

    private final String BASE_URL = "http://localhost:8080";
    private Response response;
    private JSONObject jsonObject;


    @When("^User set all data in request id (\\d+), first name (.*), last name (.*), age (\\d+), active (.*) and date of birth (.*)$")
    public void setAllData(int id, String firstName, String lastName, int age, final String active, String dob) throws Throwable {

        jsonObject = new JSONObject();
        jsonObject.put("id", id)
                .put("first_name", firstName)
                .put("last_name", lastName)
                .put("properties", new JSONObject()
                        .put("age", age)
                        .put("active", active.equalsIgnoreCase("y"))
                        .put("date_of_birth", dob));


    }

    @When("^User set mandatory data in request id (\\d+), first name (.*), last name (.*)$")
    public void setMandatory(int id, String firstName, String lastName) throws Throwable {

        jsonObject = new JSONObject();
        jsonObject.put("id", id)
                .put("first_name", firstName)
                .put("last_name", lastName);

    }

    @And("^Send POST request$")
    public void sendPostRequest() {
        response = given().contentType("application/json").body(jsonObject.toString())
                .post(BASE_URL + "/rest/api/customer");

    }

    @Then("^User expect to receive id (\\d), and status 'successfully created'$")
    public void successResponse(int id) {
         assertEquals(200, response.getStatusCode());
        assertEquals("successfully created", response.getBody().jsonPath().getString("status"));
        assertEquals(response.getBody().jsonPath().getInt("id"), id);
    }

    @When("^User send GET request$")
    public void user_send_GET_request() {
        response = given().contentType("application/json")
                .get(BASE_URL + "/rest/api/customer/1");
    }

    @Then("^User expect to see a customer$")
    public void user_expect_to_see_a_customer() {
        assertEquals(200, response.getStatusCode());
        assertEquals("Roman", response.jsonPath().get("first_name"));
        assertEquals("Onyshkiv", response.jsonPath().getString("last_name"));

    }

    @When("^User send GET request with not existing id$")
    public void user_send_GET_request_with_not_existing_id() {
        response = given().contentType("application/json")
                .get(BASE_URL + "/rest/api/customer/3");
    }

    @Then("^User expect to receive 404 error$")
    public void user_expect_to_receive_en_error() {
        assertEquals(404, response.getStatusCode());
    }

    @When("^User send GET for customer with mandatory fields only")
    public void getForCustomerWithMandatoryFieldsOnly() {
        response = given().contentType("application/json")
                .get(BASE_URL + "/rest/api/customer/2");
    }

    @Then("^User expect to see an existing customer")
    public void existingCustomerWithMandatoryFields() {
        assertEquals(200, response.getStatusCode());
        assertEquals(2, response.jsonPath().getInt("id"));
        assertEquals("Roman", response.jsonPath().getString("first_name"));
        assertEquals("Onyshkiv", response.jsonPath().getString("last_name"));
    }



}
