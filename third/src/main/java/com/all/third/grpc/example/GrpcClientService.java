package com.all.third.grpc.example;

//import java.util.concurrent.CountDownLatch;
//
//import org.springframework.stereotype.Service;
//
//import com.all.third.BiRequest;
//import com.all.third.BiResponse;
//import com.all.third.ClientRequest;
//import com.all.third.ClientResponse;
//import com.all.third.ServerRequest;
//import com.all.third.StreamingServiceGrpc;
//import com.all.third.UnaryRequest;
//import com.all.third.UnaryResponse;
//
//import io.grpc.stub.StreamObserver;
//import net.devh.boot.grpc.client.inject.GrpcClient;
//
//@Service
public class GrpcClientService {
//	@GrpcClient("thirdClient")
//	private StreamingServiceGrpc.StreamingServiceBlockingStub blockingStub;
//	
//	@GrpcClient("thirdClient")
//	private StreamingServiceGrpc.StreamingServiceStub asyncStub;
//	
//	public String unaryCall(String message) {
//        UnaryRequest request = UnaryRequest.newBuilder().setMessage(message).build();
//        UnaryResponse response = blockingStub.unaryCall(request);
//        return response.getMessage();
//    }
//
//    public int clientStreamingCall(int[] numbers) throws InterruptedException {
//        CountDownLatch latch = new CountDownLatch(1);
//        final int[] sum = {0};
//
//        StreamObserver<ClientRequest> requestObserver = asyncStub.clientStreamingCall(new StreamObserver<>() {
//            @Override
//            public void onNext(ClientResponse response) {
//                sum[0] = response.getSum();
//            }
//
//            @Override
//            public void onError(Throwable t) {
//                t.printStackTrace();
//                latch.countDown();
//            }
//
//            @Override
//            public void onCompleted() {
//                latch.countDown();
//            }
//        });
//
//        for (int number : numbers) {
//            requestObserver.onNext(ClientRequest.newBuilder().setNumber(number).build());
//        }
//        requestObserver.onCompleted();
//        latch.await();
//        return sum[0];
//    }
//
//    public void serverStreamingCall(String prefix) {
//        ServerRequest request = ServerRequest.newBuilder().setPrefix(prefix).build();
//        blockingStub.serverStreamingCall(request).forEachRemaining(response ->
//                System.out.println("Received: " + response.getMessage())
//        );
//    }
//
//    public void bidirectionalStreamingCall(String[] messages) throws InterruptedException {
//        CountDownLatch latch = new CountDownLatch(1);
//
//        StreamObserver<BiRequest> requestObserver = asyncStub.bidirectionalStreamingCall(new StreamObserver<>() {
//            @Override
//            public void onNext(BiResponse response) {
//                System.out.println("Received: " + response.getMessage());
//            }
//
//            @Override
//            public void onError(Throwable t) {
//                t.printStackTrace();
//                latch.countDown();
//            }
//
//            @Override
//            public void onCompleted() {
//                latch.countDown();
//            }
//        });
//
//        for (String message : messages) {
//            requestObserver.onNext(BiRequest.newBuilder().setMessage(message).build());
//        }
//        requestObserver.onCompleted();
//        latch.await();
//    }
}
