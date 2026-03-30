package com.example.demo.config;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final FournisseurRepository fournisseurRepository;
    private final MedicamentRepository medicamentRepository;
    private final LotRepository lotRepository;
    private final ClientRepository clientRepository;
    private final VenteRepository venteRepository;
    private final VenteLigneRepository venteLigneRepository;
    private final CommandeRepository commandeRepository;
    private final CommandeLigneRepository commandeLigneRepository;
    private final AuditLogRepository auditLogRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("--- Démarrage de l'initialisation des données VitaGest ---");
        
        // 1. Purge Strategy (Order is crucial due to FK)
        venteLigneRepository.deleteAll();
        venteRepository.deleteAll();
        commandeLigneRepository.deleteAll();
        commandeRepository.deleteAll();
        lotRepository.deleteAll();
        auditLogRepository.deleteAll();
        medicamentRepository.deleteAll();
        clientRepository.deleteAll();
        fournisseurRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();

        // 2. Roles & Users
        Role adminRole = roleRepository.save(new Role(null, "ROLE_ADMIN"));
        Role pharmacienRole = roleRepository.save(new Role(null, "ROLE_PHARMACIEN"));

        User admin = User.builder()
                .nomComplet("Administrator")
                .email("admin@vitagest.com")
                .password(passwordEncoder.encode("admin123"))
                .role(adminRole)
                .build();
        userRepository.save(admin);

        List<User> employees = Arrays.asList(
                User.builder().nomComplet("Ali Mansouri").email("ali@vitagest.com").password(passwordEncoder.encode("pass123")).role(pharmacienRole).build(),
                User.builder().nomComplet("Sara El Fassi").email("sara@vitagest.com").password(passwordEncoder.encode("pass123")).role(pharmacienRole).build(),
                User.builder().nomComplet("Omar Bennani").email("omar@vitagest.com").password(passwordEncoder.encode("pass123")).role(pharmacienRole).build()
        );
        userRepository.saveAll(employees);

        // 3. Suppliers (Moroccan)
        List<Fournisseur> suppliers = Arrays.asList(
                new Fournisseur(null, "Sothema", "contact@sothema.ma", "0522404040", 2, 4.5),
                new Fournisseur(null, "Cooper Maroc", "info@cooper.ma", "0522303030", 1, 4.8),
                new Fournisseur(null, "Promopharm", "sales@promopharm.ma", "0522505050", 3, 4.2),
                new Fournisseur(null, "Afric-Phar", "contact@africphar.ma", "0522606060", 2, 4.0),
                new Fournisseur(null, "Laprophan", "info@laprophan.ma", "0522707070", 2, 4.7),
                new Fournisseur(null, "Bottu", "contact@bottu.com", "0522808080", 1, 4.6),
                new Fournisseur(null, "Galenica", "info@galenica.ma", "0522909090", 4, 3.9),
                new Fournisseur(null, "Pharma 5", "sales@pharma5.ma", "0522202020", 2, 4.4),
                new Fournisseur(null, "Maphar", "contact@maphar.ma", "0522101010", 3, 4.1),
                new Fournisseur(null, "Zenith Pharma", "info@zenith.ma", "0522444444", 2, 4.3)
        );
        fournisseurRepository.saveAll(suppliers);

        // 4. Medicaments (Catalog)
        // Order: id, dci, nom, forme, dosage, prix, classe, codeAtc, vignetteUrl, lots
        List<Medicament> catalog = Arrays.asList(
                new Medicament(null, "Paracétamol", "Doliprane 500mg", "Comprimé", "500mg", new BigDecimal("15.50"), "Analgésique", "N02BE01", null, null),
                new Medicament(null, "Paracétamol", "Doliprane 1g", "Comprimé", "1g", new BigDecimal("26.20"), "Analgésique", "N02BE01", null, null),
                new Medicament(null, "Phloroglucinol", "Spasfon", "Comprimé", "80mg", new BigDecimal("32.00"), "Antispasmodique", "A03AX12", null, null),
                new Medicament(null, "Amoxicilline", "Augmentin 1g", "Sachet", "1g", new BigDecimal("112.50"), "Antibiotique", "J01CR02", null, null),
                new Medicament(null, "Paracétamol/Caféine", "Panadol Extra", "Comprimé", "500mg/65mg", new BigDecimal("45.00"), "Analgésique", "N02BE51", null, null),
                new Medicament(null, "Pseudoéphédrine", "Rhumafed", "Comprimé", "60mg", new BigDecimal("28.00"), "Rhume", "R01BA52", null, null),
                new Medicament(null, "Salbutamol", "Ventoline", "Inhalateur", "100µg", new BigDecimal("55.40"), "Asthme", "R03AC02", null, null),
                new Medicament(null, "Alginate", "Gaviscon", "Sirop", "250ml", new BigDecimal("48.00"), "Anti-acide", "A02BX13", null, null),
                new Medicament(null, "Povidone iodée", "Betadine", "Solution", "125ml", new BigDecimal("22.50"), "Antiseptique", "D08AL01", null, null),
                new Medicament(null, "Paracétamol", "Humex Rhume", "Comprimé", "500mg", new BigDecimal("35.00"), "Rhume", "R05X", null, null),
                new Medicament(null, "Paracétamol/Phéniramine", "Fervex Adulte", "Sachet", "500mg", new BigDecimal("42.00"), "Grippe", "R05X", null, null),
                new Medicament(null, "Paracétamol", "Dafalgan 1g", "Gélule", "1g", new BigDecimal("25.80"), "Analgésique", "N02BE01", null, null),
                new Medicament(null, "Alpha-amylase", "Maxilase", "Sirop", "200ml", new BigDecimal("38.50"), "Maux de gorge", "R02A", null, null),
                new Medicament(null, "Aspirine", "Aspégic 500mg", "Sachet", "500mg", new BigDecimal("20.00"), "Analgésique", "N02BA01", null, null),
                new Medicament(null, "Métopimazine", "Vogalène", "Comprimé", "15mg", new BigDecimal("44.20"), "Anti-nauséeux", "A04AD05", null, null),
                new Medicament(null, "Alvérine", "Meteospasmyl", "Gélule", "60mg", new BigDecimal("52.00"), "Digestif", "A03AX08", null, null),
                new Medicament(null, "Desloratadine", "Aerius", "Comprimé", "5mg", new BigDecimal("85.00"), "Antihistaminique", "R06AX27", null, null),
                new Medicament(null, "Cétirizine", "Zyrtec", "Comprimé", "10mg", new BigDecimal("72.00"), "Antiallergique", "R06AE07", null, null),
                new Medicament(null, "Amoxicilline", "Clamoxyl 500mg", "Gélule", "500mg", new BigDecimal("65.00"), "Antibiotique", "J01CA04", null, null),
                new Medicament(null, "Céfixime", "Oroken", "Suspension", "40mg/5ml", new BigDecimal("145.00"), "Antibiotique", "J01DD08", null, null),
                new Medicament(null, "Pristinamycine", "Pyostacine", "Comprimé", "500mg", new BigDecimal("210.00"), "Antibiotique", "J01FG01", null, null),
                new Medicament(null, "Oméprazole", "Mopral 20mg", "Gélule", "20mg", new BigDecimal("120.00"), "Anti-ulcéreux", "A02BC01", null, null),
                new Medicament(null, "Esoméprazole", "Inexium 40mg", "Comprimé", "40mg", new BigDecimal("185.00"), "Anti-ulcéreux", "A02BC05", null, null),
                new Medicament(null, "Diclofénac", "Voltarène 50mg", "Comprimé", "50mg", new BigDecimal("58.00"), "Anti-inflammatoire", "M01AB05", null, null),
                new Medicament(null, "Kétoprofène", "Profénid 100mg", "Comprimé", "100mg", new BigDecimal("62.50"), "Anti-inflammatoire", "M01AE03", null, null),
                new Medicament(null, "Ibuprofène", "Advil 400mg", "Comprimé", "400mg", new BigDecimal("34.00"), "Analgésique", "M01AE01", null, null),
                new Medicament(null, "Ibuprofène", "Nurofen", "Comprimé", "200mg", new BigDecimal("28.00"), "Analgésique", "M01AE01", null, null),
                new Medicament(null, "Paracétamol", "Efferalgan 500mg", "Effervescent", "500mg", new BigDecimal("18.00"), "Analgésique", "N02BE01", null, null),
                new Medicament(null, "Diosmectite", "Smecta", "Sachet", "3g", new BigDecimal("46.00"), "Anti-diarrhéique", "A07BC05", null, null),
                new Medicament(null, "Lopéramide", "Imodium", "Gélule", "2mg", new BigDecimal("35.00"), "Anti-diarrhéique", "A07DA03", null, null),
                new Medicament(null, "Alumine/Magnésie", "Maalox", "Comprimé", "400mg/400mg", new BigDecimal("31.00"), "Anti-acide", "A02AD01", null, null),
                new Medicament(null, "Trolamine", "Biafine", "Emulsion", "93g", new BigDecimal("55.00"), "Brûlures", "D03AX12", null, null)
        );
        medicamentRepository.saveAll(catalog);

        // 5. Inventory Intelligence (Lots)
        // Order: id, numeroLot, fabrication, expiration, quantite, medicament
        List<Lot> lots = new ArrayList<>();
        Random rand = new Random();
        for (Medicament med : catalog) {
            // Scenario A: Expiring soon or expired
            lots.add(new Lot(null, "LOT-EXP-" + med.getId(), LocalDate.now().minusMonths(6), LocalDate.now().plusMonths(rand.nextInt(3)), 15, med));
            
            // Scenario B: Healthy Stock
            lots.add(new Lot(null, "LOT-HLT-" + med.getId(), LocalDate.now().minusMonths(12), LocalDate.now().plusYears(2), 100 + rand.nextInt(50), med));
            
            // Scenario C: Low Stock
            lots.add(new Lot(null, "LOT-LOW-" + med.getId(), LocalDate.now().minusMonths(1), LocalDate.now().plusYears(1), 5 + rand.nextInt(3), med));
        }
        lotRepository.saveAll(lots);

        // 6. Clients & Loyalty
        // Order: id, nom, prenom, email, tel, allergies, pointsFidelite, consentRgpd, ventes
        List<Client> moroccanClients = Arrays.asList(
                new Client(null, "Mansouri", "Ahmed", "ahmed.mans@gmail.com", "0661122334", "Pénicilline", true, null),
                new Client(null, "Bennani", "Fatema Zahra", "fz.bennani@yahoo.fr", "0662233445", null, true, null),
                new Client(null, "El Amrani", "Yassine", "yassine.amr@gmail.com", "0663344556", "Pollen", true, null),
                new Client(null, "Benjelloun", "Khadija", "khadija.benj@outlook.com", "0664455667", null, true, null),
                new Client(null, "Tazi", "Mehdi", "mehdi.tazi@gmail.com", "0665566778", "Aspirine", true, null),
                new Client(null, "Alami", "Zineb", "zineb.alami@gmail.com", "0666677889", null, true, null),
                new Client(null, "Idrissi", "Omar", "omar.idrissi@gmail.com", "0667788990", null, true, null),
                new Client(null, "Raoui", "Latifa", "latifa.raoui@gmail.com", "0668899001", "Pénicilline", true, null),
                new Client(null, "Cherkaoui", "Hassan", "hassan.cherk@gmail.com", "0669900112", null, true, null),
                new Client(null, "Ait Ahmed", "Najat", "najat.ait@gmail.com", "0660011223", "Lactose", true, null),
                new Client(null, "Saidi", "Mustapha", "musta.saidi@gmail.com", "0651122334", null, true, null),
                new Client(null, "Lahlou", "Amine", "amine.lahlou@gmail.com", "0652233445", null, true, null),
                new Client(null, "Jabri", "Siham", "siham.jabri@gmail.com", "0653344556", "Fruits à coque", true, null),
                new Client(null, "Berrada", "Rachid", "rachid.berrada@gmail.com", "0654455667", null, true, null),
                new Client(null, "Mernissi", "Salma", "salma.mer@gmail.com", "0655566778", null, true, null),
                new Client(null, "Slaoui", "Anas", "anas.slaoui@gmail.com", "0656677889", null, true, null),
                new Client(null, "Filali", "Meryem", "meryem.filali@gmail.com", "0657788990", "Sulfamides", true, null),
                new Client(null, "Belkhayat", "Hamza", "hamza.belk@gmail.com", "0658899001", null, true, null),
                new Client(null, "Oulidi", "Laila", "laila.oulidi@gmail.com", "0659900112", null, true, null),
                new Client(null, "Bakkali", "Said", "said.bakkali@gmail.com", "0650011223", null, true, null)
        );
        clientRepository.saveAll(moroccanClients);

        // 7. Business Transactions (Sales)
        for (int i = 0; i < 15; i++) {
            Client c = moroccanClients.get(rand.nextInt(moroccanClients.size()));
            User v = employees.get(rand.nextInt(employees.size()));
            Vente vente = new Vente(null, LocalDateTime.now().minusDays(rand.nextInt(30)), BigDecimal.ZERO, "Espèces", c, v, null);
            vente = venteRepository.save(vente);
            
            BigDecimal totalVente = BigDecimal.ZERO;
            int nbLignes = 1 + rand.nextInt(3);
            for (int j = 0; j < nbLignes; j++) {
                Medicament m = catalog.get(rand.nextInt(catalog.size()));
                int qty = 1 + rand.nextInt(3);
                BigDecimal prixLigne = m.getPrix().multiply(new BigDecimal(qty));
                totalVente = totalVente.add(prixLigne);
                VenteLigne ligne = new VenteLigne(null, qty, m.getPrix(), BigDecimal.ZERO, vente, m);
                venteLigneRepository.save(ligne);
            }
            vente.setTotal(totalVente);
            venteRepository.save(vente);
        }

        // 8. Purchase Orders (Commandes)
        String[] status = {"BROUILLON", "EN_ATTENTE", "RECUE"};
        for (int i = 0; i < 5; i++) {
            Fournisseur f = suppliers.get(rand.nextInt(suppliers.size()));
            String currentStatut = (i < 2) ? "RECUE" : status[rand.nextInt(2)]; // Force 2 received
            Commande cmd = new Commande(null, LocalDateTime.now().minusDays(rand.nextInt(10)), currentStatut, BigDecimal.ZERO, f, null);
            cmd = commandeRepository.save(cmd);
            
            BigDecimal totalCmd = BigDecimal.ZERO;
            for (int j = 0; j < 5; j++) {
                Medicament m = catalog.get(rand.nextInt(catalog.size()));
                int qty = 20 + rand.nextInt(80);
                BigDecimal prixAchat = m.getPrix().multiply(new BigDecimal("0.7")); // 30% margin
                totalCmd = totalCmd.add(prixAchat.multiply(new BigDecimal(qty)));
                CommandeLigne ligne = new CommandeLigne(null, qty, prixAchat, LocalDate.now().plusYears(1), cmd, m);
                commandeLigneRepository.save(ligne);
            }
            cmd.setTotalAmount(totalCmd);
            commandeRepository.save(cmd);
        }

        // 9. Audit Logs
        auditLogRepository.save(new AuditLog(null, "INITIALIZATION", "SYSTEM", 1L, LocalDateTime.now(), "Initialisation du système VitaGest terminée avec succès.", admin));
        auditLogRepository.save(new AuditLog(null, "IMPORT", "CATALOG", 30L, LocalDateTime.now(), "Importation de 32 médicaments marocains.", admin));

        System.out.println("--- Initialisation terminée : " + catalog.size() + " médicaments, " + suppliers.size() + " fournisseurs et " + moroccanClients.size() + " clients créés. ---");
    }
}
