import java.sql.*;
import oracle.jdbc.driver.*;
import java.io.*;

public class ClientJDBC {

    // la connexion avec le SGBD
    protected Connection conn = null;

    // méthode permettant de lire une chaine entrée au clavier
    // prompt est un message affiché à l'écran
    public String readEntry(String prompt) {
         try {
             StringBuffer buffer = new StringBuffer();
             System.out.print(prompt);
             System.out.flush();
             int c = System.in.read();
             while (c != '\n' && c != -1) {
                 buffer.append((char)c);
                 c = System.in.read();
             }
             return buffer.toString().trim();
         }
         catch(IOException ioe) {
             System.err.println(ioe.getClass().toString() + ": " + ioe.getMessage());
             return "";
         }
     }

    // *** liste des fonctions d'accès à la base distante ***


    public void saisirSport() {
	String nom;
        int code, nbre;
        Statement req;
        PreparedStatement reqp;
        ResultSet res; 

        try {
           nom = readEntry("Donner le nom du sport : ");
        
           req = conn.createStatement();
           res = req.executeQuery("select max(code_sport) from sport");
           
           if (res.next()) 
              code = res.getInt(1); 
           else
	       code = 1;
        
           req.close();

           code++;
           reqp = conn.prepareStatement("insert into sport values(?,?)");
           reqp.setInt(1,code);
           reqp.setString(2,nom);
           nbre = reqp.executeUpdate();
           System.out.println("--" + nbre);
           req.close();

	}catch (SQLException ex) {
            System.out.println("Erreur : " + ex);
	}

    }



    public void supprimerSport() {
	String nom, ch;
        int code, nbre;
        Statement req; 
        PreparedStatement reqp;
        ResultSet res; 

        try {
        
           req = conn.createStatement();
           res = req.executeQuery("select * from sport");
           
           while (res.next()) {
              code = res.getInt(1); 
              nom = res.getString(2);
              System.out.println(code + " - " + nom);   
	   } 
  
           req.close();
           ch = readEntry("    Donner le numero du sport a supprimer : ");
           code = Integer.parseInt(ch);
          
           reqp = conn.prepareStatement("delete from sport where code_sport = ?");
           reqp.setInt(1,code);
           nbre = reqp.executeUpdate();
           reqp.close();

	}catch (SQLException ex) {
            System.out.println("Erreur : " + ex);
	}

    }



    public void saisirDiscipline() 
    {
        System.out.println(" \n --- Pas encore implémentée !!! ---\n");
    }



    public void supprimerDiscipline() 
    {
	System.out.println(" \n --- Pas encore implémentée !!! ---\n");
    }


    public void listerSports() {
	String nom;
        int code, nbre;
        Statement req;
        ResultSet res; 

        try {
        
           req = conn.createStatement();
           res = req.executeQuery("select * from sport");
           
           while (res.next()) {
              code = res.getInt(1); 
              nom = res.getString(2);
              System.out.println(code + " - " + nom);   
	   } 
  
           req.close();

	}catch (SQLException ex) {
            System.out.println("Erreur : " + ex);
	}

    }


    public void listerDisciplines()
    {
	System.out.println(" \n --- Pas encore implémentée !!! ---\n");
    }

    public void disciplinesDunSport() 
    {
	System.out.println(" \n --- Pas encore implémentée !!! ---\n");
    }

    public void sportDuneDiscipline()
    {
	System.out.println(" \n --- Pas encore implémentée !!! ---\n");
    }

 


    // connexion avec le SGBD. Retourne faux si la connexion a échoué
    public boolean connexion(String url, String login, String mdp)
    {
	   
        try {
	    // chargement d'un pilote JDBC MySQL
			Class c=Class.forName("com.mysql.jdbc.Driver");
			Driver pilote=(Driver) c.newInstance();
			DriverManager.registerDriver(pilote);
            // creation d'une connexion
            conn = DriverManager.getConnection(url, login, mdp);
	    return true;
	}
	catch(Exception e)
	{
		System.err.println("Erreur de connexion : "+e);
		return false;
	}
     }

    // fermeture de la connexion avec le SGDB
    public void close() 
    {
	
	try {
            // fermeture de la connexion
            conn.close(); 
	}
	catch(Exception e) {
	    System.err.println(" Problème lors de la fermeture de la connexion : "+e);
        }
     } 





    public static void main (String argv[])  {

	ClientJDBC jdbc = new ClientJDBC();
        String ch;
        int choix = 0;

	if (argv.length < 2) {
        	System.err.println("Erreur : paramètres manquants");
		System.err.println("Usage : java ClientJDBC login mdp");
		System.exit(-1);
        }

	if (!jdbc.connexion("jdbc:mysql://localhost:3306/exemple_ecole",argv[0], argv[1]))
	{
		System.err.println("Impossible de se connecter à la base");
		System.exit(-1);
	}


	do {
		System.out.println("\n\n---------------- Consultation ----------------");
		
                System.out.println("1 - consulter la liste des sports");
		System.out.println("2 - consulter la liste des disciplines");	
                System.out.println("3 - consulter les disciplines d'un sport donné");
		System.out.println("4 - consulter le sport d'une discipline");
		
		System.out.println("---------------- Modification ----------------");
                
		System.out.println("5 - saisir un nouveau sport");
                System.out.println("6 - supprimer un sport");
                System.out.println("7 - saisir une discipline");
                System.out.println("8 - supprimer une discipline");

		System.out.println("---------------- Quitter ----------------");
                System.out.println("0 - quitter");
                System.out.println("  ");

		try {
                    ch = jdbc.readEntry("Votre choix : ");
                    choix = Integer.parseInt(ch);

			
         	    switch(choix) {

			case 1 :
			    jdbc.listerSports();
			    break;
                	case 2 : 
	 		    jdbc.listerDisciplines();
			    break;
                        case 3 :
                            jdbc.disciplinesDunSport();
                            break;
                        case 4 :
                            jdbc.sportDuneDiscipline();
                            break;

			case 5 :
			    jdbc.saisirSport();
			    break;
                	case 6 : 
	 		    jdbc.supprimerSport();
			    break;
                        case 7 :
                            jdbc.saisirDiscipline();
                            break;
                        case 8 :
                            jdbc.supprimerDiscipline();
                            break;
		    }
		}
		catch(NumberFormatException e)
		{ 
			System.out.println("*** veuillez entrer un nombre ! ***");
		}
	    }
	while (choix != 0);

	System.out.println("\nAu revoir !");
            
	jdbc.close();

    }
}