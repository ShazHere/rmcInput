package inputGenerator;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.joda.time.DateTime;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import shaz.rmc.core.domain.Problem;
import shaz.rmc.core.domain.XMLProblemDAO;



/**
 * @author Shaza
 *
 */
public class Generator {

	private static FileWriter writer;
	private static XmlWriter xmlWriter;
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) {
	
		RandomGenerator rg = new MersenneTwister();
		xmlWriter = new XmlWriter(rg);
		
			for (int i = 0; i< Param.TOTAL_FILES; i++) {
				writeFile(i);
			}
		
		//writeFile("NEWSample", toXML(problem));
		//Problem problem = loadProblem();
	}
	
	/**
	 * Loads the problem file into GlobalParameters.PROBLEM using XMLPROBLEMDAO 
	 * @param sim Simulator instance.
	 * 
	 */
	protected static Problem loadProblem() {
		XMLProblemDAO dao = new XMLProblemDAO(new File( Param.RESULT_FOLDER+"6/1.2_6.xml"));
		Problem problem = null;
		try {
			problem = dao.loadProblem();
		} catch (IOException e) {
			e.printStackTrace();
		}
		assert problem == null : "Problem is not Loaded!";
		return problem;
		//log.debug("problem is LOADED, CURRENT_TIME is = "  + new DateTime(GlobalParameters.START_DATETIME.getMillis() + (sim.getCurrentTime())) );
		 
	}

	protected static void writeFile(int fileNo) {
		try {
			int noOfTries = 0;
			BigDecimal stress;
			int scale;
			String xmlString;
			do {
				xmlString = xmlWriter.makeXML();
				stress = xmlWriter.getStress();
				scale = xmlWriter.getScale();
				noOfTries++;
			} while (!isAcceptableStressScale(stress, scale)); //if you want a specific stress and scale value
			//} while (!isAcceptableStress(stress)); // for bulk running, and producing bulk files without any specific scale and stress values.
			System.out.println("NO of tries = " + noOfTries);
			writeXmlFile(stress,scale, xmlString, fileNo);
		} 
		catch (IOException e) {
			System.out.println("EXCEption");
			e.printStackTrace();
		}
	}

	/**
	 * @param stress
	 * @param xmlString
	 * @param fileNo 
	 * @throws IOException
	 */
	private static void writeXmlFile(BigDecimal stress, int scale,
			String xmlString, int fileNo) throws IOException {
		
		
		String fileName = stress+ "_" + scale + "_" + fileNo;//new DateTime().toString("dd-MM-yyy_kk-mm-ss");
		createDirectory(scale);
		writer = new FileWriter(new File(Param.RESULT_FOLDER+scale+"/"+fileName +".xml"));
		writer.write(xmlString);
		writer.close();
		System.out.println("File writen: " + Param.RESULT_FOLDER +scale+ "/" +fileName+ ".xml");
	}

	private static void createDirectory(int scale) {
		String dirName = Param.RESULT_FOLDER + "/" + scale;
		 File theDir = new File(dirName);

		  // if the directory does not exist, create it
		  if (!theDir.exists()) {
		    System.out.println("creating directory: " + dirName);
		    boolean result = theDir.mkdir();  

		     if(result) {    
		       System.out.println("DIR created");  
		     }
		  }		
	}

	/**
	 * @param stress
	 * @return true if its is just ok for any stress and scale value. 
	 */
	private static boolean isAcceptableStress(BigDecimal stress) {
		BigDecimal beforeDecimal = stress.setScale(1, RoundingMode.HALF_UP);
		if (beforeDecimal.compareTo(stress) == 0)
			return true;
		return false;
	}
	/**
	 * @param stress
	 * @param scale
	 * @return true or false, according to required values, and parameters given
	 * 
	 * If I need some precise inputFile, then here I have to modify requied scale and required stress value.
	 */
	private static boolean isAcceptableStressScale(BigDecimal stress, int scale) {
		int requiredScale = 14; //specify requried sccale here
		BigDecimal requiredStress = new BigDecimal(1.5).setScale(1, RoundingMode.HALF_UP); //specify required stress here
		Boolean scaleAns=false, stressAns = false;
		if (requiredScale != 0){
			if (scale == requiredScale)
				scaleAns = true;
		}
		else
			scaleAns = true;
		
		if (requiredStress.equals(new BigDecimal(0)) == false){
			if (requiredStress.equals(stress) == true)
				stressAns = true;
		}
		else stressAns = true;
			
		return scaleAns && stressAns;
	}
}
