package com.microsoft.azuresamples.msal4j.msidentityspringbootwebapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AzureLoginFlowTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void unauthenticatedRequestIsRedirectedToAzureLogin() throws Exception {
        mockMvc.perform(get("/token_details"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", org.hamcrest.Matchers.containsString("/oauth2/authorization/azure")));
    }

    @Test
    void authenticatedOidcUserCanAccessProtectedEndpoint() throws Exception {
        mockMvc.perform(get("/token_details").with(oidcLogin()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("claims"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("ID Token Details")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("user")));
    }
}
