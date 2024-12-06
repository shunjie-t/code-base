package com.all.third.grpc;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.all.third.CreateRequest;
import com.all.third.CreateResponse;
import com.all.third.DataServiceGrpc;
import com.all.third.TargetServiceGrpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;

//@Service
@Component
public class DataClient {	
//	@GrpcClient("fourthClient")
	private TargetServiceGrpc.TargetServiceBlockingStub blockingStub;
//	
//	@GrpcClient("fourthClient")
	private TargetServiceGrpc.TargetServiceStub asyncStub;
	
	private CreateResponse responseResult = null;
	
	public DataClient() {
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9094).usePlaintext().build();
		asyncStub = TargetServiceGrpc.newStub(channel);
	}

	public CreateResponse invokeCreateData(List<CreateRequest> request) {
		CountDownLatch latch = new CountDownLatch(1);

		StreamObserver<CreateRequest> requestObserver = asyncStub.createData(new StreamObserver<>() {
			@Override
			public void onNext(CreateResponse response) {
				responseResult = response;
				System.out.println(response);
			}

			@Override
			public void onError(Throwable t) {
				System.out.println(t);
				latch.countDown();
			}

			@Override
			public void onCompleted() {
				System.out.println("on complete");
				latch.countDown();
			}
		});
		
		for(CreateRequest req : request) {
			requestObserver.onNext(req);
		}
		
		requestObserver.onCompleted();
		try {
			latch.await(1, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return responseResult;
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
