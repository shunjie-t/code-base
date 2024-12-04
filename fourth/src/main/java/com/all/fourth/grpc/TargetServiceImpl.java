package com.all.fourth.grpc;

import java.io.ByteArrayOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.all.fourth.CreateRequest;
import com.all.fourth.CreateResponse;
import com.all.fourth.DeleteRequest;
import com.all.fourth.DeleteResponse;
import com.all.fourth.ReadRequest;
import com.all.fourth.ReadResponse;
import com.all.fourth.TargetServiceGrpc;
import com.all.fourth.UpdateRequest;
import com.all.fourth.UpdateResponse;
import com.all.fourth.common.Constants;
import com.all.fourth.dao.DataJpaDao;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

//endpoint to be called by third client
@GrpcService
public class TargetServiceImpl extends TargetServiceGrpc.TargetServiceImplBase {
	@Autowired
	private DataJpaDao dataJpaDao;
	
//	client streaming
	@Override
	public StreamObserver<CreateRequest> createData(StreamObserver<CreateResponse> responseObserver) {
		return new StreamObserver<CreateRequest>() {
			private ByteArrayOutputStream byteData;

			@Override
			public void onNext(CreateRequest request) {
				if(StringUtils.isBlank(request.getDataType())) {
					responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Data type must not be blank").asRuntimeException());
					return;
				}
				else if(request.getData() == null) {
					responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Data must not be blank").asRuntimeException());
					return;
				}

				request.newBuilder().setCreBy(Constants.fourth).build();
			}

			@Override
			public void onError(Throwable t) {
				System.out.println(t.getMessage());
			}

			@Override
			public void onCompleted() {
				responseObserver.onNext(null);
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
