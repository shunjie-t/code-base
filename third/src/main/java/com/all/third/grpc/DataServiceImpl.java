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
			private ByteArrayOutputStream byteData;
			private List<CreateRequest> createRequest = null;

			@Override
			public void onNext(CreateRequest request) {
				if(StringUtils.isBlank(request.getDataType())) {
					responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Data type must not be blank").asRuntimeException());
					return;
				}
				
				if(request.getData().hasNonText()) {
					if(StringUtils.isBlank(request.getData().getNonText().getDataDesc())) {
						responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Data description must not be blank").asRuntimeException());
						return;
					}
					else if(request.getData().getNonText().getByteData() == null) {
						responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Byte data must not be blank").asRuntimeException());
						return;
					}
					ByteString chunkData = request.getData().getNonText().getByteData();
				}
				else if(!request.getData().hasText()) {
					responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Data must not be blank").asRuntimeException());
					return;
				}
				
				String creBy = request.getCreBy();
				if(StringUtils.isBlank(creBy)) {
					creBy = Constants.third;
				}

				if(StringUtils.isBlank(request.getCreBy())) {
					createRequest.add(CreateRequest.newBuilder().setDataType(request.getDataType()).setData(request.getData()).setEnabled(request.getEnabled()).setCreBy(creBy).build());
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
	
//	//server streaming
//	@Override
//	public void readData(ReadRequest request, StreamObserver<ReadResponse> responseObserver) {
//		
//	}
//	
//	//bidirectional streaming
//	@Override
//	public StreamObserver<UpdateRequest> updateData(StreamObserver<UpdateResponse> responseObserver) {
//		return null;
//	}
//	
//	//unary
//	@Override
//	public void deleteData(DeleteRequest request, StreamObserver<DeleteResponse> responseObserver) {
//		
//	}
}
