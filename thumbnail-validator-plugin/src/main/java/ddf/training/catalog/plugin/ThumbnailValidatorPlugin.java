package ddf.training.catalog.plugin;

import java.io.Serializable;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ddf.catalog.data.Metacard;
import ddf.catalog.operation.CreateRequest;
import ddf.catalog.operation.DeleteRequest;
import ddf.catalog.operation.UpdateRequest;
import ddf.catalog.plugin.PluginExecutionException;
import ddf.catalog.plugin.PreIngestPlugin;
import ddf.catalog.plugin.StopProcessingException;

/**
 * Validates that the thumbnail in a given {@link Metacard} is below a threshold amount.
 */
public class ThumbnailValidatorPlugin implements PreIngestPlugin {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThumbnailValidatorPlugin.class);

    private static final String BEYOND_THRESHOLD_MESSAGE = "Thumbnail is beyond maximum threshold size.";

    public static final int MAXIMUM_THRESHOLD = 131072; // 128KB

    public CreateRequest process(CreateRequest input) throws PluginExecutionException,
        StopProcessingException {

        if (input != null && input.getMetacards() != null) {
            for (Metacard metacard : input.getMetacards()) {

                if (metacard != null && !isValidSize(metacard.getThumbnail())) {
                    LOGGER.debug("Thumbnail size: "
                            + (metacard.getThumbnail() != null ? metacard.getThumbnail().length
                                    : "none"));
                    throw new StopProcessingException(BEYOND_THRESHOLD_MESSAGE);
                }

            }
        }
        return input;
    }

    public UpdateRequest process(UpdateRequest input) throws PluginExecutionException,
        StopProcessingException {
        if (input.getUpdates() != null) {
            for (Entry<Serializable, Metacard> updateEntry : input.getUpdates()) {

                Metacard metacard = updateEntry.getValue();
                if (metacard != null && !isValidSize(metacard.getThumbnail())) {
                    throw new StopProcessingException(BEYOND_THRESHOLD_MESSAGE);
                }

            }
        }
        return input;
    }

    public DeleteRequest process(DeleteRequest input) throws PluginExecutionException,
        StopProcessingException {
        return input;
    }

    /**
     * 
     * @param thumbnailBytes
     *            byte array representation of the thumbnail
     * @return <code>true</code> when thumbnail is <code>null</code> OR the amount of thumbnail
     *         bytes is less than or equal to {@link ThumbnailValidatorPlugin#MAXIMUM_THRESHOLD},
     *         <code>false</code> otherwise
     */

    protected boolean isValidSize(byte[] thumbnailBytes) {
        if (thumbnailBytes != null && thumbnailBytes.length > MAXIMUM_THRESHOLD) {
            return false;
        }
        return true;
    }
}
