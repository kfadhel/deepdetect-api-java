package com.deepdetect.api.request;

import static com.deepdetect.api.TestUtils.GET_INFO_TRAIN_JOB_RESPONSE_FILE;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.URLDecoder;

import org.junit.Test;

import com.deepdetect.api.TestUtils;
import com.deepdetect.api.exceptions.DeepDetectException;
import com.deepdetect.api.response.InfoTrainJobResponse;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;

public class InfoTrainJobRequestTest extends AbstractRequestTest {

	private static final String SERVICE_NAME = "myserv";
	private static final int JOB_ID = 1;

	@Override
	public void testRequestRequiresBaseURL() throws DeepDetectException {
		InfoTrainJobRequest.newInfoTrainJobRequest() //
				.service(SERVICE_NAME) //
				.jobId(JOB_ID) //
				.build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInfoTrainJobRequestRequiresServiceName() throws DeepDetectException {
		InfoTrainJobRequest.newInfoTrainJobRequest() //
				.baseURL(baseUrl) //
				.jobId(JOB_ID) //
				.build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInfoTrainJobRequestRequiresJobID() throws DeepDetectException {
		InfoTrainJobRequest.newInfoTrainJobRequest() //
				.baseURL(baseUrl) //
				.service(SERVICE_NAME) //
				.build();
	}

	@Test
	public void testInfoTrainJobRequestReturnsExpectedResult()
			throws DeepDetectException, InterruptedException, IOException {

		server.enqueue(new MockResponse().setBody(TestUtils.getResourceAsString(GET_INFO_TRAIN_JOB_RESPONSE_FILE)));

		InfoTrainJobResponse response = InfoTrainJobRequest.newInfoTrainJobRequest() //
				.baseURL(baseUrl) //
				.service(SERVICE_NAME) //
				.jobId(JOB_ID) //
				.build().process();

		RecordedRequest request = server.takeRequest(2, SECONDS);
		assertThat(request.getMethod(), is("GET"));
		assertThat(URLDecoder.decode(request.getPath(), "UTF-8"),
				is("/train?service=" + SERVICE_NAME + "&job=" + JOB_ID));

		assertThat(response.getStatus().getCode(), is(200));
		assertThat(response.getStatus().getMessage(), is("OK"));
		assertThat(response.getHead().getStatus(), is("running"));
		assertThat(response.getBody().getMeasure().getIteration(), is(445L));
	}
}
