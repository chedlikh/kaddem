package tn.esprit.spring.kaddem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.kaddem.entities.Equipe;

import tn.esprit.spring.kaddem.repositories.EquipeRepository;
import tn.esprit.spring.kaddem.services.EquipeServiceImpl;



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
    void testAddEquipe() {
        // Préparation de l'objet d'équipe à ajouter
        Equipe equipe = new Equipe(null, "Equipe C", null);

        // Simulation du comportement de la méthode save
        when(equipeRepository.save(any(Equipe.class))).thenReturn(new Equipe(1, "Equipe C", null));

        // Exécution de la méthode à tester
        Equipe result = equipeService.addEquipe(equipe);

        // Vérification des résultats
        assertNotNull(result);
        assertEquals("Equipe C", result.getNomEquipe());
    }
}
