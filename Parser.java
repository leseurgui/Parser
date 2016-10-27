import java.io.File;
import java.io.Reader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Parser {
	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Usage: java Parser fichier.csv");
			return;
		}

		if (args.length != 1) {
			System.err.println("Nombre d'arguments incorrect.");
			return;
		}
		Iterable<CSVRecord> parser = null;
		try {
			Reader fichier = new FileReader(args[0]);
			parser = CSVFormat.DEFAULT.withDelimiter(';').withHeader().parse(fichier);
		} catch (IOException e1) {
			System.err.println("Impossible de lire le fichier.");
			return;
		}
		/*
		 * Etape 1 : récupération d'une instance de la classe
		 * "DocumentBuilderFactory"
		 */
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			String temp = "";
			System.out.println("avant boucle");
			for (CSVRecord ligne : parser) {
				temp = ligne.get("tend");
        		System.out.println("fdp");
			}
			/*
			 * Etape 2 : création d'un parseur
			 */
			final DocumentBuilder builder = factory.newDocumentBuilder();
			/*
			 * Etape 3 : création d'un Document
			 */
			final Document document = builder.newDocument();
			/*
			 * Etape 4 : création de l'Element racine
			 */
			final Element racine = document.createElement("meteo");
			document.appendChild(racine);
			/*
			 * Etape 5 : création d'une personne
			 */
			final Comment commentaire = document.createComment(temp);
			racine.appendChild(commentaire);
			final Element personne = document.createElement("personne");
			personne.setAttribute("sexe", "masculin");
			racine.appendChild(personne);
			/*
			 * Etape 6 : création du nom et du prénom
			 */
			final Element nom = document.createElement("nom");
			nom.appendChild(document.createTextNode("DOE"));
			final Element prenom = document.createElement("prenom");
			prenom.appendChild(document.createTextNode("John"));
			personne.appendChild(nom);
			personne.appendChild(prenom);
			/*
			 * Etape 7 : récupération des numéros de téléphone
			 */
			final Element telephones = document.createElement("telephones");
			final Element fixe = document.createElement("telephone");
			fixe.appendChild(document.createTextNode("01 02 03 04 05"));
			fixe.setAttribute("type", "fixe");
			final Element portable = document.createElement("telephone");
			portable.appendChild(document.createTextNode("06 07 08 09 10"));
			portable.setAttribute("type", "portable");
			telephones.appendChild(fixe);
			telephones.appendChild(portable);
			personne.appendChild(telephones);
			/*
			 * Etape 8 : affichage
			 */
			final TransformerFactory transformerFactory = TransformerFactory.newInstance();
			final Transformer transformer = transformerFactory.newTransformer();
			final DOMSource source = new DOMSource(document);
			final StreamResult sortie = new StreamResult(new File("file.xml"));
			// final StreamResult result = new StreamResult(System.out);
			// prologue
			transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
			// formatage
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
			transformer.transform(source, sortie);
		}

		catch (final ParserConfigurationException e) {
			e.printStackTrace();
		}

		catch (TransformerConfigurationException e) {
			e.printStackTrace();
		}

		catch (TransformerException e) {
			e.printStackTrace();
		}

	}
}
