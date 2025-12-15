import com.dthvinh.Server.Utils.Builder.MusicBrainUrlBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MusicBrainUrlBuilderTest {


    @Test
    public void buildSearchArtistTest(){
        MusicBrainUrlBuilder builder = MusicBrainUrlBuilder.createBuilder();
        builder.withQueryArtistName("john");
        builder.withLimit(15);
        builder.withOffset(45);

        String searchArtist = builder.buildSearchArtist();

        Assertions.assertEquals("artist?query=john&limit=15&offset=45", searchArtist);
    }
}
