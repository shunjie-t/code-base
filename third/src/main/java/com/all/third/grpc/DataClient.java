package com.all.third.grpc;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.all.third.CreateRequest;
import com.all.third.CreateResponse;
import com.all.third.DeleteRequest;
import com.all.third.DeleteResponse;
import com.all.third.ReadRequest;
import com.all.third.ReadResponse;
import com.all.third.TargetServiceGrpc;
import com.all.third.UpdateRequest;
import com.all.third.UpdateResponse;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;

@Component
public class DataClient {
	@GrpcClient("fourthClient")
	private TargetServiceGrpc.TargetServiceBlockingStub blockingStub;
	
	@GrpcClient("fourthClient")
	private TargetServiceGrpc.TargetServiceStub asyncStub;
	
	private CreateResponse createResponse = null;
	private UpdateResponse updateResponse = null;
	
//	public DataClient() {
//		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9094).usePlaintext().build();
//		asyncStub = TargetServiceGrpc.newStub(channel);
//		blockingStub = TargetServiceGrpc.newBlockingStub(channel);
//	}

	public CreateResponse invokeCreateData(List<CreateRequest> request) {
		CountDownLatch latch = new CountDownLatch(1);

		StreamObserver<CreateRequest> requestObserver = asyncStub.createData(new StreamObserver<>() {
			@Override
			public void onNext(CreateResponse response) {
				createResponse = response;
			}

			@Override
			public void onError(Throwable t) {
				System.out.println(t);
				latch.countDown();
			}

			@Override
			public void onCompleted() {
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
		
		return createResponse;
	}
	
	public ReadResponse invokeReadData(ReadRequest request) {
		Iterator<ReadResponse> iterator = blockingStub.withDeadlineAfter(3600, TimeUnit.SECONDS).readData(request);
		ReadResponse response = null;
		while(iterator.hasNext()) {
			response = iterator.next();
		}
		return response;
	}
	
	public UpdateResponse invokeUpdateData(List<UpdateRequest> request) {
//	public void invokeUpdateData(List<UpdateRequest> request) {
		CountDownLatch latch = new CountDownLatch(1);
		StreamObserver<UpdateRequest> requestObserver = asyncStub.updateData(new StreamObserver<UpdateResponse>() {
			@Override
			public void onNext(UpdateResponse response) {
				 updateResponse = response;
			}

			@Override
			public void onError(Throwable t) {
				System.out.println(t);
				latch.countDown();
			}

			@Override
			public void onCompleted() {
				 latch.countDown();
			}
		});
		
		for(UpdateRequest req : request) {
			requestObserver.onNext(req);
		}
		
		requestObserver.onCompleted();
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return updateResponse;
	}
	
	public DeleteResponse invokeDeleteData(DeleteRequest request) {
		blockingStub.deleteData(null);
		DeleteResponse response = null;
		return response;
	}
}
