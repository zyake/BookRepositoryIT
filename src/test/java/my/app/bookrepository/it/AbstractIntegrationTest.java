package my.app.bookrepository.it;

import my.app.bookrepository.it.util.JdbcManager;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.sql.SQLException;
import java.util.logging.Logger;

public abstract class AbstractIntegrationTest {

    public static final String APP_URL = "http://192.168.1.109:8080/bookrepository/";

    private  static final Logger LOG = Logger.getLogger(AbstractIntegrationTest.class.getName());

    private JdbcManager jdbcManager = new JdbcManager();

    private WebDriver driver;

    @Before
    public void setup() throws SQLException {
        jdbcManager.open();
        System.setProperty("webdriver.chrome.driver","resource/win/chromedriver.exe");
        driver = new ChromeDriver();
    }

    @After
    public void after() throws SQLException {
        try {
            if ( jdbcManager != null ) {
                jdbcManager.close();
            }
        } catch (Exception ex){
            System.err.println(ex);
        }

        if ( driver != null ) {
            driver.quit();
        }
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void setupDefaultData() {
        getJdbcManager().replaceData("Publishers",
                new String[]{"Name"},
                new String[][]{
                        {"'Publisher1'"}, {"'Publisher2'"}, {"'Publisher3'"}
                });

        getJdbcManager().replaceData("Ranks",
                new String[]{"Rank"},
                new String[][]{
                        {"'Rank1'"}, {"'Rank2'"}, {"'Rank3'"}
                });

        getJdbcManager().replaceData("Genres",
                new String[]{"Genre"},
                new String[][]{
                        {"'Genre1'"}, {"'Genre2'"}, {"'Genre3'"}
                });

        getJdbcManager().replaceData("Books",
                new String[]{"Revision", "Name", "Url", "Publisher", "Price", "PurchaseDate", "ReadingState", "Comment", "Rank", "Genre"},
                new String[][]{
                        {"0", "'Test1'", "'Url1'", "'Publisher1'", "100", "'2013/01/05'", "100", "'Comment'", "'Rank1'", "'Genre1'"},
                        {"0", "'Test2'", "'Url2'", "'Publisher2'", "200", "'2013/02/05'", "50", "'Comment'", "'Rank2'", "'Genre2'"},
                        {"0", "'Test3'", "'Url3'", "'Publisher3'", "300", "'2013/03/05'", "0", "'Comment'", "'Rank3'", "'Genre3'"}
                });
    }

    public JdbcManager getJdbcManager() {
        return jdbcManager;
    }
}
