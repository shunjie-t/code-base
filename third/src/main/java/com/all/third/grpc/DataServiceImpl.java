package com.all.third.grpc;

import com.all.third.CreateData;
import com.all.third.CreateRequest;
import com.all.third.CreateResponse;
import com.all.third.DataServiceGrpc;
import com.all.third.DeleteRequest;
import com.all.third.DeleteResponse;
import com.all.third.ReadRequest;
import com.all.third.ReadResponse;
import com.all.third.UpdateRequest;
import com.all.third.UpdateResponse;
import com.all.third.common.Constants;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

//endpoint for grpcurl/postman to invoke DataClient
@GrpcService
public class DataServiceImpl extends DataServiceGrpc.DataServiceImplBase {
	@Autowired
	private DataClient dataClient;

//	client streaming
	@Override
	public StreamObserver<CreateRequest> createData(StreamObserver<CreateResponse> responseObserver) {
		return new StreamObserver<CreateRequest>() {
			private List<CreateRequest> createRequest = new ArrayList<>();
			
			@Override
			public void onNext(CreateRequest request) {
				if(!request.getRequestList().isEmpty()) {
					CreateRequest.Builder newRequest = CreateRequest.newBuilder();
					
					for(CreateData data : request.getRequestList()) {
						if(StringUtils.isBlank(data.getDataType())) {
							responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Data type must not be blank").asRuntimeException());
							return;
						}
						
						if(data.getData().hasNonText()) {
							if(StringUtils.isBlank(data.getData().getNonText().getDataDesc())) {
								responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Data description must not be blank").asRuntimeException());
								return;
							}
							else if(data.getData().getNonText().getByteData() == null) {
								responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Byte data must not be blank").asRuntimeException());
								return;
							}
						}
						else if(!data.getData().hasText()) {
							responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Data must not be blank").asRuntimeException());
							return;
						}
						
						String creBy = data.getCreBy();
						if(StringUtils.isBlank(data.getCreBy())) {
							creBy = Constants.third;
						}
						
						CreateData newData = CreateData.newBuilder()
								.setDataType(data.getDataType()).setData(data.getData())
								.setEnabled(data.getEnabled()).setCreBy(creBy)
								.build();
						newRequest.addRequest(newData);
					}
					
					createRequest.add(newRequest.build());
				}
				else {
					System.out.println("request must not be empty");
				}
			}
			
			@Override
			public void onError(Throwable t) {
				System.out.println(t);
			}
			
			@Override
			public void onCompleted() {
				CreateResponse response = dataClient.invokeCreateData(createRequest);
				responseObserver.onNext(response);
				responseObserver.onCompleted();
			}
		};
	}
	
	//server streaming
	@Override
	public void readData(ReadRequest request, StreamObserver<ReadResponse> responseObserver) {
		if(request.getRequest().hasDataId() || request.getRequest().hasOther()) {
			ReadResponse response = dataClient.invokeReadData(request);
			responseObserver.onNext(response);
		}
		else {
			System.out.println("request must not be empty");
		}
		responseObserver.onCompleted();
	}
	
	//bidirectional streaming
	@Override
	public StreamObserver<UpdateRequest> updateData(StreamObserver<UpdateResponse> responseObserver) {		
		return new StreamObserver<UpdateRequest>() {
			private List<UpdateRequest> updateRequest = new ArrayList<>();
			
			@Override
			public void onNext(UpdateRequest request) {
				 if(!request.getUpdateRecord().hasDataId() && !request.getUpdateRecord().hasOther())  {
					 System.out.println("search update field must not be empty");
				 }
			}

			@Override
			public void onError(Throwable t) {
				System.out.println(t);
			}

			@Override
			public void onCompleted() {
				UpdateResponse response = dataClient.invokeUpdateData(updateRequest);
				responseObserver.onNext(response);
				responseObserver.onCompleted();
			}
		};
	}
	
	//unary
	@Override
	public void deleteData(DeleteRequest request, StreamObserver<DeleteResponse> responseObserver) {
		DeleteResponse response = dataClient.invokeDeleteData(request);
	}
}
