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

	public void evoluerEquipes() {
		log.info("Starting team evolution process.");

		// Convert the result of findAll() to a List
		List<Equipe> equipes = new ArrayList<>((Collection<Equipe>) equipeRepository.findAll());
		log.debug("Total teams to evaluate: {}", equipes.size());

		for (Equipe equipe : equipes) {
			if (equipe.getNiveau() == Niveau.JUNIOR || equipe.getNiveau() == Niveau.SENIOR) {
				log.debug("Evaluating team: {}", equipe.getIdEquipe());

				List<Etudiant> etudiants = new ArrayList<>(equipe.getEtudiants());
				if (etudiants == null || etudiants.isEmpty()) {
					log.warn("No students found in team: {}", equipe.getIdEquipe());
					continue; // Skip to the next team
				}
				long oneYearInMillis = 1000L * 60 * 60 * 24 * 365;
				int nbEtudiantsAvecContratsActifs = 0;

				for (Etudiant etudiant : etudiants) {
					log.debug("Checking student: {}", etudiant.getIdEtudiant());
					Set<Contrat> contrats = etudiant.getContrats();

					if (contrats != null) {
						Date dateSysteme = new Date();


						for (Contrat contrat : contrats) {
							log.debug("Checking contract: {}", contrat.getIdContrat());

							// Simplify the boolean condition
							boolean isActiveContract = !contrat.getArchive()
									&& (dateSysteme.getTime() - contrat.getDateFinContrat().getTime() > oneYearInMillis);

							if (isActiveContract) {
								nbEtudiantsAvecContratsActifs++;
								log.debug("Active contract found for student: {}", contrat.getIdContrat());
							}

						}
					}

					if (nbEtudiantsAvecContratsActifs >= 3) {
						break; // No need to check further students
					}
				}

				if (nbEtudiantsAvecContratsActifs >= 3) {
					upgradeTeam(equipe);
				}
			}
		}

		log.info("Team evolution process completed.");
	}



	private void upgradeTeam(Equipe equipe) {
		log.info("Team {} meets the criteria for evolution.", equipe.getIdEquipe());

		if (equipe.getNiveau() == Niveau.JUNIOR) {
			equipe.setNiveau(Niveau.SENIOR);
			log.info("Team {} upgraded from JUNIOR to SENIOR.", equipe.getIdEquipe());
		} else if (equipe.getNiveau() == Niveau.SENIOR) {
			equipe.setNiveau(Niveau.EXPERT);
			log.info("Team {} upgraded from SENIOR to EXPERT.", equipe.getIdEquipe());
		}

		equipeRepository.save(equipe);
		log.debug("Team {} updated in the repository.", equipe.getIdEquipe());
	}

}
