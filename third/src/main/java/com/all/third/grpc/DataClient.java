package com.all.third.grpc;

import org.springframework.stereotype.Service;

import com.all.third.CreateRequest;
import com.all.third.CreateResponse;
import com.all.third.DataServiceGrpc;

import net.devh.boot.grpc.client.inject.GrpcClient;

@Service
public class DataClient {
	@GrpcClient("fourthClient")
	private DataServiceGrpc.DataServiceBlockingStub blockingStub;
	
	@GrpcClient("fourthClient")
	private DataServiceGrpc.DataServiceStub asyncStub;
	
//	public CreateResponse invokeCreateData(CreateRequest request) {
//		return asyncStub.createData(request);
//	}
//	
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
