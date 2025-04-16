package com.all.fourth.grpc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.all.fourth.CompleteData;
import com.all.fourth.CreateData;
import com.all.fourth.CreateRequest;
import com.all.fourth.CreateResponse;
import com.all.fourth.Data;
import com.all.fourth.DeleteRequest;
import com.all.fourth.DeleteResponse;
import com.all.fourth.OtherField;
import com.all.fourth.ReadRequest;
import com.all.fourth.ReadResponse;
import com.all.fourth.SearchField;
import com.all.fourth.TargetServiceGrpc;
import com.all.fourth.UpdateField;
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
			private CreateResponse.Builder response = CreateResponse.newBuilder();
			private List<DataView> dataView = new ArrayList<>();
			List<CompleteData> completeData = new ArrayList<>();
			
			@Override
			public void onNext(CreateRequest request) {
				for(CreateData data : request.getCreateRecordList()) {
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
					view.setDataType(data.getDataType().toUpperCase());
					view.setData(data.getData().getText().toUpperCase());
					view.setCreBy(data.getCreBy().toUpperCase());
					view.setCreOn(currentDateTime);
					view.setUpdBy(data.getCreBy().toUpperCase());
					view.setUpdOn(currentDateTime);
					if(data.hasEnabled()) {
						view.setEnabled(data.getEnabled().getValue());
					}
					else {
						view.setEnabled(true);
					}
					dataView.add(view);
				}
			}

			@Override
			public void onError(Throwable t) {
				responseObserver.onError(t);
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
								.setEnabled(view.isEnabled())
								.setCreBy(view.getCreBy())
								.setCreOn(view.getCreOn().toString())
								.setUpdBy(view.getCreBy())
								.setUpdOn(view.getCreOn().toString())
								.build();
						
						completeData.add(newCompleteData);
					}
					
					response.addAllCreatedRecord(completeData);
					responseObserver.onNext(response.build());
				} catch (Exception e) {
					e.printStackTrace();
					responseObserver.onError(e);
				}
				responseObserver.onCompleted();
			}
		};
	}
	
//	server streaming
	@Override
	public void readData(ReadRequest request, StreamObserver<ReadResponse> responseObserver) {
		List<DataView> result = null;
		SearchField readRequest = request.getReadRecord();
		
		if(readRequest.hasDataId()) {
			List<BigDecimal> idList = new ArrayList<>();
			for(String id : readRequest.getDataId().getDataList()) {
				BigDecimal dbId = new BigDecimal(id);
				idList.add(dbId);
			}
			result = dataJpaWrapper.findAllById(idList);
		}
		else if(readRequest.hasOther()) {
			List<String> dataTypeList = new ArrayList<>();
			List<String> creByList = new ArrayList<>();
			List<String> updByList = new ArrayList<>();
			OtherField other = readRequest.getOther();
			
			if(!other.getDataType().getDataList().isEmpty()) {
				for(String dataType : other.getDataType().getDataList()) {
					dataTypeList.add(dataType.toUpperCase());
				}
			}
			
			Boolean enabled = null;
			if(other.hasEnabled()) {
				enabled = other.getEnabled().getValue();
			}
			
			if(!other.getCreBy().getDataList().isEmpty()) {
				for(String creBy : other.getCreBy().getDataList()) {
					creByList.add(creBy.toUpperCase());
				}
			}
			
			if(!other.getUpdBy().getDataList().isEmpty()) {
				for(String updBy : other.getUpdBy().getDataList()) {
					updByList.add(updBy.toUpperCase());
				}
			}
			result = dataJpaWrapper.findByConditions(dataTypeList, enabled, creByList, updByList);
		}
		else {
			responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Request is empty").asRuntimeException());
		}
		
		if(!result.isEmpty()) {
			for(DataView view : result) {
				ReadResponse.Builder readResponse = ReadResponse.newBuilder();
				Data data = Data.newBuilder().setText(view.getData()).build();
				CompleteData completeData = CompleteData.newBuilder().setDataId(view.getDataId().toString())
						.setDataType(view.getDataType()).setData(data).setEnabled(view.isEnabled())
						.setCreBy(view.getCreBy()).setCreOn(view.getCreOn().toString())
						.setUpdBy(view.getUpdBy()).setUpdOn(view.getUpdOn().toString()).build();
				readResponse.setReadRecord(completeData);
				responseObserver.onNext(readResponse.build());
			}
		}
		
		responseObserver.onCompleted();
	}

