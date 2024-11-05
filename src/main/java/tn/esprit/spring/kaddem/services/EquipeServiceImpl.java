package tn.esprit.spring.kaddem.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Equipe;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.entities.Niveau;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;

import java.util.*;

@Slf4j
@AllArgsConstructor
@Service
public class EquipeServiceImpl implements IEquipeService {
	EquipeRepository equipeRepository;


	public List<Equipe> retrieveAllEquipes() {
		log.info("Starting to retrieve all teams from the repository.");
		List<Equipe> equipes = (List<Equipe>) equipeRepository.findAll();
		if (equipes.isEmpty()) {
			log.warn("No teams found in the repository.");
		} else {
			log.debug("Number of teams retrieved: {}", equipes.size());
		}
		log.info("Finished retrieving all teams.");
		return equipes;
	}


	public Equipe addEquipe(Equipe e) {
		log.info("Adding a new team: {}", e);
		Equipe savedEquipe = equipeRepository.save(e);
		log.debug("Team added successfully with ID: {}", savedEquipe.getIdEquipe());
		return savedEquipe;
	}

	public void deleteEquipe(Integer idEquipe) {
		log.info("Deleting team with ID: {}", idEquipe);
		Equipe e = retrieveEquipe(idEquipe);
		equipeRepository.delete(e);
		log.info("Team with ID: {} has been deleted.", idEquipe);
	}

	public Equipe retrieveEquipe(Integer equipeId) {
		log.info("Retrieving team with ID: {}", equipeId);
		Equipe equipe = equipeRepository.findById(equipeId).orElse(null);
		if (equipe == null) {
			log.error("Team with ID: {} not found.", equipeId);
		} else {
			log.debug("Team found: {}", equipe);
		}
		return equipe;
	}

	public Equipe updateEquipe(Equipe e) {
		log.info("Updating team with ID: {}", e.getIdEquipe());
		Equipe updatedEquipe = equipeRepository.save(e);
		log.debug("Team updated: {}", updatedEquipe);
		return updatedEquipe;
	}

}
