import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Base64;

public class JwtSecretKeyGenerator {
    public static void main(String[] args) {
        // Gera uma chave segura para o algoritmo HS512
        SecretKey secretKey = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS512);

        // Converte a chave para Base64 para facilitar o uso no application.yml
        String base64EncodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println("Chave secreta (Base64): " + base64EncodedKey);
    }
}