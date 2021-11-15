package com.contoso;

import java.util.ArrayList;
import java.util.List;

import com.microsoft.graph.models.User;

class ClientCredentialGrant {

    private static String clientId;
    private static String secret;
    private static String tenant;
    private static List<String> scopes = new ArrayList<String>();

    public static void main(String args[]) throws Exception {

        clientId = "<Client_ID>";
        secret = "<Client_Secret>";
        tenant = "<Tenant_ID>";
        scopes.add("https://graph.microsoft.com/.default");

        Graph.initializeGraphAuth(clientId, secret, tenant, scopes);

        User user = Graph.getUserById("<User_ID>");
        System.out.println("Welcome " + user.displayName);
        System.out.println("Mail: " + user.mail);
        System.out.println();

        Graph.getMessagesByUserId("<User_ID>");
    }
}
