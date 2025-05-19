package com.pickandgo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@Component
public class DatabaseTester implements ApplicationRunner {

    private final DataSource dataSource;

    @Autowired
    public DatabaseTester(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(ApplicationArguments args) {
        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement()) {

            System.out.println("========== TEST DE CONNEXION À LA BASE DE DONNÉES ==========");
            System.out.println("Connexion établie avec succès à : " + connection.getMetaData().getURL());

            // Vérification que la table existe et affichage de quelques données
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM Utilisateur")) {
                if (rs.next()) {
                    System.out.println("Nombre d'utilisateurs : " + rs.getInt(1));
                }
            }

            // Affichage de quelques utilisateurs
            try (ResultSet rs = stmt.executeQuery("SELECT * FROM Utilisateur LIMIT 5")) {
                System.out.println("Liste des utilisateurs :");
                boolean hasData = false;

                while (rs.next()) {
                    hasData = true;
                    System.out.println("nom: " + rs.getString("nomU") +
                            ", prenom: " + rs.getString("prenomU"));
                }

                if (!hasData) {
                    System.out.println("Aucun utilisateur trouvé dans la base de données.");
                }
            }

            System.out.println("CONNEXION À LA BASE DE DONNÉES RÉUSSIE !");
            System.out.println("=========================================================");

        } catch (Exception e) {
            System.err.println("ERREUR DE CONNEXION À LA BASE DE DONNÉES :");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}