package tn.esprit.spring.kaddem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.spring.kaddem.entities.Equipe;

import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;
import tn.esprit.spring.kaddem.services.EquipeServiceImpl;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class) // Pour exécuter les tests dans un environnement Spring
@SpringBootTest
class EquipeServiceImplTest {

    @Mock
    private EquipeRepository equipeRepository;  // Mock du repository de l'équipe

    @Mock
    private EtudiantRepository etudiantRepository;  // Mock du repository des étudiants

    @InjectMocks
    private EquipeServiceImpl equipeService;  // Service à tester avec ses dépendances mockées

    private Equipe equipe;
    private Etudiant etudiant;

    @BeforeEach
    void setUp() {
        // Initialisation des objets pour chaque test
        equipe = new Equipe();
        equipe.setIdEquipe(1);
        equipe.setNomEquipe("Equipe 1");

        etudiant = new Etudiant();
        etudiant.setIdEtudiant(1);
        etudiant.setNomE("Etudiant 1");
        etudiant.setPrenomE("Prenom 1");
        etudiant.setEquipes(new ArrayList<>());

    }

    @Test
    void testRetrieveAllEquipes() {
        // Préparer le mock
        List<Equipe> equipes = Arrays.asList(equipe);
        when(equipeRepository.findAll()).thenReturn(equipes);

        // Appel de la méthode à tester
        List<Equipe> result = equipeService.retrieveAllEquipes();

        // Vérification des résultats
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Equipe 1", result.get(0).getNomEquipe());
        verify(equipeRepository, times(1)).findAll();  // Vérifie que la méthode a été appelée une fois
    }

    @Test
    void testAddEquipe() {
        // Préparer le mock
        when(equipeRepository.save(any(Equipe.class))).thenReturn(equipe);

        // Appel de la méthode à tester
        Equipe result = equipeService.addEquipe(equipe);

        // Vérification des résultats
        assertNotNull(result);
        assertEquals(1, result.getIdEquipe());
        assertEquals("Equipe 1", result.getNomEquipe());
        verify(equipeRepository, times(1)).save(equipe);  // Vérifie que la méthode save a été appelée une fois
    }

    @Test
    void testDeleteEquipe() {
        // Préparer le mock
        when(equipeRepository.findById(1)).thenReturn(Optional.of(equipe));

        // Appel de la méthode à tester
        equipeService.deleteEquipe(1);

        // Vérification que la méthode delete a bien été appelée
        verify(equipeRepository, times(1)).delete(equipe);
    }

    @Test
    void testDeleteEquipe_NotFound() {
        // Préparer le mock pour un cas où l'équipe n'est pas trouvée
        when(equipeRepository.findById(1)).thenReturn(Optional.empty());

        // Appel de la méthode à tester
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            equipeService.deleteEquipe(1);
        });

        // Vérification du message d'exception
        assertEquals("Equipe with ID 1 does not exist.", exception.getMessage());
    }

    @Test
    void testAssignEtudiantToEquipe() {
        // Préparer les mocks
        when(etudiantRepository.findById(1)).thenReturn(Optional.of(etudiant));
        when(equipeRepository.findById(1)).thenReturn(Optional.of(equipe));

        // Appel de la méthode à tester
        String result = equipeService.assignEtudiantToEquipe(1, 1);

        // Vérification des résultats
        assertEquals("Etudiant assigned to Equipe successfully.", result);
        assertTrue(etudiant.getEquipes().contains(equipe));
        assertTrue(equipe.getEtudiants().contains(etudiant));

        // Vérifier que save a été appelé sur les deux repositories
        verify(etudiantRepository, times(1)).save(etudiant);
        verify(equipeRepository, times(1)).save(equipe);
    }

    @Test
    void testAssignEtudiantToEquipe_NotFound() {
        // Préparer les mocks pour un cas où l'étudiant ou l'équipe n'est pas trouvé
        when(etudiantRepository.findById(1)).thenReturn(Optional.empty());
        when(equipeRepository.findById(1)).thenReturn(Optional.of(equipe));

        // Appel de la méthode à tester
        String result = equipeService.assignEtudiantToEquipe(1, 1);

        // Vérification des résultats
        assertEquals("Etudiant or Equipe not found.", result);

        // Vérifier que save n'a pas été appelé
        verify(etudiantRepository, never()).save(any());
        verify(equipeRepository, never()).save(any());
    }

    @Test
    void testRetrieveEquipe() {
        // Préparer le mock
        when(equipeRepository.findById(1)).thenReturn(Optional.of(equipe));

        // Appel de la méthode à tester
        Equipe result = equipeService.retrieveEquipe(1);

        // Vérification des résultats
        assertNotNull(result);
        assertEquals(1, result.getIdEquipe());
        assertEquals("Equipe 1", result.getNomEquipe());
        verify(equipeRepository, times(1)).findById(1);  // Vérifie que findById a été appelé une fois
    }

    @Test
    void testRetrieveEquipe_NotFound() {
        // Préparer le mock pour un cas où l'équipe n'est pas trouvée
        when(equipeRepository.findById(1)).thenReturn(Optional.empty());

        // Appel de la méthode à tester
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            equipeService.retrieveEquipe(1);
        });

        // Vérification du message d'exception
        assertEquals("Team not found with ID: 1", exception.getMessage());
    }
}