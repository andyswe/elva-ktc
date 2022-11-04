import com.github.andy.elva.model.Measurement;
import io.github.andyswe.elva.data.reader.Reader;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

public class ReaderTest {


    @Test
    public void shouldReadAndParseData() {
        Reader target = new Reader();

        Measurement result = target.obtain();

        assertThat(result.getEl(), greaterThan(10065199));
        assertThat(result.getPower(), greaterThan(0));
        assertThat(result.getVa(), greaterThan(79678));

    }
}
