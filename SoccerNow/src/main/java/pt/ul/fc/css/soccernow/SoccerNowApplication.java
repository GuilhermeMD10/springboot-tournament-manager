package pt.ul.fc.css.soccernow;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import jakarta.transaction.Transactional;
import pt.ul.fc.css.soccernow.model.Utilizador;
import pt.ul.fc.css.soccernow.repository.UtilizadorRepository;

@SpringBootApplication
public class SoccerNowApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoccerNowApplication.class, args);

    }

    @Bean
    @Transactional
    public CommandLineRunner demo(UtilizadorRepository ur) {
        return (args) -> {
            System.out.println("do some sanity tests here");
            Utilizador admin = new Utilizador();
            admin.setNif("123456789");
            admin.setEmail("admin@fc.ul.pt");
            admin.setNome("Admin");
            ur.save(admin);
            System.out.println(("Admin adicionado:" + admin.getNome()));

        };
    }
}
