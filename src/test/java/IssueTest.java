import githubPage.model.Constants;
import githubPage.model.Issue;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.restassured.AllureRestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.TestDataReader;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.parameter;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Owner("evgdas")
@Feature("Работа с задачами")
public class IssueTest extends BaseTest {
    private int issueNumber;
    private Issue issue = new Issue();

    @Test
    @DisplayName("Создание новой задачи и проверка через API")
    public void shouldCreateNewIssue() {
        parameter("Репозиторий", Constants.REPOSITORY);
        parameter("Заголовок Issue", Constants.ISSUE_TITLE);

        step("Логин на github", () -> {
            open("https://github.com");
            $(byText("Sign in")).click();
            $("#login_field").setValue(TestDataReader.getLoginFromProperty());
            $("#password").setValue(TestDataReader.getPasswordFromProperty());
            $("input[name='commit']").click();
        });
        step("Открываем главную страницу репозитария", () -> {
            open("https://github.com" + Constants.REPOSITORY);
        });
        step("Открываем страницу с задачами", () -> {
            $("a[href='" + Constants.REPOSITORY + "issues']").click();
        });
        step("Создаем новую задачу", () -> {
            $(byText("New issue'")).click();
            $("#issue_title").click();
            $("#issue_title").setValue(Constants.ISSUE_TITLE);
            $("#labels-select-menu").click();
            $(byText(Constants.LABEL)).click();
            $("#labels-select-menu").click();
            $(byText("Submit new issue")).click();

            //Получить номер созданной Issue:
            issueNumber = Integer.parseInt(($x("//span[contains(text(),'#')]").getText().replace("#", "")));
            parameter("Issue", issueNumber);
        });
        step("Проверяем задачу через API", () -> {
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
            assertThat(issue.getTitle(), equalTo(Constants.ISSUE_TITLE));
            assertThat(issue.getLabels().get(0).getName(), equalTo(Constants.LABEL));
        });
    }
}
