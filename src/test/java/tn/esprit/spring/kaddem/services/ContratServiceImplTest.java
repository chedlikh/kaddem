package tn.esprit.spring.kaddem.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.kaddem.dto.ContratDTO;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.entities.Specialite;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ContratServiceImplTest {

    @Mock
    private ContratRepository contratRepository;

    @Mock
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private ContratServiceImpl contratService;

    private Contrat contrat;
    private ContratDTO contratDTO;
    private Etudiant etudiant;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set up test data
        etudiant = new Etudiant();
        etudiant.setIdEtudiant(1); // Assuming there's an ID field in Etudiant

        contrat = new Contrat();
        contrat.setIdContrat(1);
        contrat.setDateDebutContrat(new Date());
        contrat.setDateFinContrat(new Date());
        contrat.setSpecialite(Specialite.IA); // Example enum value
        contrat.setArchive(false);
        contrat.setMontantContrat(1000);
        contrat.setEtudiant(etudiant);

        contratDTO = new ContratDTO(new Date(), new Date(), Specialite.IA, false, 1000, 1);
    }

    @Test
    void retrieveAllContrats() {
        // Arrange
        List<Contrat> expectedContrats = Arrays.asList(contrat);
        when(contratRepository.findAll()).thenReturn(expectedContrats);

        // Act
        List<Contrat> actualContrats = contratService.retrieveAllContrats();

        // Assert
        assertNotNull(actualContrats);
        assertEquals(1, actualContrats.size());
        assertEquals(contrat.getIdContrat(), actualContrats.get(0).getIdContrat());
        verify(contratRepository, times(1)).findAll();
    }

    @Test
    void addContrat() {
        // Arrange
        when(etudiantRepository.findById(1)).thenReturn(Optional.of(etudiant));
        when(contratRepository.save(any(Contrat.class))).thenReturn(contrat);

        // Act
        Contrat savedContrat = contratService.addContrat(contratDTO);

        // Assert
        assertNotNull(savedContrat);
        assertEquals(contrat.getIdContrat(), savedContrat.getIdContrat());
        verify(contratRepository, times(1)).save(any(Contrat.class));
    }

    @Test
    void updateContrat() {
        // Arrange
        when(contratRepository.findById(1)).thenReturn(Optional.of(contrat));
        when(etudiantRepository.findById(1)).thenReturn(Optional.of(etudiant));
        when(contratRepository.save(any(Contrat.class))).thenReturn(contrat);

        // Act
        Contrat updatedContrat = contratService.updateContrat(contratDTO);

        // Assert
        assertNotNull(updatedContrat);
        assertEquals(contrat.getIdContrat(), updatedContrat.getIdContrat());
        verify(contratRepository, times(1)).save(any(Contrat.class));
    }

    @Test
    void retrieveContrat() {
        // Arrange
        when(contratRepository.findById(1)).thenReturn(Optional.of(contrat));

        // Act
        Contrat retrievedContrat = contratService.retrieveContrat(1);

        // Assert
        assertNotNull(retrievedContrat);
        assertEquals(contrat.getIdContrat(), retrievedContrat.getIdContrat());
        verify(contratRepository, times(1)).findById(1);
    }

    @Test
    void removeContrat() {
        // Arrange
        when(contratRepository.findById(1)).thenReturn(Optional.of(contrat));

        // Act
        contratService.removeContrat(1);

        // Assert
        verify(contratRepository, times(1)).delete(contrat);
    }

    @Test
    void updateContrat_NotFound() {
        // Arrange
        when(contratRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            contratService.updateContrat(contratDTO);
        });

        assertEquals("Contrat not found with id: 1", exception.getMessage());
        verify(contratRepository, times(1)).findById(1);
    }
}
