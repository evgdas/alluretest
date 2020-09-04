import githubPage.BasicSteps;
import githubPage.model.Constants;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.TestDataReader;

@Owner("evgdas")
@Feature("Работа с задачами")
public class IssueStepsTest extends BaseTest {
    private final BasicSteps steps = new BasicSteps();

    @Test
    @DisplayName("Создание и проверка задачи используя шаги")
    public void shouldCreateNewIssueBySteps() {
        steps
                .loginGithub(TestDataReader.getLoginFromProperty(),
                        TestDataReader.getPasswordFromProperty())
                .openRepository(Constants.REPOSITORY)
                .openIssuePage(Constants.REPOSITORY)
                .createNewIssue(Constants.REPOSITORY, Constants.ISSUE_TITLE, Constants.LABEL)
                .checkIssueByApi(Constants.ISSUE_TITLE, Constants.LABEL);
    }
}
