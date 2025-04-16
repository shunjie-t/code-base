package com.all.third.grpc;

import com.all.third.CreateData;
import com.all.third.CreateRequest;
import com.all.third.CreateResponse;
import com.all.third.DataIdField;
import com.all.third.DataServiceGrpc;
import com.all.third.DeleteRequest;
import com.all.third.DeleteResponse;
import com.all.third.OtherField;
import com.all.third.ReadRequest;
import com.all.third.ReadResponse;
import com.all.third.SearchByCreBy;
import com.all.third.SearchByDataType;
import com.all.third.SearchByUpdBy;
import com.all.third.SearchField;
import com.all.third.UpdateField;
import com.all.third.UpdateRequest;
import com.all.third.UpdateResponse;
import com.all.third.common.Constants;
import com.google.protobuf.BoolValue;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.ArrayList;
import java.util.Iterator;
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
				if(!request.getCreateRecordList().isEmpty()) {
					CreateRequest.Builder newCreateRequest = CreateRequest.newBuilder();
					
					for(CreateData data : request.getCreateRecordList()) {
						CreateData.Builder newCreateData = CreateData.newBuilder();
						
						if(StringUtils.isBlank(data.getDataType())) {
							responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Data type must not be blank").asRuntimeException());
							return;
						}
						
						if(!data.getData().hasText() && !data.getData().hasNonText()) {
							responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Data must not be blank").asRuntimeException());
							return;
						}
						else if(data.getData().hasNonText()) {
							if(StringUtils.isBlank(data.getData().getNonText().getDataDesc())) {
								responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Data description must not be blank").asRuntimeException());
								return;
							}
							else if(data.getData().getNonText().getByteData() == null) {
								responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Byte data must not be blank").asRuntimeException());
								return;
							}
						}
						
						if(data.hasEnabled()) {
							newCreateData.setEnabled(data.getEnabled());
						}
						else {
							BoolValue isEnabled = BoolValue.newBuilder().setValue(true).build();
							newCreateData.setEnabled(isEnabled);
						}
						
						String creBy = data.getCreBy();
						if(StringUtils.isBlank(data.getCreBy())) {
							creBy = Constants.third;
						}
						
						newCreateData.setDataType(data.getDataType().toUpperCase())
						.setData(data.getData()).setCreBy(creBy.toUpperCase());
						newCreateRequest.addCreateRecord(newCreateData.build());
					}
					
					createRequest.add(newCreateRequest.build());
				}
				else {
					responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Request must not be empty").asRuntimeException());
				}
			}
			
			@Override
			public void onError(Throwable t) {
				responseObserver.onError(t);
			}
			
			@Override
			public void onCompleted() {
				if(!createRequest.isEmpty()) {					
					CreateResponse response = dataClient.invokeCreateData(createRequest);
					responseObserver.onNext(response);
				}
				responseObserver.onCompleted();
			}
		};
	}
	
	//server streaming
	@Override
	public void readData(ReadRequest request, StreamObserver<ReadResponse> responseObserver) {
		if(request.getReadRecord().hasDataId() || request.getReadRecord().hasOther()) {
			Iterator<ReadResponse> response = null;

			if(request.getReadRecord().hasDataId()) {
				response = dataClient.invokeReadData(request);
			}
			else if (request.getReadRecord().hasOther()) {
				ReadRequest.Builder newReadRequest = ReadRequest.newBuilder();
				OtherField other = request.getReadRecord().getOther();
				OtherField.Builder newOtherField = OtherField.newBuilder();
				
				SearchByDataType.Builder sbdt = SearchByDataType.newBuilder();
				if(other.hasDataType()) {
					for(String data : other.getDataType().getDataList()) {
						sbdt.addData(data.toUpperCase());
					}
				}
				newOtherField.setDataType(sbdt);
				
				if(other.hasEnabled()) {
					newOtherField.setEnabled(other.getEnabled());
				}
				
				SearchByCreBy.Builder sbcf = SearchByCreBy.newBuilder();
				if(other.hasCreBy()) {
					for(String data : other.getCreBy().getDataList()) {
						sbcf.addData(data.toUpperCase());
					}
				}
				newOtherField.setCreBy(sbcf);
				
				SearchByUpdBy.Builder sbub = SearchByUpdBy.newBuilder();
				if(other.hasUpdBy()) {
					for(String data : other.getUpdBy().getDataList()) {
						sbub.addData(data.toUpperCase());
					}
				}
				newOtherField.setUpdBy(sbub);
				
				SearchField.Builder newSearchField = SearchField.newBuilder();
				newSearchField.setOther(newOtherField);
				newReadRequest.setReadRecord(newSearchField);
				
				response = dataClient.invokeReadData(newReadRequest.build());
			}
			
			while(response.hasNext()) {
				responseObserver.onNext(response.next());
			}
		}
		else {
			responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Request must not be empty").asRuntimeException());
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
				 if(!request.getSearchRecord().hasDataId() && !request.getSearchRecord().hasOther()) {
					 responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Search update field must not be empty").asRuntimeException());
				 }
				 else if(!request.hasUpdateRecord()) {
					 responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("There must be at least one data to be updated").asRuntimeException());
				 }
				 else {
					 UpdateRequest.Builder newRequest = UpdateRequest.newBuilder();
					 SearchField.Builder newSearchField = SearchField.newBuilder();
					 
					 if(request.getSearchRecord().hasDataId()) {
						 DataIdField.Builder newDataIdField = DataIdField.newBuilder();
						 newDataIdField.addAllData(request.getSearchRecord().getDataId().getDataList());
						 newSearchField.setDataId(newDataIdField);
					 }
					 else if(request.getSearchRecord().hasOther()) {
						 OtherField.Builder newOtherField = OtherField.newBuilder();
						 OtherField otherField = request.getSearchRecord().getOther();
						 
						 if(otherField.hasDataType()) {
							 SearchByDataType.Builder sbdt = SearchByDataType.newBuilder();
							 for(String dataType : otherField.getDataType().getDataList()) {
								 sbdt.addData(dataType.toUpperCase());
							 }
							 newOtherField.setDataType(sbdt);
						 }
						 
						 if(otherField.hasEnabled()) {
							 newOtherField.setEnabled(otherField.getEnabled()); 
						 }
						 
						 if(otherField.hasCreBy()) {
							 SearchByCreBy.Builder sbcb = SearchByCreBy.newBuilder();
							 for(String creBy : otherField.getCreBy().getDataList()) {
								 sbcb.addData(creBy.toUpperCase());
							 }
							 newOtherField.setCreBy(sbcb);
						 }
						 
						 if(otherField.hasUpdBy()) {
							 SearchByUpdBy.Builder sbub = SearchByUpdBy.newBuilder();
							 for(String updBy : otherField.getCreBy().getDataList()) {
								 sbub.addData(updBy.toUpperCase());
							 }
							 newOtherField.setUpdBy(sbub);
						 }
						 
						 newSearchField.setOther(newOtherField);
					 }
					 newRequest.setSearchRecord(newSearchField);
					 
					 UpdateField.Builder newUpdateField = UpdateField.newBuilder();
					 if(request.getUpdateRecord().hasData()) {
						 newUpdateField.setData(request.getUpdateRecord().getData());
					 }
					 
					 if(request.getUpdateRecord().hasDataType()) {
						 newUpdateField.setDataType(request.getUpdateRecord().getDataType().toUpperCase());
					 }
					 
					 if(request.getUpdateRecord().hasEnabled()) {
						 boolean isEnabled = request.getUpdateRecord().getEnabled().getValue() ? true : false;
						 BoolValue enabled = BoolValue.newBuilder().setValue(isEnabled).build();
						 newUpdateField.setEnabled(enabled);
					 }
					 
					 if(request.getUpdateRecord().hasUpdBy()) {
						 newUpdateField.setUpdBy(request.getUpdateRecord().getUpdBy().toUpperCase());
					 }
					 else {
						 newUpdateField.setUpdBy(Constants.third);
					 }
					 newRequest.setUpdateRecord(newUpdateField);
					 
					 updateRequest.add(newRequest.build());
				 }
			}

			@Override
			public void onError(Throwable t) {
				responseObserver.onError(t);
			}

			@Override
			public void onCompleted() {
				if(!updateRequest.isEmpty()) {
					UpdateResponse response = dataClient.invokeUpdateData(updateRequest);
					responseObserver.onNext(response);
				}
				responseObserver.onCompleted();
			}
		};
	}
	
	//unary
	@Override
	public void deleteData(DeleteRequest request, StreamObserver<DeleteResponse> responseObserver) {
		if(request.getDeleteRecord().hasDataId() || request.getDeleteRecord().hasOther()) {
			DeleteResponse response = null;
			
			if(request.getDeleteRecord().hasDataId()) {
				response = dataClient.invokeDeleteData(request);
			}
			else if(request.getDeleteRecord().hasOther()) {
				OtherField other = request.getDeleteRecord().getOther();				
				OtherField.Builder newOtherField = OtherField.newBuilder();
				
				SearchByDataType.Builder sbdt = SearchByDataType.newBuilder();
				if(other.hasDataType()) {
					for(String data : other.getDataType().getDataList()) {
						sbdt.addData(data.toUpperCase());
					}
				}
				newOtherField.setDataType(sbdt);
				
				if(other.hasEnabled()) {
					newOtherField.setEnabled(other.getEnabled());
				}
				
				SearchByCreBy.Builder sbcf = SearchByCreBy.newBuilder();
				if(other.hasCreBy()) {
					for(String data : other.getCreBy().getDataList()) {
						sbcf.addData(data.toUpperCase());
					}
				}
				newOtherField.setCreBy(sbcf);
				
				SearchByUpdBy.Builder sbub = SearchByUpdBy.newBuilder();
				if(other.hasUpdBy()) {
					for(String data : other.getUpdBy().getDataList()) {
						sbub.addData(data.toUpperCase());
					}
				}
				newOtherField.setUpdBy(sbub);

				SearchField.Builder newSearchField = SearchField.newBuilder();
				newSearchField.setOther(newOtherField);
				DeleteRequest.Builder newDeleteRequest = DeleteRequest.newBuilder();
				newDeleteRequest.setDeleteRecord(newSearchField);
				response = dataClient.invokeDeleteData(newDeleteRequest.build());
			}
			responseObserver.onNext(response);
		}
		else {
			responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Request must not be empty").asRuntimeException());
		}
		responseObserver.onCompleted();
	}
}
