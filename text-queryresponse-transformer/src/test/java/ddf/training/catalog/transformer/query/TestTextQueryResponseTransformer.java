package ddf.training.catalog.transformer.query;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import ddf.catalog.data.BinaryContent;
import ddf.catalog.data.Metacard;
import ddf.catalog.data.Result;
import ddf.catalog.data.impl.BinaryContentImpl;
import ddf.catalog.data.impl.MetacardImpl;
import ddf.catalog.data.impl.ResultImpl;
import ddf.catalog.operation.SourceResponse;
import ddf.catalog.operation.impl.QueryRequestImpl;
import ddf.catalog.operation.impl.SourceResponseImpl;
import ddf.catalog.transform.CatalogTransformerException;
import ddf.catalog.transform.MetacardTransformer;

public class TestTextQueryResponseTransformer {

    @Test
    public void test() throws CatalogTransformerException, IOException {

        MetacardImpl metacard = new MetacardImpl();

        metacard.setTitle("MyTitle");
        metacard.setMetadata("<xml>sample text</xml>");
        metacard.setExpirationDate(new Date());
        metacard.setLocation("POINT (1 0)");

        MetacardImpl metacard2 = new MetacardImpl();

        metacard2.setTitle("MyTitle2");
        metacard2.setMetadata("<xml>sample text2</xml>");
        metacard2.setExpirationDate(new Date());
        metacard2.setLocation("POINT (1 0)");

        TextQueryResponseTransformer transformer = new TextQueryResponseTransformer();

        transformer.setTransformer(new MetacardTransformer() {

            public BinaryContent transform(Metacard metacard, Map<String, Serializable> arguments)
                throws CatalogTransformerException {
                return new BinaryContentImpl(new ByteArrayInputStream(
                        ("[" + metacard.getTitle() + "]").getBytes()));
            }
        });

        List<Result> results = new ArrayList<Result>();

        results.add(new ResultImpl(metacard));
        results.add(new ResultImpl(metacard2));

        SourceResponse response = new SourceResponseImpl(new QueryRequestImpl(null), results);

        BinaryContent content = transformer.transform(response, null);

        assertNotNull(content);

        String transformation = new String(content.getByteArray());

        System.out.println("Transformation: " + transformation);

      //  assertTrue(transformation.indexOf("MyTitle") != -1);
      //  assertTrue(transformation.indexOf("MyTitle2") != -1);

    }
}
