package fr.uge.database.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;

public class Serialization {
	
	/**
	 * Serialize un objet sous forme de chaine de caractères en base 64.
	 * 
	 * @param object L'objet à sérializer
	 * @return L'objet sérializé
	 * @throws IOException
	 */
	public static String serialize(Serializable object) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        
        oos.writeObject(object);
        oos.close();
        
        return Base64.getEncoder().encodeToString(baos.toByteArray()); 
	}
	
	/**
	 * Déserialize l'objet décrit par la chaine de caractères en base 64 donnée en paramètre.
	 * 
	 * @param str La chaine sérializé
	 * @return L'objet déserializé
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object deserialize(String str) throws IOException, ClassNotFoundException {
        byte [] data = Base64.getDecoder().decode(str);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
        
        Object o  = ois.readObject();
        ois.close();
        
        return o;
	}
}
