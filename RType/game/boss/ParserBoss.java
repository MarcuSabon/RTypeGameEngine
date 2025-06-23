package boss;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ParserBoss {

	public static String[][] parseTableauFromFile(String fileName) throws IOException {
		InputStream inputStream = ParserBoss.class.getResourceAsStream(fileName);
		if (inputStream == null) {
			throw new IOException("File not found: " + fileName);
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String line;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");

		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
			stringBuilder.append(ls);
		}

		reader.close();

		String tableauStr = stringBuilder.toString();

		String[] lignes = tableauStr.split(ls);

		String[][] body = new String[lignes.length][];

		for (int i = 0; i < lignes.length; i++) {
			String[] elements = lignes[i].trim().split("\\s+");
			body[i] = new String[elements.length];

			for (int j = 0; j < elements.length; j++) {
				body[i][j] = elements[j];
			}
		}

		return body;
	}
}
