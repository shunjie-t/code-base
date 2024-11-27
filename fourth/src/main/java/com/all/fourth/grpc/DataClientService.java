package com.all.fourth.grpc;

import org.springframework.web.bind.annotation.RestController;

import com.all.fourth.DataServiceGrpc;

import net.devh.boot.grpc.client.inject.GrpcClient;

@RestController
public class DataClientService {
	@GrpcClient("FourthClient")
	private DataServiceGrpc.DataServiceBlockingStub blockStub;
	
	@GrpcClient("FourthClient")
	private DataServiceGrpc.DataServiceStub asyncStub;
	
	public void invokeGetDataDetail() {}
	
	public void invokePostData() {}
	
	public void invokeGetData() {}
	
	public void invokePostAndGetData() {}
}
