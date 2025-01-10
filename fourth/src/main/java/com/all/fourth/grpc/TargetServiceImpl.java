package com.all.fourth.grpc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.all.fourth.CompleteData;
import com.all.fourth.CreateData;
import com.all.fourth.CreateRequest;
import com.all.fourth.CreateResponse;
import com.all.fourth.Data;
import com.all.fourth.DeleteRequest;
import com.all.fourth.DeleteResponse;
import com.all.fourth.Enabled;
import com.all.fourth.ReadRequest;
import com.all.fourth.ReadResponse;
import com.all.fourth.TargetServiceGrpc;
import com.all.fourth.UpdateRequest;
import com.all.fourth.UpdateResponse;
import com.all.fourth.dao.view.DataView;
import com.all.fourth.dao.view.IDataJpaWrapper;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

//endpoint to be called by third client
@GrpcService
public class TargetServiceImpl extends TargetServiceGrpc.TargetServiceImplBase {
	@Autowired
	private IDataJpaWrapper dataJpaWrapper;
	
//	client streaming
	@Override
	public StreamObserver<CreateRequest> createData(StreamObserver<CreateResponse> responseObserver) {
		return new StreamObserver<CreateRequest>() {
			private CreateResponse response = null;
			private List<DataView> dataView = new ArrayList<>();
			List<CompleteData> completeData = new ArrayList<>();
			
			@Override
			public void onNext(CreateRequest request) {
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
					
					if(StringUtils.isBlank(data.getCreBy())) {
						responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Cre by must not be blank").asRuntimeException());
						return;
					}
					
					LocalDateTime currentDateTime = LocalDateTime.now();
					DataView view = new DataView();
					view.setDataType(data.getDataType());
					view.setData(data.getData().getText());
					if(data.getEnabledValue() == 1) {						
						view.setEnabled(true);
					}
					else {						
						view.setEnabled(false);
					}
					view.setCreBy(data.getCreBy());
					view.setCreOn(currentDateTime);
					view.setUpdBy(data.getCreBy());
					view.setUpdOn(currentDateTime);
					dataView.add(view);
				}
			}

			@Override
			public void onError(Throwable t) {
				System.out.println(t.getMessage());
			}

			@Override
			public void onCompleted() {
				List<DataView> result = null;
				try {
					result = dataJpaWrapper.saveAllPostAndVerify(dataView);
					
					for(DataView view : result) {
						Data newData = Data.newBuilder().setText(view.getData()).build();
						
						CompleteData newCompleteData = CompleteData.newBuilder()
								.setDataId(view.getDataId().toString())
								.setDataType(view.getDataType())
								.setData(newData)
								.setEnabled(view.isEnabled() ? Enabled.TRUE : Enabled.FALSE)
								.setCreBy(view.getCreBy())
								.setCreOn(view.getCreOn().toString())
								.setUpdBy(view.getCreBy())
								.setUpdOn(view.getCreOn().toString())
								.build();
						
						completeData.add(newCompleteData);
					}
					response = CreateResponse.newBuilder().addAllResponse(completeData).build();
					responseObserver.onNext(response);
				} catch (Exception e) {
					e.printStackTrace();
				}
				responseObserver.onCompleted();
			}
		};
	}
	
//	server streaming
	@Override
	public void readData(ReadRequest request, StreamObserver<ReadResponse> responseObserver) {
		List<DataView> result = null;
		if(request.getRequest().hasDataId()) {			
			List<BigDecimal> idList = new ArrayList<>();
			for(String id : request.getRequest().getDataId().getDataList()) {
				BigDecimal dbId = new BigDecimal(id);
				idList.add(dbId);
			}
			
			result = dataJpaWrapper.findAllById(idList);
		}
		else if(request.getRequest().hasOther()) {
			List<String> dataTypeList = new ArrayList<>();
			List<String> creByList = new ArrayList<>();
			List<String> updByList = new ArrayList<>();
			if(!request.getRequest().getOther().getDataType().getDataList().isEmpty()) {
				for(String dataType : request.getRequest().getOther().getDataType().getDataList()) {
					dataTypeList.add(dataType);
				}
			}
			if(!request.getRequest().getOther().getCreBy().getDataList().isEmpty()) {
				for(String creBy : request.getRequest().getOther().getDataType().getDataList()) {
					creByList.add(creBy);
				}
			}
			if(!request.getRequest().getOther().getUpdBy().getDataList().isEmpty()) {
				for(String updBy : request.getRequest().getOther().getDataType().getDataList()) {
					updByList.add(updBy);
				}
			}
			
			result = dataJpaWrapper.findByConditions(dataTypeList, request.getRequest().getOther().getEnabledValue() == 1, creByList, updByList);
		}
		else {
			System.out.println("request is empty");
		}
		
		if(!result.isEmpty()) {
			for(DataView view : result) {
				ReadResponse.Builder responseBuilder = ReadResponse.newBuilder();
				Data data = Data.newBuilder().setText(view.getData()).build();
				CompleteData completeData = CompleteData.newBuilder().setDataId(view.getDataId().toString())
						.setDataType(view.getDataType()).setData(data).setEnabled(view.isEnabled() ? Enabled.TRUE : Enabled.FALSE)
						.setCreBy(view.getCreBy()).setCreOn(view.getCreOn().toString())
						.setUpdBy(view.getUpdBy()).setUpdOn(view.getUpdOn().toString()).build();
				responseBuilder.setResponse(completeData);
				responseObserver.onNext(responseBuilder.build());
			}
		}
		
		responseObserver.onCompleted();
	}

//	bidirectional streaming
	@Override
	public StreamObserver<UpdateRequest> updateData(StreamObserver<UpdateResponse> responseObserver) {
		return new StreamObserver<UpdateRequest>() {
			@Override
			public void onNext(UpdateRequest value) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onError(Throwable t) {
				System.out.println(t);
			}

			@Override
			public void onCompleted() {
				// TODO Auto-generated method stub
			}
		};
	}

//	unary
	@Override
	public void deleteData(DeleteRequest request, StreamObserver<DeleteResponse> responseObserver) {
//		if(request.getDeleteFields().hasDataId()) {}
//		else if(request.getDeleteFields().hasOther()) {}
		responseObserver.onNext(null);
		responseObserver.onCompleted();
	}
}
