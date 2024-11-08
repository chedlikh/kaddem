package tn.esprit.spring.kaddem.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.kaddem.dto.EquipeDTO;
import tn.esprit.spring.kaddem.entities.Equipe;
import tn.esprit.spring.kaddem.entities.Niveau;
import tn.esprit.spring.kaddem.services.IEquipeService;

import java.util.List;
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/equipe")
public class EquipeRestController {
	IEquipeService equipeService;
	// http://localhost:8089/Kaddem/equipe/retrieve-all-equipes
	@GetMapping("/retrieve-all-equipes")
	public List<Equipe> getAllEquipes() {
		log.info("form controller GET request to retrieve all teams.");
		List<Equipe> equipes = equipeService.retrieveAllEquipes();
		log.debug("from controller Retrieved {} teams.", equipes.size());
		return equipes;
	}
	// http://localhost:8089/Kaddem/equipe/retrieve-equipe/8
	@GetMapping("/retrieve-equipe/{equipe-id}")
	public Equipe retrieveEquipe(@PathVariable("equipe-id") Integer equipeId) {
		return equipeService.retrieveEquipe(equipeId);
	}

	// http://localhost:8089/Kaddem/equipe/add-equipe

	@PostMapping("/add-equipe")
	public EquipeDTO addEquipe(@RequestBody EquipeDTO equipeDTO) {
		Equipe equipe = new Equipe();
		equipe.setNomEquipe(equipeDTO.getNomEquipe());
		equipe.setNiveau(Niveau.valueOf(String.valueOf(equipeDTO.getNiveau())));
		equipe.setIdEquipe(equipe.getIdEquipe());

		Equipe savedEquipe = equipeService.addEquipe(equipe);
		return new EquipeDTO(savedEquipe.getIdEquipe(), savedEquipe.getNomEquipe(), savedEquipe.getNiveau());
	}

	// http://localhost:8089/Kaddem/equipe/remove-equipe/1
	@DeleteMapping("/remove-equipe/{equipe-id}")
	public void removeEquipe(@PathVariable("equipe-id") Integer equipeId) {
		equipeService.deleteEquipe(equipeId);
	}

	// http://localhost:8089/Kaddem/equipe/update-equipe
	@PutMapping("/update-equipe")
	public EquipeDTO updateEquipe(@RequestBody EquipeDTO equipeDTO) {
		// Convert DTO to entity
		Equipe equipe = new Equipe();
		equipe.setIdEquipe(equipeDTO.getIdEquipe());
		equipe.setNomEquipe(equipeDTO.getNomEquipe());

		// Directly set the Niveau enum
		equipe.setNiveau(equipeDTO.getNiveau());

		// Call the service to update the entity
		Equipe updatedEquipe = equipeService.updateEquipe(equipe);

		// Convert back to DTO to return
		return new EquipeDTO(updatedEquipe.getIdEquipe(), updatedEquipe.getNomEquipe(), updatedEquipe.getNiveau());
	}
	@PostMapping("/{etudiantId}/assign-to-equipe/{equipeId}")
	public String assignEtudiantToEquipe(@PathVariable Integer etudiantId, @PathVariable Integer equipeId) {
		return equipeService.assignEtudiantToEquipe(etudiantId, equipeId);
	}



}


