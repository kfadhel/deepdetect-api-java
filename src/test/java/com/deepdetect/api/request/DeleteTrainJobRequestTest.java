package com.deepdetect.api.request;

import static com.deepdetect.api.TestUtils.DELETE_TRAIN_JOB_RESPONSE_FILE;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import java.io.IOException;
import java.net.URLDecoder;
import org.hamcrest.core.Is;
import org.junit.Test;

import com.deepdetect.api.TestUtils;
import com.deepdetect.api.exceptions.DeepDetectException;
import com.deepdetect.api.response.DeleteTrainingJobResponse;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;

public class DeleteTrainJobRequestTest extends AbstractRequestTest {

	private static final String SERVICE_NAME = "myserv";
	private static final int JOB_ID = 1;

	@Override
	public void testRequestRequiresBaseURL() throws DeepDetectException {
		DeleteTrainingJobRequest.newDeleteTrainingJobRequest() //
				.service(SERVICE_NAME) //
				.jobId(JOB_ID) //
				.build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDeleteTrainingJobRequestRequiresServiceName() throws DeepDetectException {
		DeleteTrainingJobRequest.newDeleteTrainingJobRequest() //
				.baseURL(baseUrl) //
				.jobId(JOB_ID) //
				.build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDeleteTrainingJobRequestRequiresJobID() throws DeepDetectException {
		DeleteTrainingJobRequest.newDeleteTrainingJobRequest() //
				.baseURL(baseUrl) //
				.service(SERVICE_NAME) //
				.build();
	}

	@Test
	public void testDeleteTrainingJobRequestReturnsExpectedResult()
			throws DeepDetectException, IOException, InterruptedException {

		server.enqueue(new MockResponse().setBody(TestUtils.getResourceAsString(DELETE_TRAIN_JOB_RESPONSE_FILE)));
		DeleteTrainingJobResponse response = DeleteTrainingJobRequest.newDeleteTrainingJobRequest() //
				.baseURL(baseUrl) //
				.service(SERVICE_NAME) //
				.jobId(JOB_ID) //
				.build() //
				.process();

		RecordedRequest request = server.takeRequest(2, SECONDS);
		assertThat(request.getMethod(), is("DELETE"));
		assertThat(URLDecoder.decode(request.getPath(), "UTF-8"),
				is("/train?service=" + SERVICE_NAME + "&job=" + JOB_ID));

		assertThat(response.getStatus().getCode(), Is.is(200));
		assertThat(response.getStatus().getMessage(), Is.is("OK"));
		assertThat(response.getHead().getStatus(), Is.is("terminated"));
		assertThat(response.getHead().getTime(), Is.is(196.0));
	}

}
