package ddf.training.catalog.transformer.metacard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import ddf.catalog.data.BinaryContent;
import ddf.catalog.data.impl.MetacardImpl;
import ddf.catalog.transform.CatalogTransformerException;
import ddf.catalog.transform.MetacardTransformer;

public class TestTextMetacardTransformer {

    @Test
    public void test() throws CatalogTransformerException, IOException {
        MetacardImpl metacard = new MetacardImpl();

        metacard.setTitle("MyTitle");
        metacard.setMetadata("<xml>sample text</xml>");
        metacard.setExpirationDate(new Date());
        metacard.setLocation("POINT (1 0)");

        MetacardTransformer transformer = new TextMetacardTransformer();

        BinaryContent content = transformer.transform(metacard, null);

        String output = IOUtils.toString(content.getInputStream());

        System.out.println(output);

        assertNotNull(output);

        // assertTrue(output.length() > 0);

        assertEquals(content.getMimeTypeValue(), "text/plain");
    }
}
