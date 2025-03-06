import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        String password = "password"; // A senha que você deseja criptografar
        String encodedPassword = new BCryptPasswordEncoder().encode(password);
        System.out.println(encodedPassword);
    }
}