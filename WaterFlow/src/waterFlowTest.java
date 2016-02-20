import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;


public class waterFlowTest {
	private List<String> readFile(String fileName) {
		Path path = Paths.get(new File(fileName).getAbsolutePath());
		try {
			return Files.readAllLines(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Test
	public void testSimple() {
		String inputFileName = "test_input_file.txt";
		String expectedOutputFileName = "test_output_file.txt";
		String outputFileName = "output.txt";

		waterFlow p1 = new waterFlow();
		List<InputNode> inpReturnList = p1.readFile(inputFileName);

		p1.process(inpReturnList);

		Assert.assertEquals(readFile(expectedOutputFileName), readFile(outputFileName));
		
		System.out.println(p1);
	}
}