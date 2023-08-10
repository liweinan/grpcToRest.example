/*
 * JBoss, Home of Professional Open Source.
 *
 * Copyright 2023 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.resteasy.grpc.server;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import org.greet.GreetServiceGrpc;
import org.greet.GreetServiceGrpc.GreetServiceBlockingStub;
import org.greet.Greet_proto.GeneralEntityMessage;
import org.greet.Greet_proto.GeneralReturnMessage;
import org.greet.Greet_proto.dev_resteasy_example_grpc_greet___GeneralGreeting;
import org.greet.Greet_proto.dev_resteasy_example_grpc_greet___Greeting;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.helpers.Operations;
import org.jboss.dmr.ModelNode;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response;

public class GreetingTest {

    private static ManagedChannel channelPlaintext;
    private static GreetServiceBlockingStub blockingStub;

//    @BeforeClass
//    public static void beforeClass() throws Exception {
//
//        // Use plaintext connection
//        try (ModelControllerClient client = ModelControllerClient.Factory.create("localhost", 9990)) {
//            final ModelNode handlerAddress = Operations.createAddress("subsystem", "grpc");
//            final ModelNode op = Operations.createWriteAttributeOperation(handlerAddress, "key-manager-name", "");
//            final ModelNode result = client.execute(op);
//            if (!Operations.isSuccessfulOutcome(result)) {
//                throw new RuntimeException("Failed to execute operation: " + op + " " +
//                        Operations.getFailureDescription(result).asString());
//            }
//        }
//
//        // Establish ServletContext
//        Client client = ClientBuilder.newClient();
//        String url = "http://localhost:8080/grpcToRest.example.grpc-1.0.1.Final-SNAPSHOT/grpcToJakartaRest/grpcserver/context";
//        Response response = client.target(url).request().get();
//        Assert.assertEquals(200, response.getStatus());
//        client.close();
//
//        // Create gRPC connection
//        channelPlaintext = ManagedChannelBuilder.forTarget("localhost:9555").usePlaintext().build();
//        blockingStub = GreetServiceGrpc.newBlockingStub(channelPlaintext);
//    }

    @Test
    public void testGreeting() {
        channelPlaintext = ManagedChannelBuilder.forTarget("localhost:9555")
                .usePlaintext()
                .intercept(new ClientLogInterceptor()).
                build();
        blockingStub = GreetServiceGrpc.newBlockingStub(channelPlaintext);

        GeneralEntityMessage.Builder builder = GeneralEntityMessage.newBuilder();
        GeneralEntityMessage gem = builder.setURL("http://localhost:xxx/greet/Bill").build();
        GeneralReturnMessage grm = blockingStub.greet(gem);
        dev_resteasy_example_grpc_greet___Greeting greeting = grm.getDevResteasyExampleGrpcGreetGreetingField();
        Assert.assertEquals("hello, Bill", greeting.getS());

    }

//    @Test
//    public void testGeneralGreeting() {
//        GeneralEntityMessage.Builder builder = GeneralEntityMessage.newBuilder();
//        GeneralEntityMessage gem = builder.setURL("http://localhost:8080/salute/Bill?salute=Heyyy").build();
//        try {
//            GeneralReturnMessage grm = blockingStub.generalGreet(gem);
//            dev_resteasy_example_grpc_greet___GeneralGreeting greeting = grm.getDevResteasyExampleGrpcGreetGeneralGreetingField();
//            Assert.assertEquals("Heyyy", greeting.getSalute());
//            Assert.assertEquals("Bill", greeting.getGreetingSuper().getS());
//        } catch (StatusRuntimeException e) {
//            //
//        }
//    }
}