//	bidirectional streaming
	@Override
	public StreamObserver<UpdateRequest> updateData(StreamObserver<UpdateResponse> responseObserver) {
		return new StreamObserver<UpdateRequest>() {
			List<DataView> existedDataView = new ArrayList<>();
			List<DataView> updateDataView = new ArrayList<>();
			
			@Override
			public void onNext(UpdateRequest request) {
				if(!request.getSearchRecord().hasDataId() && !request.getSearchRecord().hasOther()) {
					responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Search update field must not be empty").asRuntimeException());
				}
				else if(!request.getUpdateRecord().hasData() && !request.getUpdateRecord().hasDataType() && 
						!request.getUpdateRecord().hasEnabled() && !request.getUpdateRecord().hasUpdBy()) {
					responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("There must be at least one data to be updated").asRuntimeException());
				}
				else {
					SearchField searchRecord = request.getSearchRecord();
					if(searchRecord.hasDataId()) {
						List<BigDecimal> searchId = searchRecord.getDataId().getDataList().stream().map(BigDecimal::new).collect(Collectors.toList());
						existedDataView = dataJpaWrapper.findAllById(searchId);
					}
					else if(searchRecord.hasOther()) {
						List<String> dataTypeList = new ArrayList<>();
						List<String> creByList = new ArrayList<>();
						List<String> updByList = new ArrayList<>();
						OtherField other = searchRecord.getOther();
						
						if(!other.getDataType().getDataList().isEmpty()) {
							for(String dataType : other.getDataType().getDataList()) {
								dataTypeList.add(dataType.toUpperCase());
							}
						}
						
						Boolean enabled = null;
						if(other.hasEnabled()) {
							enabled = other.getEnabled().getValue();
						}
						
						if(!other.getCreBy().getDataList().isEmpty()) {
							for(String creBy : other.getCreBy().getDataList()) {
								creByList.add(creBy.toUpperCase());
							}
						}
						
						if(!other.getUpdBy().getDataList().isEmpty()) {
							for(String updBy : other.getUpdBy().getDataList()) {
								updByList.add(updBy.toUpperCase());
							}
						}
						
						existedDataView = dataJpaWrapper.findByConditions(dataTypeList, enabled, creByList, updByList);
					}
					
					UpdateField updateRecord = request.getUpdateRecord();
					for(int i = 0; i < existedDataView.size(); i++) {
						DataView newDv = new DataView();
						if(updateRecord.hasDataType()) {
							newDv.setDataType(updateRecord.getDataType());
						}
						else {
							newDv.setDataType(existedDataView.get(i).getDataType());
						}
						
						if(updateRecord.hasData() && updateRecord.getData().hasText()) {
							newDv.setData(updateRecord.getData().getText());
						}
						else {
							newDv.setData(existedDataView.get(i).getData());
						}
						
						newDv.setEnabled(existedDataView.get(i).isEnabled());
						
						if(updateRecord.hasUpdBy()) {
							newDv.setUpdBy(updateRecord.getUpdBy());
						}
						else {
							newDv.setUpdBy(existedDataView.get(i).getUpdBy());
						}
						
						LocalDateTime dateTime = LocalDateTime.now();
						newDv.setDataId(existedDataView.get(i).getDataId());
						newDv.setCreBy(existedDataView.get(i).getCreBy());
						newDv.setCreOn(dateTime);
						newDv.setUpdOn(dateTime);
						updateDataView.add(newDv);
					}
				}
			}

			@Override
			public void onError(Throwable t) {
				responseObserver.onError(t);
			}

			@Override
			public void onCompleted() {
				List<DataView> updatedDataView = new ArrayList<>();
				try {
					updatedDataView = dataJpaWrapper.saveAllPostAndVerify(updateDataView);
					
					List<CompleteData> existedRecord = new ArrayList<>();
					List<CompleteData> updatedRecord = new ArrayList<>();
					
					for(int i = 0; i < updatedDataView.size(); i++) {
						DataView existedDv = existedDataView.get(i);
						Data existedData = Data.newBuilder().setText(existedDv.getData()).build();
						CompleteData existCD = CompleteData.newBuilder()
								.setDataId(existedDv.getDataId().toString())
								.setDataType(existedDv.getDataType())
								.setData(existedData)
								.setEnabled(existedDv.isEnabled())
								.setCreBy(existedDv.getCreBy())
								.setCreOn(existedDv.getCreOn().toString())
								.setUpdBy(existedDv.getUpdBy())
								.setUpdOn(existedDv.getUpdOn().toString())
								.build();
						existedRecord.add(existCD);
						
						DataView updatedDv = updatedDataView.get(i);
						Data updatedData = Data.newBuilder().setText(updatedDv.getData()).build();
						CompleteData updateCD = CompleteData.newBuilder()
								.setDataId(updatedDv.getDataId().toString())
								.setDataType(updatedDv.getDataType())
								.setData(updatedData)
								.setEnabled(updatedDv.isEnabled())
								.setCreBy(updatedDv.getCreBy())
								.setCreOn(updatedDv.getCreOn().toString())
								.setUpdBy(updatedDv.getUpdBy())
								.setUpdOn(updatedDv.getUpdOn().toString())
								.build();
						updatedRecord.add(updateCD);
					}
					
					UpdateResponse response = UpdateResponse.newBuilder().addAllExistedRecord(existedRecord).addAllUpdatedRecord(updatedRecord).build();
					responseObserver.onNext(response);
				} catch (Exception e) {
					responseObserver.onError(e);
				}
				responseObserver.onCompleted();
			}
		};
	}

