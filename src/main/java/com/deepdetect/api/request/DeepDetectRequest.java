package com.deepdetect.api.request;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import com.deepdetect.api.enums.Operation;
import com.deepdetect.api.exceptions.DeepDetectException;
import com.deepdetect.api.response.DeepDetectResponse;
import com.google.gson.JsonObject;

/**
 * Base class for requests to the DeepDetect API.
 * 
 * @param <T>
 *            The response class of the request, a subclass of
 *            {@link DeepDetectResponse}
 */
public abstract class DeepDetectRequest<T extends DeepDetectResponse> {

	protected String baseURL;

	protected abstract JsonObject getContent();

	protected abstract String getPath();

	protected abstract Operation getOperation();

	protected abstract T internalProcess();

	/**
	 * process the request to the deepdetect server with given parameters, get
	 * the json result and deserialize it to the response class
	 * 
	 * @return T a subclass of {@link DeepDetectResponse} the response to the
	 *         request
	 * @throws DeepDetectException
	 *             when a network exception or the server respond with error
	 *             code
	 */
	public T process() throws DeepDetectException {
		try {
			T internalResult = internalProcess();
			int resultCode = internalResult.getStatus().getCode();
			if (resultCode != 200 && resultCode != 201) {
				throw new DeepDetectException("Exception getting response, error code: " + resultCode);
			}
			return internalResult;
		} catch (Exception exp) {
			throw new DeepDetectException("", exp);
		}
	}

	/**
	 * do a get call to the deepdetect server
	 * 
	 * @return server response as plain text
	 */
	protected String doGet() {
		return ClientBuilder.newClient().target(baseURL).path(getPath()).request().get(String.class);
	}

	/**
	 * do a post call to the deepdetect server with content as json
	 * 
	 * @return server response as plain text
	 */
	protected String doPost() {
		return ClientBuilder.newClient().target(baseURL).path(getPath()).request()
				.post(Entity.entity(getContent(), MediaType.APPLICATION_JSON), String.class);
	}

	/**
	 * do a put call to the deepdetect server with content as json
	 * 
	 * @return server response as plain text
	 */
	protected String doPut() {
		return ClientBuilder.newClient().target(baseURL).path(getPath()).request()
				.put(Entity.entity(getContent(), MediaType.APPLICATION_JSON), String.class);
	}

	/**
	 * do a delete call to the deepdetect server with content as json
	 * 
	 * @return server response as plain text
	 */
	protected String doDelete() {
		return ClientBuilder.newClient().target(baseURL).path(getPath()).request().delete(String.class);
	}
}
