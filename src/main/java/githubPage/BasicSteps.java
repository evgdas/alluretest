package githubPage;

import githubPage.model.Issue;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import utils.TestDataReader;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$x;
import static io.qameta.allure.Allure.parameter;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class BasicSteps {
    private int issueNumber;

    @Step("Логин на github")
    public BasicSteps loginGithub(String login, String password) {
        open("https://github.com");
        $(byText("Sign in")).click();
        $("#login_field").setValue(login);
        $("#password").setValue(password);
        $("input[name='commit']").click();
        return this;
    }

    @Step("Открываем главную страницу репозитария")
    public BasicSteps openRepository(String repository) {
        open("https://github.com" + repository);
        return this;
    }

    @Step("Открываем страницу с задачами")
    public BasicSteps openIssuePage(String repository) {
        $("a[href='" + repository + "issues']").click();
        return this;
    }

    @Step("Создаем новую задачу")
    public BasicSteps createNewIssue(String repository, String issueTitle, String label) {
        parameter("Репозиторий", repository);
        parameter("Заголовок Issue", issueTitle);

        $(byText("New issue")).click();
        $("#issue_title").click();
        $("#issue_title").setValue(issueTitle);
        $("#labels-select-menu").click();
        $(byText(label)).click();
        $("#labels-select-menu").click();
        $(byText("Submit new issue")).click();
        issueNumber = Integer.parseInt($x("//span[contains(text(),'#')]").getText().replace("#", ""));

        parameter("Issue Number", issueNumber);
        closeWebDriver();
        return this;
    }

    @Step("Проверяем задачу через API")
    public void checkIssueByApi(String issueTitle, String label) {
        Issue issue = new Issue();
        issue = given()
                .filter(new AllureRestAssured())
                .header("Authorization", TestDataReader.getTokenFromProperty())
                .baseUri("https://api.github.com")
                .body("")
                .when()
                .get("/repos/evgdas/qaguru/issues/" + issueNumber)
                .then()
                .statusCode(200)
                .extract()
                .as(Issue.class);
        assertThat(issue.getTitle(), equalTo(issueTitle));
        assertThat(issue.getLabels().get(0).getName(), equalTo(label));
    }
}
