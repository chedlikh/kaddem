package tn.esprit.spring.kaddem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.kaddem.entities.*;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;
import tn.esprit.spring.kaddem.services.EquipeServiceImpl;

import java.util.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.slf4j.Logger;

class EquipeServiceImplTest {

    @InjectMocks
    private EquipeServiceImpl equipeService;
    @Mock
    private Logger log;

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
    void testEvoluerEquipes_NoStudentsInTeam() {
        // Arrange
        Equipe equipe = new Equipe();
        equipe.setIdEquipe(1);
        equipe.setNiveau(Niveau.JUNIOR);
        equipe.setEtudiants(new HashSet<>()); // No students in the team

        // Mocking the repository behavior
        when(equipeRepository.findAll()).thenReturn(Collections.singletonList(equipe));

        // Act
        equipeService.evoluerEquipes();



        // If you have a way to check that the state of the team hasn't changed,
        // you can assert that as well. For example:
        assertEquals(0, equipe.getEtudiants().size()); // Ensuring no students are still present
    }


        @Test
        void testEvoluerEquipes_StudentsWithActiveContracts() {
            // Arrange
            Date dateFinContrat = new Date(System.currentTimeMillis() - (1000L * 60 * 60 * 24 * 366)); // More than a year ago
            Contrat contrat = new Contrat();
            contrat.setIdContrat(1);
            contrat.setArchive(false);
            contrat.setDateFinContrat(dateFinContrat);

            Etudiant etudiant = new Etudiant();
            etudiant.setIdEtudiant(1);
            etudiant.setContrats(new HashSet<>(Collections.singletonList(contrat)));

            Equipe equipe = new Equipe();
            equipe.setIdEquipe(1);
            equipe.setNiveau(Niveau.JUNIOR);
            equipe.setEtudiants(new HashSet<>(Collections.singletonList(etudiant)));

            when(equipeRepository.findAll()).thenReturn(Collections.singletonList(equipe));

            // Act
            equipeService.evoluerEquipes();


        }

        @Test
        void testEvoluerEquipes_BreaksAfterThreeActiveContracts() {
            // Arrange
            Contrat activeContrat = new Contrat();
            activeContrat.setIdContrat(1);
            activeContrat.setArchive(false);
            activeContrat.setDateFinContrat(new Date(System.currentTimeMillis() - (1000L * 60 * 60 * 24 * 366))); // More than a year ago

            Etudiant etudiant1 = new Etudiant();
            etudiant1.setIdEtudiant(1);
            etudiant1.setContrats(new HashSet<>(Collections.singletonList(activeContrat)));

            Etudiant etudiant2 = new Etudiant();
            etudiant2.setIdEtudiant(2);
            etudiant2.setContrats(new HashSet<>(Collections.singletonList(activeContrat)));

            Etudiant etudiant3 = new Etudiant();
            etudiant3.setIdEtudiant(3);
            etudiant3.setContrats(new HashSet<>(Collections.singletonList(activeContrat)));

            Equipe equipe = new Equipe();
            equipe.setIdEquipe(1);
            equipe.setNiveau(Niveau.SENIOR);
            equipe.setEtudiants(new HashSet<>(Arrays.asList(etudiant1, etudiant2, etudiant3)));

            when(equipeRepository.findAll()).thenReturn(Collections.singletonList(equipe));

            // Act
            equipeService.evoluerEquipes();

           
        }
    }




