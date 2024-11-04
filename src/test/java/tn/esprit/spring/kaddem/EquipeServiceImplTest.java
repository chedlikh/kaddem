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

import java.util.List;
import java.util.Optional;


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


}
