package ddf.training.catalog.plugin;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import ddf.catalog.data.impl.MetacardImpl;
import ddf.catalog.operation.CreateRequest;
import ddf.catalog.operation.impl.CreateRequestImpl;
import ddf.catalog.plugin.PluginExecutionException;
import ddf.catalog.plugin.StopProcessingException;

public class TestThumbnailValidator {

	private static final String SAMPLE_ID = "12345678900987654321abcdeffedcba";

	@Test
	public void testValidSize() {

		MetacardImpl m = new MetacardImpl();

		byte[] smallBytes = { 86 };

		m.setTitle("myTitle");
		m.setId(SAMPLE_ID);
		m.setThumbnail(smallBytes);

		ThumbnailValidatorPlugin validator = new ThumbnailValidatorPlugin();

		try {
			CreateRequest request = validator.process(new CreateRequestImpl(m));
			
			assertEquals("myTitle", request.getMetacards().get(0).getTitle());
		} catch (PluginExecutionException e) {
			fail("Should not be throwing exception.");
		} catch (StopProcessingException e) {
			fail("Should not be throwing exception.");
		}

	}

	@Test(expected=StopProcessingException.class)
	public void testIsNotValidSize() throws PluginExecutionException, StopProcessingException {

		MetacardImpl m = new MetacardImpl();

		byte[] largeBytes = new byte[ThumbnailValidatorPlugin.MAXIMUM_THRESHOLD + 1];
		for (int i = 0; i < ThumbnailValidatorPlugin.MAXIMUM_THRESHOLD + 1; i++) {
			largeBytes[i] = (byte) i;
		}

		m.setTitle("myTitle");
		m.setId(SAMPLE_ID);
		m.setThumbnail(largeBytes);

		ThumbnailValidatorPlugin validator = new ThumbnailValidatorPlugin();

		validator.process(new CreateRequestImpl(m));

	}
	
	@Test()
	public void testThumbnailIsNotNull() throws PluginExecutionException, StopProcessingException {

		MetacardImpl m = new MetacardImpl();

		m.setTitle("myTitle");
		m.setId(SAMPLE_ID);
		
		ThumbnailValidatorPlugin validator = new ThumbnailValidatorPlugin();

		try {
			CreateRequest request = validator.process(new CreateRequestImpl(m));
			
			assertEquals("myTitle", request.getMetacards().get(0).getTitle());
		} catch (PluginExecutionException e) {
			fail("Should not be throwing exception.");
		} catch (StopProcessingException e) {
			fail("Should not be throwing exception.");
		}

	}
}
