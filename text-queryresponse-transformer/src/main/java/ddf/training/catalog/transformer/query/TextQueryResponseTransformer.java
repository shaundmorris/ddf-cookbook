package ddf.training.catalog.transformer.query;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.Map;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;

import ddf.catalog.data.BinaryContent;
import ddf.catalog.data.impl.BinaryContentImpl;
import ddf.catalog.operation.SourceResponse;
import ddf.catalog.transform.CatalogTransformerException;
import ddf.catalog.transform.MetacardTransformer;
import ddf.catalog.transform.QueryResponseTransformer;

public class TextQueryResponseTransformer implements QueryResponseTransformer {

    private static final String MIME_TYPE_TEXT = "text/plain";

    private MetacardTransformer transformer;

    public MetacardTransformer getTransformer() {
        return transformer;
    }

    public void setTransformer(MetacardTransformer transformer) {
        this.transformer = transformer;
    }

    public BinaryContent transform(SourceResponse upstreamResponse,
            Map<String, Serializable> arguments) throws CatalogTransformerException {

        String text = getText(upstreamResponse);

        try {
            return new BinaryContentImpl(new ByteArrayInputStream(text.getBytes()), new MimeType(
                    MIME_TYPE_TEXT));
        } catch (MimeTypeParseException e) {
            throw new CatalogTransformerException(e);
        }
    }

    private String getText(SourceResponse upstreamResponse) {

        /*
         * INSERT YOUR IMPLEMENTATION HERE
         */

        return "";
    }
}
