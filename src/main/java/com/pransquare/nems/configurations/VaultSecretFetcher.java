package com.pransquare.nems.configurations;



import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;

@Component
public class VaultSecretFetcher {

    @Autowired
    private VaultTemplate vaultTemplate;

    public Map<String, Object> getDbCredentials() {
        VaultResponse response = vaultTemplate.read("secret/db-config");

        if (response == null || response.getData() == null) {
            throw new RuntimeException("❌ Vault response is null or empty");
        }

        return response.getData();
    }
}
