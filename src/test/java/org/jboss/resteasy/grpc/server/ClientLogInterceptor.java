package org.jboss.resteasy.grpc.server;

import io.grpc.*;

public class ClientLogInterceptor implements ClientInterceptor {

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor,
                                                               CallOptions callOptions, Channel channel) {
        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(
                channel.newCall(methodDescriptor, callOptions)) {

            @Override
            public void sendMessage(ReqT message) {

                System.out.printf("Sending method '%s' message '%s'%n", methodDescriptor.getFullMethodName(),
                        message.toString());
                System.out.printf("getSchemaDescriptor '%s'%n", methodDescriptor.getSchemaDescriptor());
                System.out.printf("getServiceName '%s'%n", methodDescriptor.getServiceName());

                super.sendMessage(message);
            }

            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                System.out.println(ClientLogInterceptor.class.getSimpleName());

                ClientCall.Listener<RespT> listener = new ForwardingClientCallListener<RespT>() {
                    @Override
                    protected Listener<RespT> delegate() {
                        return responseListener;
                    }

                    @Override
                    public void onMessage(RespT message) {
                        System.out.printf("Received message '%s'%n", message.toString());
                        super.onMessage(message);
                    }
                };

                super.start(listener, headers);
            }
        };
    }

}