//	unary
	@Override
	public void deleteData(DeleteRequest request, StreamObserver<DeleteResponse> responseObserver) {
		if(!request.getDeleteRecord().hasDataId() && !request.getDeleteRecord().hasOther()) {
			responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Request is empty").asRuntimeException());
			responseObserver.onCompleted();
		}
		else if(request.getDeleteRecord().hasDataId()) {
			List<BigDecimal> id = request.getDeleteRecord().getDataId().getDataList().stream().map(BigDecimal::new).collect(Collectors.toList());
			List<DataView> records = dataJpaWrapper.findAllById(id);
			Boolean deleted = dataJpaWrapper.deleteAllById(id);
			
			List<CompleteData> deletedDataList = new ArrayList<>();
			for(DataView dv : records) {
				Data data = Data.newBuilder().setText(dv.getData()).build();
				CompleteData completeData = CompleteData.newBuilder()
						.setDataId(dv.getDataId().toString())
						.setDataType(dv.getDataType())
						.setData(data)
						.setEnabled(dv.isEnabled())
						.setCreBy(dv.getCreBy())
						.setCreOn(dv.getCreOn().toString())
						.setUpdBy(dv.getUpdBy())
						.setUpdOn(dv.getUpdOn().toString()).build();
				deletedDataList.add(completeData);
			}
			DeleteResponse.Builder response = DeleteResponse.newBuilder().setDeleteSuccessful(deleted.toString());
			if(!deletedDataList.isEmpty()) {
				response.addAllDeletedRecord(deletedDataList);
			}
			responseObserver.onNext(response.build());
		}
		else if(request.getDeleteRecord().hasOther()) {
			List<String> dataTypeList = new ArrayList<>();
			List<String> creByList = new ArrayList<>();
			List<String> updByList = new ArrayList<>();
			OtherField other = request.getDeleteRecord().getOther();
			
			if(!other.getDataType().getDataList().isEmpty()) {
				for(String dataType : other.getDataType().getDataList()) {
					dataTypeList.add(dataType.toUpperCase());
				}
			}
			
			Boolean enabled = null;
			if(other.hasEnabled()) {
				enabled = other.getEnabled().getValue();
			}
			
			if(!other.getCreBy().getDataList().isEmpty()) {
				for(String creBy : other.getCreBy().getDataList()) {
					creByList.add(creBy.toUpperCase());
				}
			}
			
			if(!other.getUpdBy().getDataList().isEmpty()) {
				for(String updBy : other.getUpdBy().getDataList()) {
					updByList.add(updBy.toUpperCase());
				}
			}
			
			List<DataView> records = dataJpaWrapper.findByConditions(dataTypeList, enabled, creByList, updByList);
			Boolean deleted = false;
			if(!records.isEmpty()) {
				deleted = dataJpaWrapper.deleteByConditions(dataTypeList, enabled, creByList, updByList);			
			}
			
			List<CompleteData> deletedDataList = new ArrayList<>();
			for(DataView dv : records) {
				Data data = Data.newBuilder().setText(dv.getData()).build();
				CompleteData completeData = CompleteData.newBuilder()
						.setDataId(dv.getDataId().toString())
						.setDataType(dv.getDataType())
						.setData(data)
						.setEnabled(dv.isEnabled())
						.setCreBy(dv.getCreBy())
						.setCreOn(dv.getCreOn().toString())
						.setUpdBy(dv.getUpdBy())
						.setUpdOn(dv.getUpdOn().toString()).build();
				deletedDataList.add(completeData);
			}
			DeleteResponse.Builder response = DeleteResponse.newBuilder().setDeleteSuccessful(deleted.toString());
			if(!deletedDataList.isEmpty()) {
				response.addAllDeletedRecord(deletedDataList);
			}
			responseObserver.onNext(response.build());
		}
		responseObserver.onCompleted();
	}
}
