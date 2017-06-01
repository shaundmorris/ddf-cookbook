package ddf.training.catalog.transformer.metacard;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.Map;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ddf.catalog.data.BinaryContent;
import ddf.catalog.data.Metacard;
import ddf.catalog.data.impl.BinaryContentImpl;
import ddf.catalog.transform.CatalogTransformerException;
import ddf.catalog.transform.MetacardTransformer;

public class TextMetacardTransformer implements MetacardTransformer {

    private static final String MIME_TYPE_TEXT = "text/plain";

    private static final Logger LOGGER = LoggerFactory.getLogger(TextMetacardTransformer.class);

    public BinaryContent transform(Metacard metacard, Map<String, Serializable> arguments)
        throws CatalogTransformerException {

        if (metacard == null || metacard.getMetacardType() == null) {
            return null;
        }

        try {
            return new BinaryContentImpl(new ByteArrayInputStream(getText(metacard).getBytes()),
                    new MimeType(MIME_TYPE_TEXT));
        } catch (MimeTypeParseException e) {
            LOGGER.error("Failed to parse mime type", e);
            return null;
        }
    }

    /**
     * Converts Metacard into plain text.
     * 
     * @param metacard
     *            a {@link Metacard} instance
     * @return text - a plain text version of the {@link Metacard} instance
     */
    private String getText(Metacard metacard) {

        /*
         * INSERT YOUR IMPLEMENTATION HERE
         */

        return "";
    }
}
