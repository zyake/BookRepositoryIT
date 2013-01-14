package my.app.bookrepository.it.showbook;

import my.app.bookrepository.it.AbstractIntegrationTest;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RegisterBookTest extends AbstractIntegrationTest {

    @Test
    public void testRegister_normal() throws InterruptedException {
        // init
        setupDefaultData();

        // test
        getDriver().get(APP_URL);
        TimeUnit.SECONDS.sleep(3);

        WebElement registerBook = getDriver().findElement(By.cssSelector("nav a"));
        registerBook.click();
        TimeUnit.SECONDS.sleep(3);

        WebElement dialog = getDriver().findElement(By.cssSelector(".dialog"));
        dialog.findElement(By.name("name")).sendKeys("NEW_BOOK");
        dialog.findElement(By.name("url")).sendKeys("NEW_URL");
        dialog.findElement(By.name("purchaseDate")).sendKeys("2012-12-12");
        dialog.findElement(By.cssSelector(".submit")).click();
        TimeUnit.SECONDS.sleep(3);

        String books = getJdbcManager().getTableAsCSV("Books", "1=1 ORDER BY No");
        System.out.println(books);

        // assert
        assertThat(books,
                is("1,0,Test1,Url1,Publisher1,100,2013-01-05,100,Comment,Rank1,Genre1\r\n" +
                    "2,0,Test2,Url2,Publisher2,200,2013-02-05,50,Comment,Rank2,Genre2\r\n" +
                    "3,0,Test3,Url3,Publisher3,300,2013-03-05,0,Comment,Rank3,Genre3\r\n" +
                    "4,0,NEW_BOOK,NEW_URL,Publisher1,0,2012-12-12,0,,Rank1,Genre1\r\n"));
    }
}
