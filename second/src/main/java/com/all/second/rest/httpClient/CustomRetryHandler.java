package com.all.second.rest.httpClient;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.hc.client5.http.ConnectTimeoutException;
import org.apache.hc.client5.http.HttpRequestRetryStrategy;
import org.apache.hc.core5.http.ConnectionClosedException;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.util.TimeValue;

public class CustomRetryHandler implements HttpRequestRetryStrategy {
	@Override
	public boolean retryRequest(HttpRequest request, IOException exception, int execCount, HttpContext context) {
		if (execCount >= 3) {
            return false;
        }
        if (exception instanceof ConnectTimeoutException || exception instanceof ConnectionClosedException) {
            return true;
        }
		return false;
	}

	@Override
	public boolean retryRequest(HttpResponse response, int execCount, HttpContext context) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public TimeValue getRetryInterval(HttpResponse response, int execCount, HttpContext context) {
		return TimeValue.of(1, TimeUnit.SECONDS); // 1 second delay between retries
	}
}
