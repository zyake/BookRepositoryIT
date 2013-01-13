package my.app.bookrepository.it.showbook;

import my.app.bookrepository.it.AbstractIntegrationTest;
import org.junit.Test;

public class ShowBookTest extends AbstractIntegrationTest {

    @Test
    public void testShowBook_normal() {
        // init
        replaceData("Publishers",
            new String[]{"Name"},
            new String[][] {
                {"'Publisher1'"}, {"'Publisher2'"}, {"'Publisher3'"}
            });

        replaceData("Ranks",
                new String[]{"Rank"},
                new String[][] {
                        {"'Rank1'"}, {"'Rank2'"}, {"'Rank3'"}
         });


        replaceData("Genres",
                new String[]{"Genre"},
                new String[][] {
                        {"'Genre1'"}, {"'Genre2'"}, {"'Genre3'"}
        });

        replaceData("Books",
            new String[] {"Revision", "Name", "Url", "Publisher", "Price", "PurchaseDate", "ReadingState", "Comment", "Rank", "Genre"},
            new String[][] {
                    {"0", "'Test1'", "'Url1'", "'Publisher1'", "100", "'2013/02/05'", "100", "'Comment'", "'Rank1'", "'Genre1'"}
         });
    }
}
