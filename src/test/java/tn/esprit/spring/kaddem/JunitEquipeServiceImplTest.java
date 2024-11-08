package tn.esprit.spring.kaddem;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import tn.esprit.spring.kaddem.entities.Equipe;

import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.entities.Niveau;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;
import tn.esprit.spring.kaddem.services.EquipeServiceImpl;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
@RunWith(SpringRunner.class)
class JunitEquipeServiceImplTest {
    @Autowired
    private EquipeServiceImpl equipeService;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private EquipeRepository equipeRepository;

    @Test
    @Order(1)
    void addEquipe() {
        Equipe equipe = new Equipe();
        equipe.setNomEquipe("Equipe A1");
        equipe.setNiveau(Niveau.JUNIOR);
        equipe.setIdEquipe(1);

        Equipe addedEquipe = equipeService.addEquipe(equipe);
        assertNotNull(addedEquipe);
        assertEquals("Equipe A1", addedEquipe.getNomEquipe());

        log.info("AddEquipeTest : Ok");
    }

    @Test
    @Order(3)
    void updateEquipe() {
        Equipe equipe = equipeRepository.findByNomEquipe("Equipe A1")
                .orElseThrow(() -> new NoSuchElementException("Equipe not found with name: " + "Equipe A"));

        Equipe updatedEquipe = equipeService.updateEquipe(equipe);
        assertNotNull(updatedEquipe);
        assertEquals("Equipe A1", updatedEquipe.getNomEquipe());

        log.info("UpdateEquipeTest : Ok");
    }

    @Test
    @Order(2)
    void retrieveEquipe() {
        Equipe equipe = equipeRepository.findByNomEquipe("Equipe A1")
                .orElseThrow(() -> new NoSuchElementException("Equipe not found with name: " + "Equipe A"));

        assertNotNull(equipe);
        assertEquals("Equipe A1", equipe.getNomEquipe()); // Ensure the correct ID is retrieved

        log.info("RetrieveEquipeTest : Ok");
    }

    @Test
    @Order(6)
    void deleteEquipe() {
        Equipe equipe = equipeRepository.findByNomEquipe("Equipe A1")
                .orElseThrow(() -> new NoSuchElementException("Equipe not found  "));
        // Replace with valid ID
        Integer equipeId = equipe.getIdEquipe();

        equipeService.deleteEquipe(equipeId);

        assertThrows(NoSuchElementException.class, () -> equipeService.retrieveEquipe(equipeId));

        log.info("DeleteEquipeTest : Ok");
    }

    @Test
    @Order(4)
    void assignEtudiantToEquipe() {
        Etudiant etudiant = etudiantRepository.findById(1).orElseThrow(() -> new NoSuchElementException("Etudiant not found  ")); // Replace with valid ID
        Equipe equipe = equipeRepository.findByNomEquipe("Equipe A1")
                .orElseThrow(() -> new NoSuchElementException("Equipe not found with name: " + "Equipe A"));
        String result = equipeService.assignEtudiantToEquipe(etudiant.getIdEtudiant(), equipe.getIdEquipe());
        assertEquals("Etudiant assigned to Equipe successfully.", result);

        log.info("AssignEtudiantToEquipeTest : Ok");
    }

    @Test
    @Order(5)
    void assignEtudiantToEquipeWhenNotFound() {
        String result = equipeService.assignEtudiantToEquipe(999, 999); // Invalid IDs
        assertEquals("Etudiant or Equipe not found.", result);

        log.info("AssignEtudiantToEquipeWhenNotFoundTest : Ok");
    }
}