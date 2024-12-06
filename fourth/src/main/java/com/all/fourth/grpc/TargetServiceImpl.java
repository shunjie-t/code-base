package com.all.fourth.grpc;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
import com.all.fourth.dao.view.DataView;
import com.google.protobuf.ByteString;

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
//			private ByteArrayOutputStream byteData;
			private CreateResponse response = null;
			private List<String> dataId = new ArrayList<>();
			private List<String> dataType = new ArrayList<>();
			private List<String> data = new ArrayList<>();
			private List<Boolean> enabled = new ArrayList<>();
			private List<String> creBy = new ArrayList<>();
			private List<String> creOn = new ArrayList<>();

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
				
				if(StringUtils.isBlank(request.getCreBy())) {
					responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Cre by must not be blank").asRuntimeException());
				}
				
				LocalDateTime currentDateTime = LocalDateTime.now();
				DataView view = new DataView();
				view.setDataType(request.getDataType());
				view.setData(request.getData().getText());
				view.setEnabled(request.getEnabled());
				view.setCreBy(request.getCreBy());
				view.setCreOn(currentDateTime);
				view.setUpdBy(request.getCreBy());
				view.setUpdOn(currentDateTime);
				
				DataView saved = dataJpaDao.save(view);
				dataId.add(saved.getDataId().toString());
				dataType.add(saved.getDataType());
				data.add(saved.getData());
				enabled.add(request.getEnabled());
				creBy.add(request.getCreBy());
				creOn.add(currentDateTime.toString());
			}

			@Override
			public void onError(Throwable t) {
				System.out.println(t.getMessage());
			}

			@Override
			public void onCompleted() {
				response = CreateResponse.newBuilder().addAllDataId(dataId).addAllDataType(dataType).addAllEnabled(enabled).addAllCreBy(creBy).addAllCreOn(creOn).build();
				responseObserver.onNext(response);
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
