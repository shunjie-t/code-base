package com.all.third.grpc;

import org.springframework.stereotype.Service;

import com.all.third.CreateRequest;
import com.all.third.CreateResponse;
import com.all.third.DataServiceGrpc;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;

@Service
public class DataClient {
	@GrpcClient("fourthClient")
	private DataServiceGrpc.DataServiceBlockingStub blockingStub;

	@GrpcClient("fourthClient")
	private DataServiceGrpc.DataServiceStub asyncStub;

	public CreateResponse invokeCreateData(CreateRequest request) {
		CreateResponse response = null;

		StreamObserver<CreateRequest> createRequest = asyncStub.createData(new StreamObserver<>() {
			@Override
			public void onNext(CreateResponse value) {
				System.out.println("on next");
			}

			@Override
			public void onError(Throwable t) {
				System.out.println("on error");
			}

			@Override
			public void onCompleted() {
				System.out.println("on complete");
			}
		});

		return response;
	}
	
//	public void invokePostData() {
//		asyncStub.postData(null);
//	}
//	
//	public void invokeGetData() {
//		blockingStub.getData(null);
//	}
//	
//	public void invokePostAndGetData() {
//		asyncStub.postAndGetData(null);
//	}
}
