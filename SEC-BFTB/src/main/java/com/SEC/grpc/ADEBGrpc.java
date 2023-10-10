package com.SEC.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.15.0)",
    comments = "Source: ADEB.proto")
public final class ADEBGrpc {

  private ADEBGrpc() {}

  public static final String SERVICE_NAME = "ADEB";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.SEC.grpc.ADEBOuterClass.EchoRequest,
      com.SEC.grpc.ADEBOuterClass.EchoResponse> getEchoMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "echo",
      requestType = com.SEC.grpc.ADEBOuterClass.EchoRequest.class,
      responseType = com.SEC.grpc.ADEBOuterClass.EchoResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.SEC.grpc.ADEBOuterClass.EchoRequest,
      com.SEC.grpc.ADEBOuterClass.EchoResponse> getEchoMethod() {
    io.grpc.MethodDescriptor<com.SEC.grpc.ADEBOuterClass.EchoRequest, com.SEC.grpc.ADEBOuterClass.EchoResponse> getEchoMethod;
    if ((getEchoMethod = ADEBGrpc.getEchoMethod) == null) {
      synchronized (ADEBGrpc.class) {
        if ((getEchoMethod = ADEBGrpc.getEchoMethod) == null) {
          ADEBGrpc.getEchoMethod = getEchoMethod = 
              io.grpc.MethodDescriptor.<com.SEC.grpc.ADEBOuterClass.EchoRequest, com.SEC.grpc.ADEBOuterClass.EchoResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "ADEB", "echo"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.SEC.grpc.ADEBOuterClass.EchoRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.SEC.grpc.ADEBOuterClass.EchoResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new ADEBMethodDescriptorSupplier("echo"))
                  .build();
          }
        }
     }
     return getEchoMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.SEC.grpc.ADEBOuterClass.ReadyRequest,
      com.SEC.grpc.ADEBOuterClass.ReadyResponse> getReadyMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ready",
      requestType = com.SEC.grpc.ADEBOuterClass.ReadyRequest.class,
      responseType = com.SEC.grpc.ADEBOuterClass.ReadyResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.SEC.grpc.ADEBOuterClass.ReadyRequest,
      com.SEC.grpc.ADEBOuterClass.ReadyResponse> getReadyMethod() {
    io.grpc.MethodDescriptor<com.SEC.grpc.ADEBOuterClass.ReadyRequest, com.SEC.grpc.ADEBOuterClass.ReadyResponse> getReadyMethod;
    if ((getReadyMethod = ADEBGrpc.getReadyMethod) == null) {
      synchronized (ADEBGrpc.class) {
        if ((getReadyMethod = ADEBGrpc.getReadyMethod) == null) {
          ADEBGrpc.getReadyMethod = getReadyMethod = 
              io.grpc.MethodDescriptor.<com.SEC.grpc.ADEBOuterClass.ReadyRequest, com.SEC.grpc.ADEBOuterClass.ReadyResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "ADEB", "ready"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.SEC.grpc.ADEBOuterClass.ReadyRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.SEC.grpc.ADEBOuterClass.ReadyResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new ADEBMethodDescriptorSupplier("ready"))
                  .build();
          }
        }
     }
     return getReadyMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ADEBStub newStub(io.grpc.Channel channel) {
    return new ADEBStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ADEBBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new ADEBBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ADEBFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new ADEBFutureStub(channel);
  }

  /**
   */
  public static abstract class ADEBImplBase implements io.grpc.BindableService {

    /**
     */
    public void echo(com.SEC.grpc.ADEBOuterClass.EchoRequest request,
        io.grpc.stub.StreamObserver<com.SEC.grpc.ADEBOuterClass.EchoResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getEchoMethod(), responseObserver);
    }

    /**
     */
    public void ready(com.SEC.grpc.ADEBOuterClass.ReadyRequest request,
        io.grpc.stub.StreamObserver<com.SEC.grpc.ADEBOuterClass.ReadyResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getReadyMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getEchoMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.SEC.grpc.ADEBOuterClass.EchoRequest,
                com.SEC.grpc.ADEBOuterClass.EchoResponse>(
                  this, METHODID_ECHO)))
          .addMethod(
            getReadyMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.SEC.grpc.ADEBOuterClass.ReadyRequest,
                com.SEC.grpc.ADEBOuterClass.ReadyResponse>(
                  this, METHODID_READY)))
          .build();
    }
  }

  /**
   */
  public static final class ADEBStub extends io.grpc.stub.AbstractStub<ADEBStub> {
    private ADEBStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ADEBStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ADEBStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ADEBStub(channel, callOptions);
    }

    /**
     */
    public void echo(com.SEC.grpc.ADEBOuterClass.EchoRequest request,
        io.grpc.stub.StreamObserver<com.SEC.grpc.ADEBOuterClass.EchoResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getEchoMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void ready(com.SEC.grpc.ADEBOuterClass.ReadyRequest request,
        io.grpc.stub.StreamObserver<com.SEC.grpc.ADEBOuterClass.ReadyResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getReadyMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class ADEBBlockingStub extends io.grpc.stub.AbstractStub<ADEBBlockingStub> {
    private ADEBBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ADEBBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ADEBBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ADEBBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.SEC.grpc.ADEBOuterClass.EchoResponse echo(com.SEC.grpc.ADEBOuterClass.EchoRequest request) {
      return blockingUnaryCall(
          getChannel(), getEchoMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.SEC.grpc.ADEBOuterClass.ReadyResponse ready(com.SEC.grpc.ADEBOuterClass.ReadyRequest request) {
      return blockingUnaryCall(
          getChannel(), getReadyMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class ADEBFutureStub extends io.grpc.stub.AbstractStub<ADEBFutureStub> {
    private ADEBFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ADEBFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ADEBFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ADEBFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.SEC.grpc.ADEBOuterClass.EchoResponse> echo(
        com.SEC.grpc.ADEBOuterClass.EchoRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getEchoMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.SEC.grpc.ADEBOuterClass.ReadyResponse> ready(
        com.SEC.grpc.ADEBOuterClass.ReadyRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getReadyMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_ECHO = 0;
  private static final int METHODID_READY = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final ADEBImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(ADEBImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_ECHO:
          serviceImpl.echo((com.SEC.grpc.ADEBOuterClass.EchoRequest) request,
              (io.grpc.stub.StreamObserver<com.SEC.grpc.ADEBOuterClass.EchoResponse>) responseObserver);
          break;
        case METHODID_READY:
          serviceImpl.ready((com.SEC.grpc.ADEBOuterClass.ReadyRequest) request,
              (io.grpc.stub.StreamObserver<com.SEC.grpc.ADEBOuterClass.ReadyResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class ADEBBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ADEBBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.SEC.grpc.ADEBOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ADEB");
    }
  }

  private static final class ADEBFileDescriptorSupplier
      extends ADEBBaseDescriptorSupplier {
    ADEBFileDescriptorSupplier() {}
  }

  private static final class ADEBMethodDescriptorSupplier
      extends ADEBBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    ADEBMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (ADEBGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ADEBFileDescriptorSupplier())
              .addMethod(getEchoMethod())
              .addMethod(getReadyMethod())
              .build();
        }
      }
    }
    return result;
  }
}
