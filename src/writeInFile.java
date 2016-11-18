import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class writeInFile {
	public writeInFile(String a) {
		try {
			List<String> lines = Arrays.asList(a);
			Path file = Paths.get("/Users/hosamhany/Desktop/abc.pl");
			Files.write(file, lines, Charset.forName("UTF-8"));
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String createPredicate(String name, ArrayList<Integer> parameters) {
		String params = "";
		for (Integer parameter : parameters) {
			if (params.equals("")) {
				params = params + parameter;
			} else {
				params = params + "," + parameter;
			}
		}
		return name + "(" + params + ")";
	}

	public static void main(String[] args) {
		ArrayList<Integer> x = new ArrayList<Integer>();
		for (int i = 1; i < 7; i++) {
			x.add(i);
		}
		String res = createPredicate("check", x);
		writeInFile c = new writeInFile(res);
		System.out.println("eshta");
	}

}
