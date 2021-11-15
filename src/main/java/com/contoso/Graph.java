package com.contoso;

import java.util.List;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.logger.DefaultLogger;
import com.microsoft.graph.logger.LoggerLevel;
import com.microsoft.graph.models.Message;
import com.microsoft.graph.models.User;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.requests.MessageCollectionPage;

import okhttp3.Request;

public class Graph {
    private static GraphServiceClient<Request> graphClient = null;
    private static TokenCredentialAuthProvider authProvider = null;

    public static void initializeGraphAuth(String applicationId, String clientSecret, String tenantId,
            List<String> scopes) {

        final ClientSecretCredential credential = new ClientSecretCredentialBuilder().clientId(applicationId)
                .clientSecret(clientSecret).tenantId(tenantId).build();

        authProvider = new TokenCredentialAuthProvider(scopes, credential);

        DefaultLogger logger = new DefaultLogger();
        logger.setLoggingLevel(LoggerLevel.ERROR);

        // Build a Graph client
        graphClient = GraphServiceClient.builder().authenticationProvider(authProvider).logger(logger).buildClient();
    }

    public static User getUserById(String userId) {
        if (graphClient == null)
            throw new NullPointerException(
                    "Graph client has not been initialized. Call initializeGraphAuth before calling this method");

        User user = graphClient.users(userId).buildRequest().select("displayName,id,mail").get();

        return user;
    }

    public static void getMessagesByUserId(String userId) {
        if (graphClient == null)
            throw new NullPointerException(
                    "Graph client has not been initialized. Call initializeGraphAuth before calling this method");

        MessageCollectionPage messages = graphClient.users(userId).messages().buildRequest().select("sender,subject")
                .top(10).get();

        final List<Message> messagesList = messages.getCurrentPage();

        messagesList.forEach(m -> {
            System.out.println("subject = " + m.subject);
        });

    }
}
