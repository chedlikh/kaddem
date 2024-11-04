package tn.esprit.spring.kaddem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.kaddem.entities.*;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;
import tn.esprit.spring.kaddem.services.EquipeServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EquipeServiceImplTest {

    @InjectMocks
    private EquipeServiceImpl equipeService;

    @Mock
    private EquipeRepository equipeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetrieveAllEquipes() {
        List<Equipe> mockEquipes = new ArrayList<>();
        mockEquipes.add(new Equipe(1, "Equipe A", Niveau.JUNIOR));
        mockEquipes.add(new Equipe(2, "Equipe B", Niveau.SENIOR));

        when(equipeRepository.findAll()).thenReturn(mockEquipes);

        List<Equipe> result = equipeService.retrieveAllEquipes();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(equipeRepository, times(1)).findAll();
    }

    @Test
    void testAddEquipe() {
        Equipe equipe = new Equipe(3, "Equipe C", Niveau.JUNIOR);
        when(equipeRepository.save(equipe)).thenReturn(equipe);

        Equipe result = equipeService.addEquipe(equipe);

        assertNotNull(result);
        assertEquals("Equipe C", result.getNomEquipe());
        verify(equipeRepository, times(1)).save(equipe);
    }

    @Test
    void testRetrieveEquipe() {
        Equipe equipe = new Equipe(1, "Equipe A", Niveau.JUNIOR);
        when(equipeRepository.findById(1)).thenReturn(Optional.of(equipe));

        Equipe result = equipeService.retrieveEquipe(1);

        assertNotNull(result);
        assertEquals(1, result.getIdEquipe());
        verify(equipeRepository, times(1)).findById(1);
    }

    @Test
    void testDeleteEquipe() {
        Equipe equipe = new Equipe(1, "Equipe A", Niveau.JUNIOR);
        when(equipeRepository.findById(1)).thenReturn(Optional.of(equipe));

        equipeService.deleteEquipe(1);

        verify(equipeRepository, times(1)).delete(equipe);
    }

    @Test
    void testUpdateEquipe() {
        Equipe equipe = new Equipe(1, "Equipe A", Niveau.JUNIOR);
        when(equipeRepository.save(equipe)).thenReturn(equipe);

        Equipe result = equipeService.updateEquipe(equipe);

        assertNotNull(result);
        assertEquals("Equipe A", result.getNomEquipe());
        verify(equipeRepository, times(1)).save(equipe);
    }

    @Test
    void testEvoluerEquipes() {
        // Preparation des donnees
        Equipe equipe = new Equipe(1, "Equipe A", Niveau.JUNIOR);

        // Create contracts for students
        Set<Contrat> contrats = new HashSet<>();

        Specialite specialite = Specialite.RESEAUX;
        Integer montantContrat = 1000;

        contrats.add(new Contrat(1,
                new Date(System.currentTimeMillis() - (1000L * 60 * 60 * 24 * 366)),
                new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 30)), // Example future date for dateFinContrat
                specialite,
                false,
                montantContrat
        )); // Active contract
        // Create students and assign contracts
        // Assuming 'op' is an instance of Option
        Option option1 = Option.GAMIX; // Replace with a valid value from your Option enum
        Option option2 = Option.SE; // Replace with another valid value

        Etudiant etudiant1 = new Etudiant(1, "Etudiant 1", "Prenom 1", option1); // Provide prenomE
        Etudiant etudiant2 = new Etudiant(2, "Etudiant 2", "Prenom 2", option2); // Provide prenomE


        // Set students in the team
        Set<Etudiant> etudiants = new HashSet<>();
        etudiants.add(etudiant1);
        etudiants.add(etudiant2);
        equipe.setEtudiants(etudiants);


        // Prepare list of teams
        List<Equipe> equipes = new ArrayList<>();
        equipes.add(equipe);

        // Mocking repository behavior
        when(equipeRepository.findAll()).thenReturn(equipes);
        when(equipeRepository.save(any(Equipe.class))).thenReturn(equipe);

        // Execution de la methode
        equipeService.evoluerEquipes();

        // Verifications
        assertEquals(Niveau.SENIOR, equipe.getNiveau());
        verify(equipeRepository, times(1)).save(equipe);
    }
}
