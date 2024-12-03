package com.all.third.grpc.example;

//import com.all.third.BiRequest;
//import com.all.third.BiResponse;
//import com.all.third.ClientRequest;
//import com.all.third.ClientResponse;
//import com.all.third.ServerRequest;
//import com.all.third.ServerResponse;
//import com.all.third.StreamingServiceGrpc;
//import com.all.third.UnaryRequest;
//import com.all.third.UnaryResponse;

//import io.grpc.stub.StreamObserver;
//import net.devh.boot.grpc.server.service.GrpcService;

//@GrpcService
public class StreamingServiceImpl {
//public class StreamingServiceImpl extends StreamingServiceGrpc.StreamingServiceImplBase {
//	@Override
//	public void unaryCall(UnaryRequest request, StreamObserver<UnaryResponse> responseObserver) {
//		String message = "Hello, " + request.getMessage();
//		UnaryResponse response = UnaryResponse.newBuilder().setMessage(message).build();
//		responseObserver.onNext(response);
//		responseObserver.onCompleted();
//	}
//	
//	@Override
//	public StreamObserver<ClientRequest> clientStreamingCall(StreamObserver<ClientResponse> responseObserver) {
//		return new StreamObserver<ClientRequest>() {
//			int sum = 0;
//			
//			@Override
//			public void onNext(ClientRequest request) {
//				sum += request.getNumber();
//			}
//			
//			@Override
//			public void onError(Throwable t) {
//				t.printStackTrace();
//			}
//			
//			@Override
//			public void onCompleted() {
//				ClientResponse response = ClientResponse.newBuilder().setSum(sum).build();
//				responseObserver.onNext(response);
//				responseObserver.onCompleted();
//			}
//		};
//	}
//	
//	@Override
//	public void serverStreamingCall(ServerRequest request, StreamObserver<ServerResponse> responseObserver) {
//		for(int i = 0; i <= 5; i++) {
//			ServerResponse response = ServerResponse.newBuilder().setMessage(request.getPrefix() + " Message " + i).build();
//			responseObserver.onNext(response);
//		}
//		responseObserver.onCompleted();
//	}
//	
//	@Override
//	public StreamObserver<BiRequest> bidirectionalStreamingCall(StreamObserver<BiResponse> responseObserver) {
//		return new StreamObserver<>() {
//			public void onNext(BiRequest request) {
//				String responseMessage = "Echo: " + request.getMessage();
//				BiResponse response = BiResponse.newBuilder().setMessage(responseMessage).build();
//				responseObserver.onNext(response);
//			}
//			
//			public void onError(Throwable t) {
//				t.printStackTrace();
//			}
//			
//			public void onCompleted() {
//				responseObserver.onCompleted();
//			}
//		};
//	}
}
