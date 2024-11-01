package tn.esprit.spring.kaddem.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tn.esprit.spring.kaddem.dto.ContratDTO;
import tn.esprit.spring.kaddem.entities.Contrat;

import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class ContratServiceImpl implements IContratService{

private final ContratRepository contratRepository;
	private EtudiantRepository etudiantRepository; // Assuming you have this repository

	public List<Contrat> retrieveAllContrats(){
		return  contratRepository.findAll();
	}

	public Contrat updateContrat(ContratDTO c) {
		// Find the existing Contrat entity by ID
		Contrat contrat = contratRepository.findById(c.getEtudiantId())
				.orElseThrow(() -> new RuntimeException("Contrat not found with id: " + c.getEtudiantId()));

		// Update fields in the existing Contrat entity
		contrat.setDateDebutContrat(c.getDateDebutContrat());
		contrat.setDateFinContrat(c.getDateFinContrat());
		contrat.setSpecialite(c.getSpecialite());
		contrat.setArchive(c.getArchive());
		contrat.setMontantContrat(c.getMontantContrat());

		// Fetch the Etudiant entity by its ID
		if (c.getEtudiantId() != null) {
			Etudiant etudiant = etudiantRepository.findById(c.getEtudiantId()).orElse(null);
			contrat.setEtudiant(etudiant);
		}

		// Save the updated Contrat entity to the database
		return contratRepository.save(contrat);
	}

	public Contrat addContrat(ContratDTO c) {
		// Create a new Contrat entity
		Contrat contrat = new Contrat();

		// Set the fields from the DTO to the entity
		contrat.setDateDebutContrat(c.getDateDebutContrat());
		contrat.setDateFinContrat(c.getDateFinContrat());
		contrat.setSpecialite(c.getSpecialite());
		contrat.setArchive(c.getArchive());
		contrat.setMontantContrat(c.getMontantContrat());

		// Fetch the Etudiant entity by its ID
		if (c.getEtudiantId() != null) {
			Etudiant etudiant = etudiantRepository.findById(c.getEtudiantId()).orElse(null);
			contrat.setEtudiant(etudiant);
		}

		// Save the Contrat entity to the database
		return contratRepository.save(contrat);
	}

	public Contrat retrieveContrat (Integer  idContrat){
		return contratRepository.findById(idContrat).orElse(null);
	}

	public  void removeContrat(Integer idContrat){
		Contrat c=retrieveContrat(idContrat);
		contratRepository.delete(c);
	}



}
