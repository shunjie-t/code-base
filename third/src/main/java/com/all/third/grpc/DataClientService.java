package com.all.third.grpc;

import org.springframework.web.bind.annotation.RestController;

import com.all.third.DataServiceGrpc;

import net.devh.boot.grpc.client.inject.GrpcClient;

@RestController
public class DataClientService {
	@GrpcClient("ThirdClient")
	private DataServiceGrpc.DataServiceBlockingStub blockingStub;
	
	@GrpcClient("ThirdClient")
	private DataServiceGrpc.DataServiceStub asyncStub;
	
	public void invokeGetDataDetail() {}
	
	public void invokePostData() {}
	
	public void invokeGetData() {}
	
	public void invokePostAndGetData() {}
}
