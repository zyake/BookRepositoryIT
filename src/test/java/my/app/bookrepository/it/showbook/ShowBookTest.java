package my.app.bookrepository.it.showbook;

import my.app.bookrepository.it.AbstractIntegrationTest;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ShowBookTest extends AbstractIntegrationTest {

    @Test
    public void testShowBook_normal_showCollectionTable() throws Exception {
        // init
        setupDefaultData();

        // test
        getDriver().get(APP_URL);
        TimeUnit.SECONDS.sleep(3);

        // assert
        WebElement tbody = getDriver().findElement(By.tagName("TBODY"));
        assertThat(tbody.getText(),
                is("1 Test1 Publisher1 2013-01-05 Genre1 Rank1 100% modify\n" +
                    "2 Test2 Publisher2 2013-02-05 Genre2 Rank2 50% modify\n" +
                    "3 Test3 Publisher3 2013-03-05 Genre3 Rank3 0% modify"));
    }

    @Test
    public void testShowBook_normal_showPager() throws Exception {
        // init
        setupDefaultData();

        // test
        getDriver().get(APP_URL);
        TimeUnit.SECONDS.sleep(3);

        // assert
        WebElement pagerState = getDriver().findElement(By.cssSelector(".pagerState"));
        assertThat(pagerState.getText(), is("1/1"));
    }

    @Test
    public void testShowBook_normal_showBookInfo() throws Exception {
        // init
        setupDefaultData();

        // test
        getDriver().get(APP_URL);
        TimeUnit.SECONDS.sleep(3);

        WebElement showBookInfoAnchor = getDriver().findElement(By.cssSelector("tbody tr td a"));
        showBookInfoAnchor.click();
        TimeUnit.SECONDS.sleep(3);

        // assert
        System.out.println(getDriver().findElement(By.tagName("body")).getText());
        WebElement dialog = getDriver().findElement(By.cssSelector(".dialog"));
        assertThat(dialog.getText(),
                is("Book Info\n" +
                   "Book Name: Test1\n" +
                   "Publisher: Publisher1\n" +
                   "Purchase Date: 2013-01-05\n" +
                   "Reading State: 100%\n" +
                   "Comment: Comment\n" +
                   "Rank: Rank1\n" +
                   "Genre: Genre1\n" +
                   "close"));
    }

}
