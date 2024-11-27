package com.all.fourth.grpc;

import com.all.fourth.CompleteData;
import com.all.fourth.DataDetail;
import com.all.fourth.DataServiceGrpc;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class DataServiceImpl extends DataServiceGrpc.DataServiceImplBase {
	@Override
	public void getDataDetail(CompleteData request, StreamObserver<DataDetail> responseObserver) {
		DataDetail response = DataDetail.newBuilder().setDataId("1").setDataType("TXT").build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

	@Override
	public StreamObserver<CompleteData> postData(StreamObserver<DataDetail> responseObserver) {
		return new StreamObserver<CompleteData>() {
			@Override
			public void onNext(CompleteData value) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onError(Throwable t) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onCompleted() {
				// TODO Auto-generated method stub
				
			}
		};
	}
	
	@Override
	public void getData(CompleteData request, StreamObserver<CompleteData> responseObserver) {
		responseObserver.onCompleted();
	}
	
	@Override
	public StreamObserver<CompleteData> postAndGetData(StreamObserver<CompleteData> responseObserver) {
		return new StreamObserver<CompleteData>() {
			@Override
			public void onNext(CompleteData value) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onError(Throwable t) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onCompleted() {
				// TODO Auto-generated method stub
				
			}
		};
	}
}
