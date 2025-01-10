package com.all.fourth.grpc;

//import java.io.ByteArrayOutputStream;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.all.fourth.CreateRequest;
//import com.all.fourth.CreateResponse;
//import com.all.fourth.DataServiceGrpc;
//import com.all.fourth.common.Constants;
//import com.google.protobuf.ByteString;
//
//import io.grpc.Status;
//import io.grpc.stub.StreamObserver;
//import net.devh.boot.grpc.server.service.GrpcService;

public class DataServiceImpl {
//@GrpcService
//public class DataServiceImpl extends DataServiceGrpc.DataServiceImplBase {
//	@Autowired
//	private DataClient dataClient;
//	
////	client streaming
//	@Override
//	public StreamObserver<CreateRequest> createData(StreamObserver<CreateResponse> responseObserver) {
//		return new StreamObserver<CreateRequest>() {
//			private ByteArrayOutputStream byteData;
//			private CreateRequest createRequest = null;
//
//			@Override
//			public void onNext(CreateRequest request) {
//				if(StringUtils.isBlank(request.getDataType())) {
//					responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Data type must not be blank").asRuntimeException());
//					return;
//				}
//				
//				if(request.getData().hasNonText()) {
//					if(StringUtils.isBlank(request.getData().getNonText().getDataDesc())) {
//						responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Data description must not be blank").asRuntimeException());
//						return;
//					}
//					else if(request.getData().getNonText().getByteData() == null) {
//						responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Byte data must not be blank").asRuntimeException());
//						return;
//					}
//					ByteString chunkData = request.getData().getNonText().getByteData();
//				}
//				else if(!request.getData().hasText()) {
//					responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Data must not be blank").asRuntimeException());
//					return;
//				}
//
//				if(StringUtils.isBlank(request.getCreBy())) {
//					createRequest = CreateRequest.newBuilder().setDataType(request.getDataType()).setData(request.getData()).setEnabled(request.getEnabled()).setCreBy(Constants.third).build();
//				}
//			}
//
//			@Override
//			public void onError(Throwable t) {
//				System.out.println(t.getMessage());
//			}
//
//			@Override
//			public void onCompleted() {
////				call client
//				CreateResponse response = dataClient.invokeCreateData(createRequest);
//				responseObserver.onNext(response);
//				responseObserver.onCompleted();
//			}
//		};
//	}

////server streaming
//@Override
//public void readData(ReadRequest request, StreamObserver<ReadResponse> responseObserver) {
//
//}
//
////bidirectional streaming
//@Override
//public StreamObserver<UpdateRequest> updateData(StreamObserver<UpdateResponse> responseObserver) {
//	return null;
//}
//
////unary
//@Override
//public void deleteData(DeleteRequest request, StreamObserver<DeleteResponse> responseObserver) {
//
//}
}
