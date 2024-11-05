package tn.esprit.spring.kaddem;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import tn.esprit.spring.kaddem.entities.Equipe;

import tn.esprit.spring.kaddem.repositories.EquipeRepository;
import tn.esprit.spring.kaddem.services.EquipeServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
@RunWith(SpringRunner.class)
class JunitEquipeServiceImplTest {



        @Autowired
        private EquipeRepository equipeRepository;

        @Autowired
        private EquipeServiceImpl equipeService;
    @BeforeEach
    void setUp() {
        equipeRepository.deleteAll(); // Clear the repository before each test
    }
        @Test
        @Order(1)
        void testRetrieveAllEquipes() {
            // Préparation des données
            equipeRepository.save(new Equipe(null, "Equipe A", null));
            equipeRepository.save(new Equipe(null, "Equipe B", null));

            // Exécution de la méthode à tester
            List<Equipe> equipes = equipeService.retrieveAllEquipes();

            // Vérification des résultats
            assertEquals(2, equipes.size());
        }

}
