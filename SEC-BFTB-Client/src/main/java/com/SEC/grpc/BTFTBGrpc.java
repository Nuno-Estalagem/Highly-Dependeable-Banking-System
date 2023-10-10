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
    comments = "Source: BFTB.proto")
public final class BTFTBGrpc {

  private BTFTBGrpc() {}

  public static final String SERVICE_NAME = "BTFTB";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.SEC.grpc.BFTB.GetRidRequest,
      com.SEC.grpc.BFTB.RidResponse> getGetRidMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getRid",
      requestType = com.SEC.grpc.BFTB.GetRidRequest.class,
      responseType = com.SEC.grpc.BFTB.RidResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.SEC.grpc.BFTB.GetRidRequest,
      com.SEC.grpc.BFTB.RidResponse> getGetRidMethod() {
    io.grpc.MethodDescriptor<com.SEC.grpc.BFTB.GetRidRequest, com.SEC.grpc.BFTB.RidResponse> getGetRidMethod;
    if ((getGetRidMethod = BTFTBGrpc.getGetRidMethod) == null) {
      synchronized (BTFTBGrpc.class) {
        if ((getGetRidMethod = BTFTBGrpc.getGetRidMethod) == null) {
          BTFTBGrpc.getGetRidMethod = getGetRidMethod = 
              io.grpc.MethodDescriptor.<com.SEC.grpc.BFTB.GetRidRequest, com.SEC.grpc.BFTB.RidResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "BTFTB", "getRid"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.SEC.grpc.BFTB.GetRidRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.SEC.grpc.BFTB.RidResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new BTFTBMethodDescriptorSupplier("getRid"))
                  .build();
          }
        }
     }
     return getGetRidMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.SEC.grpc.BFTB.OpenAccountRequest,
      com.SEC.grpc.BFTB.APIresponse> getOpenAccountMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "open_account",
      requestType = com.SEC.grpc.BFTB.OpenAccountRequest.class,
      responseType = com.SEC.grpc.BFTB.APIresponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.SEC.grpc.BFTB.OpenAccountRequest,
      com.SEC.grpc.BFTB.APIresponse> getOpenAccountMethod() {
    io.grpc.MethodDescriptor<com.SEC.grpc.BFTB.OpenAccountRequest, com.SEC.grpc.BFTB.APIresponse> getOpenAccountMethod;
    if ((getOpenAccountMethod = BTFTBGrpc.getOpenAccountMethod) == null) {
      synchronized (BTFTBGrpc.class) {
        if ((getOpenAccountMethod = BTFTBGrpc.getOpenAccountMethod) == null) {
          BTFTBGrpc.getOpenAccountMethod = getOpenAccountMethod = 
              io.grpc.MethodDescriptor.<com.SEC.grpc.BFTB.OpenAccountRequest, com.SEC.grpc.BFTB.APIresponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "BTFTB", "open_account"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.SEC.grpc.BFTB.OpenAccountRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.SEC.grpc.BFTB.APIresponse.getDefaultInstance()))
                  .setSchemaDescriptor(new BTFTBMethodDescriptorSupplier("open_account"))
                  .build();
          }
        }
     }
     return getOpenAccountMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.SEC.grpc.BFTB.SendAmountRequest,
      com.SEC.grpc.BFTB.SendAmountResponse> getSendAmountMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "send_amount",
      requestType = com.SEC.grpc.BFTB.SendAmountRequest.class,
      responseType = com.SEC.grpc.BFTB.SendAmountResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.SEC.grpc.BFTB.SendAmountRequest,
      com.SEC.grpc.BFTB.SendAmountResponse> getSendAmountMethod() {
    io.grpc.MethodDescriptor<com.SEC.grpc.BFTB.SendAmountRequest, com.SEC.grpc.BFTB.SendAmountResponse> getSendAmountMethod;
    if ((getSendAmountMethod = BTFTBGrpc.getSendAmountMethod) == null) {
      synchronized (BTFTBGrpc.class) {
        if ((getSendAmountMethod = BTFTBGrpc.getSendAmountMethod) == null) {
          BTFTBGrpc.getSendAmountMethod = getSendAmountMethod = 
              io.grpc.MethodDescriptor.<com.SEC.grpc.BFTB.SendAmountRequest, com.SEC.grpc.BFTB.SendAmountResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "BTFTB", "send_amount"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.SEC.grpc.BFTB.SendAmountRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.SEC.grpc.BFTB.SendAmountResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new BTFTBMethodDescriptorSupplier("send_amount"))
                  .build();
          }
        }
     }
     return getSendAmountMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.SEC.grpc.BFTB.CheckAccountRequest,
      com.SEC.grpc.BFTB.AccountResponse> getCheckAccountMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "check_account",
      requestType = com.SEC.grpc.BFTB.CheckAccountRequest.class,
      responseType = com.SEC.grpc.BFTB.AccountResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.SEC.grpc.BFTB.CheckAccountRequest,
      com.SEC.grpc.BFTB.AccountResponse> getCheckAccountMethod() {
    io.grpc.MethodDescriptor<com.SEC.grpc.BFTB.CheckAccountRequest, com.SEC.grpc.BFTB.AccountResponse> getCheckAccountMethod;
    if ((getCheckAccountMethod = BTFTBGrpc.getCheckAccountMethod) == null) {
      synchronized (BTFTBGrpc.class) {
        if ((getCheckAccountMethod = BTFTBGrpc.getCheckAccountMethod) == null) {
          BTFTBGrpc.getCheckAccountMethod = getCheckAccountMethod = 
              io.grpc.MethodDescriptor.<com.SEC.grpc.BFTB.CheckAccountRequest, com.SEC.grpc.BFTB.AccountResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "BTFTB", "check_account"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.SEC.grpc.BFTB.CheckAccountRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.SEC.grpc.BFTB.AccountResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new BTFTBMethodDescriptorSupplier("check_account"))
                  .build();
          }
        }
     }
     return getCheckAccountMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.SEC.grpc.BFTB.ReceiveAmountRequest,
      com.SEC.grpc.BFTB.ReceiveAmountResponse> getReceiveAmountMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "receive_amount",
      requestType = com.SEC.grpc.BFTB.ReceiveAmountRequest.class,
      responseType = com.SEC.grpc.BFTB.ReceiveAmountResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.SEC.grpc.BFTB.ReceiveAmountRequest,
      com.SEC.grpc.BFTB.ReceiveAmountResponse> getReceiveAmountMethod() {
    io.grpc.MethodDescriptor<com.SEC.grpc.BFTB.ReceiveAmountRequest, com.SEC.grpc.BFTB.ReceiveAmountResponse> getReceiveAmountMethod;
    if ((getReceiveAmountMethod = BTFTBGrpc.getReceiveAmountMethod) == null) {
      synchronized (BTFTBGrpc.class) {
        if ((getReceiveAmountMethod = BTFTBGrpc.getReceiveAmountMethod) == null) {
          BTFTBGrpc.getReceiveAmountMethod = getReceiveAmountMethod = 
              io.grpc.MethodDescriptor.<com.SEC.grpc.BFTB.ReceiveAmountRequest, com.SEC.grpc.BFTB.ReceiveAmountResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "BTFTB", "receive_amount"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.SEC.grpc.BFTB.ReceiveAmountRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.SEC.grpc.BFTB.ReceiveAmountResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new BTFTBMethodDescriptorSupplier("receive_amount"))
                  .build();
          }
        }
     }
     return getReceiveAmountMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.SEC.grpc.BFTB.AuditRequest,
      com.SEC.grpc.BFTB.AuditTransactionResponse> getAuditMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "audit",
      requestType = com.SEC.grpc.BFTB.AuditRequest.class,
      responseType = com.SEC.grpc.BFTB.AuditTransactionResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.SEC.grpc.BFTB.AuditRequest,
      com.SEC.grpc.BFTB.AuditTransactionResponse> getAuditMethod() {
    io.grpc.MethodDescriptor<com.SEC.grpc.BFTB.AuditRequest, com.SEC.grpc.BFTB.AuditTransactionResponse> getAuditMethod;
    if ((getAuditMethod = BTFTBGrpc.getAuditMethod) == null) {
      synchronized (BTFTBGrpc.class) {
        if ((getAuditMethod = BTFTBGrpc.getAuditMethod) == null) {
          BTFTBGrpc.getAuditMethod = getAuditMethod = 
              io.grpc.MethodDescriptor.<com.SEC.grpc.BFTB.AuditRequest, com.SEC.grpc.BFTB.AuditTransactionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "BTFTB", "audit"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.SEC.grpc.BFTB.AuditRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.SEC.grpc.BFTB.AuditTransactionResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new BTFTBMethodDescriptorSupplier("audit"))
                  .build();
          }
        }
     }
     return getAuditMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.SEC.grpc.BFTB.CAWBRequest,
      com.SEC.grpc.BFTB.CAWBResponse> getCheckAccountWbMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "check_account_wb",
      requestType = com.SEC.grpc.BFTB.CAWBRequest.class,
      responseType = com.SEC.grpc.BFTB.CAWBResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.SEC.grpc.BFTB.CAWBRequest,
      com.SEC.grpc.BFTB.CAWBResponse> getCheckAccountWbMethod() {
    io.grpc.MethodDescriptor<com.SEC.grpc.BFTB.CAWBRequest, com.SEC.grpc.BFTB.CAWBResponse> getCheckAccountWbMethod;
    if ((getCheckAccountWbMethod = BTFTBGrpc.getCheckAccountWbMethod) == null) {
      synchronized (BTFTBGrpc.class) {
        if ((getCheckAccountWbMethod = BTFTBGrpc.getCheckAccountWbMethod) == null) {
          BTFTBGrpc.getCheckAccountWbMethod = getCheckAccountWbMethod = 
              io.grpc.MethodDescriptor.<com.SEC.grpc.BFTB.CAWBRequest, com.SEC.grpc.BFTB.CAWBResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "BTFTB", "check_account_wb"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.SEC.grpc.BFTB.CAWBRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.SEC.grpc.BFTB.CAWBResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new BTFTBMethodDescriptorSupplier("check_account_wb"))
                  .build();
          }
        }
     }
     return getCheckAccountWbMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.SEC.grpc.BFTB.ADWBRequest,
      com.SEC.grpc.BFTB.ADWBResponse> getAuditWbMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "audit_wb",
      requestType = com.SEC.grpc.BFTB.ADWBRequest.class,
      responseType = com.SEC.grpc.BFTB.ADWBResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.SEC.grpc.BFTB.ADWBRequest,
      com.SEC.grpc.BFTB.ADWBResponse> getAuditWbMethod() {
    io.grpc.MethodDescriptor<com.SEC.grpc.BFTB.ADWBRequest, com.SEC.grpc.BFTB.ADWBResponse> getAuditWbMethod;
    if ((getAuditWbMethod = BTFTBGrpc.getAuditWbMethod) == null) {
      synchronized (BTFTBGrpc.class) {
        if ((getAuditWbMethod = BTFTBGrpc.getAuditWbMethod) == null) {
          BTFTBGrpc.getAuditWbMethod = getAuditWbMethod = 
              io.grpc.MethodDescriptor.<com.SEC.grpc.BFTB.ADWBRequest, com.SEC.grpc.BFTB.ADWBResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "BTFTB", "audit_wb"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.SEC.grpc.BFTB.ADWBRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.SEC.grpc.BFTB.ADWBResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new BTFTBMethodDescriptorSupplier("audit_wb"))
                  .build();
          }
        }
     }
     return getAuditWbMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.SEC.grpc.BFTB.PowRequest,
      com.SEC.grpc.BFTB.PowResponse> getPOWRequestMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "POW_Request",
      requestType = com.SEC.grpc.BFTB.PowRequest.class,
      responseType = com.SEC.grpc.BFTB.PowResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.SEC.grpc.BFTB.PowRequest,
      com.SEC.grpc.BFTB.PowResponse> getPOWRequestMethod() {
    io.grpc.MethodDescriptor<com.SEC.grpc.BFTB.PowRequest, com.SEC.grpc.BFTB.PowResponse> getPOWRequestMethod;
    if ((getPOWRequestMethod = BTFTBGrpc.getPOWRequestMethod) == null) {
      synchronized (BTFTBGrpc.class) {
        if ((getPOWRequestMethod = BTFTBGrpc.getPOWRequestMethod) == null) {
          BTFTBGrpc.getPOWRequestMethod = getPOWRequestMethod = 
              io.grpc.MethodDescriptor.<com.SEC.grpc.BFTB.PowRequest, com.SEC.grpc.BFTB.PowResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "BTFTB", "POW_Request"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.SEC.grpc.BFTB.PowRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.SEC.grpc.BFTB.PowResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new BTFTBMethodDescriptorSupplier("POW_Request"))
                  .build();
          }
        }
     }
     return getPOWRequestMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static BTFTBStub newStub(io.grpc.Channel channel) {
    return new BTFTBStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static BTFTBBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new BTFTBBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static BTFTBFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new BTFTBFutureStub(channel);
  }

  /**
   */
  public static abstract class BTFTBImplBase implements io.grpc.BindableService {

    /**
     */
    public void getRid(com.SEC.grpc.BFTB.GetRidRequest request,
        io.grpc.stub.StreamObserver<com.SEC.grpc.BFTB.RidResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetRidMethod(), responseObserver);
    }

    /**
     */
    public void openAccount(com.SEC.grpc.BFTB.OpenAccountRequest request,
        io.grpc.stub.StreamObserver<com.SEC.grpc.BFTB.APIresponse> responseObserver) {
      asyncUnimplementedUnaryCall(getOpenAccountMethod(), responseObserver);
    }

    /**
     */
    public void sendAmount(com.SEC.grpc.BFTB.SendAmountRequest request,
        io.grpc.stub.StreamObserver<com.SEC.grpc.BFTB.SendAmountResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getSendAmountMethod(), responseObserver);
    }

    /**
     */
    public void checkAccount(com.SEC.grpc.BFTB.CheckAccountRequest request,
        io.grpc.stub.StreamObserver<com.SEC.grpc.BFTB.AccountResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getCheckAccountMethod(), responseObserver);
    }

    /**
     */
    public void receiveAmount(com.SEC.grpc.BFTB.ReceiveAmountRequest request,
        io.grpc.stub.StreamObserver<com.SEC.grpc.BFTB.ReceiveAmountResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getReceiveAmountMethod(), responseObserver);
    }

    /**
     */
    public void audit(com.SEC.grpc.BFTB.AuditRequest request,
        io.grpc.stub.StreamObserver<com.SEC.grpc.BFTB.AuditTransactionResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getAuditMethod(), responseObserver);
    }

    /**
     */
    public void checkAccountWb(com.SEC.grpc.BFTB.CAWBRequest request,
        io.grpc.stub.StreamObserver<com.SEC.grpc.BFTB.CAWBResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getCheckAccountWbMethod(), responseObserver);
    }

    /**
     */
    public void auditWb(com.SEC.grpc.BFTB.ADWBRequest request,
        io.grpc.stub.StreamObserver<com.SEC.grpc.BFTB.ADWBResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getAuditWbMethod(), responseObserver);
    }

    /**
     */
    public void pOWRequest(com.SEC.grpc.BFTB.PowRequest request,
        io.grpc.stub.StreamObserver<com.SEC.grpc.BFTB.PowResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getPOWRequestMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetRidMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.SEC.grpc.BFTB.GetRidRequest,
                com.SEC.grpc.BFTB.RidResponse>(
                  this, METHODID_GET_RID)))
          .addMethod(
            getOpenAccountMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.SEC.grpc.BFTB.OpenAccountRequest,
                com.SEC.grpc.BFTB.APIresponse>(
                  this, METHODID_OPEN_ACCOUNT)))
          .addMethod(
            getSendAmountMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.SEC.grpc.BFTB.SendAmountRequest,
                com.SEC.grpc.BFTB.SendAmountResponse>(
                  this, METHODID_SEND_AMOUNT)))
          .addMethod(
            getCheckAccountMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.SEC.grpc.BFTB.CheckAccountRequest,
                com.SEC.grpc.BFTB.AccountResponse>(
                  this, METHODID_CHECK_ACCOUNT)))
          .addMethod(
            getReceiveAmountMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.SEC.grpc.BFTB.ReceiveAmountRequest,
                com.SEC.grpc.BFTB.ReceiveAmountResponse>(
                  this, METHODID_RECEIVE_AMOUNT)))
          .addMethod(
            getAuditMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.SEC.grpc.BFTB.AuditRequest,
                com.SEC.grpc.BFTB.AuditTransactionResponse>(
                  this, METHODID_AUDIT)))
          .addMethod(
            getCheckAccountWbMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.SEC.grpc.BFTB.CAWBRequest,
                com.SEC.grpc.BFTB.CAWBResponse>(
                  this, METHODID_CHECK_ACCOUNT_WB)))
          .addMethod(
            getAuditWbMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.SEC.grpc.BFTB.ADWBRequest,
                com.SEC.grpc.BFTB.ADWBResponse>(
                  this, METHODID_AUDIT_WB)))
          .addMethod(
            getPOWRequestMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.SEC.grpc.BFTB.PowRequest,
                com.SEC.grpc.BFTB.PowResponse>(
                  this, METHODID_POW_REQUEST)))
          .build();
    }
  }

  /**
   */
  public static final class BTFTBStub extends io.grpc.stub.AbstractStub<BTFTBStub> {
    private BTFTBStub(io.grpc.Channel channel) {
      super(channel);
    }

    private BTFTBStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BTFTBStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new BTFTBStub(channel, callOptions);
    }

    /**
     */
    public void getRid(com.SEC.grpc.BFTB.GetRidRequest request,
        io.grpc.stub.StreamObserver<com.SEC.grpc.BFTB.RidResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetRidMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void openAccount(com.SEC.grpc.BFTB.OpenAccountRequest request,
        io.grpc.stub.StreamObserver<com.SEC.grpc.BFTB.APIresponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getOpenAccountMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void sendAmount(com.SEC.grpc.BFTB.SendAmountRequest request,
        io.grpc.stub.StreamObserver<com.SEC.grpc.BFTB.SendAmountResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSendAmountMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void checkAccount(com.SEC.grpc.BFTB.CheckAccountRequest request,
        io.grpc.stub.StreamObserver<com.SEC.grpc.BFTB.AccountResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCheckAccountMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void receiveAmount(com.SEC.grpc.BFTB.ReceiveAmountRequest request,
        io.grpc.stub.StreamObserver<com.SEC.grpc.BFTB.ReceiveAmountResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getReceiveAmountMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void audit(com.SEC.grpc.BFTB.AuditRequest request,
        io.grpc.stub.StreamObserver<com.SEC.grpc.BFTB.AuditTransactionResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getAuditMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void checkAccountWb(com.SEC.grpc.BFTB.CAWBRequest request,
        io.grpc.stub.StreamObserver<com.SEC.grpc.BFTB.CAWBResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCheckAccountWbMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void auditWb(com.SEC.grpc.BFTB.ADWBRequest request,
        io.grpc.stub.StreamObserver<com.SEC.grpc.BFTB.ADWBResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getAuditWbMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void pOWRequest(com.SEC.grpc.BFTB.PowRequest request,
        io.grpc.stub.StreamObserver<com.SEC.grpc.BFTB.PowResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getPOWRequestMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class BTFTBBlockingStub extends io.grpc.stub.AbstractStub<BTFTBBlockingStub> {
    private BTFTBBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private BTFTBBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BTFTBBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new BTFTBBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.SEC.grpc.BFTB.RidResponse getRid(com.SEC.grpc.BFTB.GetRidRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetRidMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.SEC.grpc.BFTB.APIresponse openAccount(com.SEC.grpc.BFTB.OpenAccountRequest request) {
      return blockingUnaryCall(
          getChannel(), getOpenAccountMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.SEC.grpc.BFTB.SendAmountResponse sendAmount(com.SEC.grpc.BFTB.SendAmountRequest request) {
      return blockingUnaryCall(
          getChannel(), getSendAmountMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.SEC.grpc.BFTB.AccountResponse checkAccount(com.SEC.grpc.BFTB.CheckAccountRequest request) {
      return blockingUnaryCall(
          getChannel(), getCheckAccountMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.SEC.grpc.BFTB.ReceiveAmountResponse receiveAmount(com.SEC.grpc.BFTB.ReceiveAmountRequest request) {
      return blockingUnaryCall(
          getChannel(), getReceiveAmountMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.SEC.grpc.BFTB.AuditTransactionResponse audit(com.SEC.grpc.BFTB.AuditRequest request) {
      return blockingUnaryCall(
          getChannel(), getAuditMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.SEC.grpc.BFTB.CAWBResponse checkAccountWb(com.SEC.grpc.BFTB.CAWBRequest request) {
      return blockingUnaryCall(
          getChannel(), getCheckAccountWbMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.SEC.grpc.BFTB.ADWBResponse auditWb(com.SEC.grpc.BFTB.ADWBRequest request) {
      return blockingUnaryCall(
          getChannel(), getAuditWbMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.SEC.grpc.BFTB.PowResponse pOWRequest(com.SEC.grpc.BFTB.PowRequest request) {
      return blockingUnaryCall(
          getChannel(), getPOWRequestMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class BTFTBFutureStub extends io.grpc.stub.AbstractStub<BTFTBFutureStub> {
    private BTFTBFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private BTFTBFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BTFTBFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new BTFTBFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.SEC.grpc.BFTB.RidResponse> getRid(
        com.SEC.grpc.BFTB.GetRidRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetRidMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.SEC.grpc.BFTB.APIresponse> openAccount(
        com.SEC.grpc.BFTB.OpenAccountRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getOpenAccountMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.SEC.grpc.BFTB.SendAmountResponse> sendAmount(
        com.SEC.grpc.BFTB.SendAmountRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSendAmountMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.SEC.grpc.BFTB.AccountResponse> checkAccount(
        com.SEC.grpc.BFTB.CheckAccountRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getCheckAccountMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.SEC.grpc.BFTB.ReceiveAmountResponse> receiveAmount(
        com.SEC.grpc.BFTB.ReceiveAmountRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getReceiveAmountMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.SEC.grpc.BFTB.AuditTransactionResponse> audit(
        com.SEC.grpc.BFTB.AuditRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getAuditMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.SEC.grpc.BFTB.CAWBResponse> checkAccountWb(
        com.SEC.grpc.BFTB.CAWBRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getCheckAccountWbMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.SEC.grpc.BFTB.ADWBResponse> auditWb(
        com.SEC.grpc.BFTB.ADWBRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getAuditWbMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.SEC.grpc.BFTB.PowResponse> pOWRequest(
        com.SEC.grpc.BFTB.PowRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getPOWRequestMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_RID = 0;
  private static final int METHODID_OPEN_ACCOUNT = 1;
  private static final int METHODID_SEND_AMOUNT = 2;
  private static final int METHODID_CHECK_ACCOUNT = 3;
  private static final int METHODID_RECEIVE_AMOUNT = 4;
  private static final int METHODID_AUDIT = 5;
  private static final int METHODID_CHECK_ACCOUNT_WB = 6;
  private static final int METHODID_AUDIT_WB = 7;
  private static final int METHODID_POW_REQUEST = 8;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final BTFTBImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(BTFTBImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_RID:
          serviceImpl.getRid((com.SEC.grpc.BFTB.GetRidRequest) request,
              (io.grpc.stub.StreamObserver<com.SEC.grpc.BFTB.RidResponse>) responseObserver);
          break;
        case METHODID_OPEN_ACCOUNT:
          serviceImpl.openAccount((com.SEC.grpc.BFTB.OpenAccountRequest) request,
              (io.grpc.stub.StreamObserver<com.SEC.grpc.BFTB.APIresponse>) responseObserver);
          break;
        case METHODID_SEND_AMOUNT:
          serviceImpl.sendAmount((com.SEC.grpc.BFTB.SendAmountRequest) request,
              (io.grpc.stub.StreamObserver<com.SEC.grpc.BFTB.SendAmountResponse>) responseObserver);
          break;
        case METHODID_CHECK_ACCOUNT:
          serviceImpl.checkAccount((com.SEC.grpc.BFTB.CheckAccountRequest) request,
              (io.grpc.stub.StreamObserver<com.SEC.grpc.BFTB.AccountResponse>) responseObserver);
          break;
        case METHODID_RECEIVE_AMOUNT:
          serviceImpl.receiveAmount((com.SEC.grpc.BFTB.ReceiveAmountRequest) request,
              (io.grpc.stub.StreamObserver<com.SEC.grpc.BFTB.ReceiveAmountResponse>) responseObserver);
          break;
        case METHODID_AUDIT:
          serviceImpl.audit((com.SEC.grpc.BFTB.AuditRequest) request,
              (io.grpc.stub.StreamObserver<com.SEC.grpc.BFTB.AuditTransactionResponse>) responseObserver);
          break;
        case METHODID_CHECK_ACCOUNT_WB:
          serviceImpl.checkAccountWb((com.SEC.grpc.BFTB.CAWBRequest) request,
              (io.grpc.stub.StreamObserver<com.SEC.grpc.BFTB.CAWBResponse>) responseObserver);
          break;
        case METHODID_AUDIT_WB:
          serviceImpl.auditWb((com.SEC.grpc.BFTB.ADWBRequest) request,
              (io.grpc.stub.StreamObserver<com.SEC.grpc.BFTB.ADWBResponse>) responseObserver);
          break;
        case METHODID_POW_REQUEST:
          serviceImpl.pOWRequest((com.SEC.grpc.BFTB.PowRequest) request,
              (io.grpc.stub.StreamObserver<com.SEC.grpc.BFTB.PowResponse>) responseObserver);
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

  private static abstract class BTFTBBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    BTFTBBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.SEC.grpc.BFTB.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("BTFTB");
    }
  }

  private static final class BTFTBFileDescriptorSupplier
      extends BTFTBBaseDescriptorSupplier {
    BTFTBFileDescriptorSupplier() {}
  }

  private static final class BTFTBMethodDescriptorSupplier
      extends BTFTBBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    BTFTBMethodDescriptorSupplier(String methodName) {
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
      synchronized (BTFTBGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new BTFTBFileDescriptorSupplier())
              .addMethod(getGetRidMethod())
              .addMethod(getOpenAccountMethod())
              .addMethod(getSendAmountMethod())
              .addMethod(getCheckAccountMethod())
              .addMethod(getReceiveAmountMethod())
              .addMethod(getAuditMethod())
              .addMethod(getCheckAccountWbMethod())
              .addMethod(getAuditWbMethod())
              .addMethod(getPOWRequestMethod())
              .build();
        }
      }
    }
    return result;
  }
}
