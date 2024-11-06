package tn.esprit.spring.kaddem.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;

@ExtendWith(MockitoExtension.class)
class DepartementServiceImplTest {

    @InjectMocks
    DepartementServiceImpl departementService;

    @Mock
    DepartementRepository departementRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetrieveAllDepartements() {
        // Arrange
        Departement departement1 = new Departement();
        Departement departement2 = new Departement();
        when(departementRepository.findAll()).thenReturn(Arrays.asList(departement1, departement2));

        // Act
        List<Departement> result = departementService.retrieveAllDepartements();

        // Assert
        assertEquals(2, result.size());
        verify(departementRepository, times(1)).findAll();
    }

    @Test
    void testAddDepartement() {
        // Arrange
        Departement departement = new Departement();
        when(departementRepository.save(any(Departement.class))).thenReturn(departement);

        // Act
        Departement result = departementService.addDepartement(departement);

        // Assert
        assertNotNull(result);
        verify(departementRepository, times(1)).save(departement);
    }

    @Test
    void testUpdateDepartement() {
        // Arrange
        Departement departement = new Departement();
        when(departementRepository.save(any(Departement.class))).thenReturn(departement);

        // Act
        Departement result = departementService.updateDepartement(departement);

        // Assert
        assertNotNull(result);
        verify(departementRepository, times(1)).save(departement);
    }

    @Test
    void testRetrieveDepartement() {
        // Arrange
        Departement departement = new Departement();
        when(departementRepository.findById(anyInt())).thenReturn(Optional.of(departement));

        // Act
        Departement result = departementService.retrieveDepartement(1);

        // Assert
        assertNotNull(result);
        verify(departementRepository, times(1)).findById(1);
    }

    @Test
    void testRetrieveDepartementNotFound() {
        // Arrange
        when(departementRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            departementService.retrieveDepartement(1);
        });
    }

    @Test
    void testDeleteDepartement() {
        // Arrange
        Departement departement = new Departement();
        when(departementRepository.findById(anyInt())).thenReturn(Optional.of(departement));

        // Act
        departementService.deleteDepartement(1);

        // Assert
        verify(departementRepository, times(1)).delete(departement);
    }

    @Test
    void testDeleteDepartementNotFound() {
        // Arrange
        when(departementRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            departementService.deleteDepartement(1);
        });
    }
}
