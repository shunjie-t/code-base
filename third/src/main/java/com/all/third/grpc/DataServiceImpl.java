package com.all.third.grpc;

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
import com.google.protobuf.ByteString;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.io.ByteArrayOutputStream;

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
			private ByteArrayOutputStream byteData;
			private CreateRequest createRequest = null;
			
			@Override
			public void onNext(CreateRequest request) {
				if(StringUtils.isBlank(request.getDataType())) {
					responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Data type must not be blank").asRuntimeException());
					return;
				}
				
				if(request.getData() == null) {
					responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Data must not be blank").asRuntimeException());
					return;
				}
				else {
					if(request.getData().getNonText() != null) {						
						ByteString chunkData = request.getData().getNonText().getByteData();
					}
				}
				
				if(StringUtils.isBlank(request.getCreBy())) {					
					request.newBuilder().setCreBy(Constants.third).build();
				}
				
				createRequest = request;
			}

			@Override
			public void onError(Throwable t) {
				System.out.println(t.getMessage());
			}

			@Override
			public void onCompleted() {
//				call client
				dataClient.invokeCreateData(request);
				responseObserver.onCompleted();
			}
		};
	}
	
//	server streaming
	@Override
	public void readData(ReadRequest request, StreamObserver<ReadResponse> responseObserver) {
		
	}
	
//	bidirectional streaming
	@Override
	public StreamObserver<UpdateRequest> updateData(StreamObserver<UpdateResponse> responseObserver) {
		return null;
	}
	
//	unary
	@Override
	public void deleteData(DeleteRequest request, StreamObserver<DeleteResponse> responseObserver) {
		
	}
}
