package tn.esprit.spring.kaddem.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.spring.kaddem.dto.ContratDTO;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Specialite;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
class JunitContratServiceImplTest {

    @Autowired
    private ContratServiceImpl contratService;

    @Test
    @Order(1)
    void addContrat() {
        ContratDTO contratDto = new ContratDTO();
        contratDto.setDateDebutContrat(new java.util.Date());
        contratDto.setDateFinContrat(new java.util.Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)); // 1 day later
        contratDto.setSpecialite(Specialite.IA); // Replace with an actual specialization
        contratDto.setArchive(false);
        contratDto.setMontantContrat(1500);
        contratDto.setEtudiantId(1); // Replace with a valid Etudiant ID

        Contrat addedContrat = contratService.addContrat(contratDto);
        assertNotNull(addedContrat);
        assertEquals(1500, addedContrat.getMontantContrat());
         assertEquals(dateDebut, addedContrat.getDateDebutContrat());
    assertEquals(dateFin, addedContrat.getDateFinContrat());
    assertEquals(Specialite.IA, addedContrat.getSpecialite()); // Remplacer par la spécialité exacte si différente
    assertEquals(false, addedContrat.isArchive());
    assertEquals(1, addedContrat.getEtudiant().getId()); // Assurez-vous que l'objet Etudiant est bien récupéré


        log.info("AddContratTest : Ok");
    }

    @Test
    @Order(2)
    void updateContrat() {
        ContratDTO contratDto = new ContratDTO();
        contratDto.setEtudiantId(1); // Ensure this ID exists
        contratDto.setDateDebutContrat(new java.util.Date());
        contratDto.setDateFinContrat(new java.util.Date(System.currentTimeMillis() + 1000 * 60 * 60 * 48)); // 2 days later
        contratDto.setSpecialite(Specialite.IA); // Replace with actual specialization
        contratDto.setArchive(true);
        contratDto.setMontantContrat(2000);

        Contrat updatedContrat = contratService.updateContrat(contratDto);
        assertNotNull(updatedContrat);
        assertEquals(2000, updatedContrat.getMontantContrat());

        log.info("UpdateContratTest : Ok");
    }
    @Test
    @Order(3)
    void retrieveContrat() {
        Contrat contrat = contratService.retrieveContrat(1); // Replace with a valid Contrat ID
        assertNotNull(contrat);

        log.info("RetrieveContratTest : Ok");
    }

    @Test
    @Order(4)
    void removeContrat() {
        Integer idContratToRemove = 1; // Replace with a valid Contrat ID to delete
        contratService.removeContrat(idContratToRemove);

        Contrat removedContrat = contratService.retrieveContrat(idContratToRemove);
        assertNull(removedContrat);

        log.info("RemoveContratTest : Ok");
    }
}

