import java.text.DecimalFormat;

/**
 *
 * This is the directly given code for the TestFile class. I have implemented it to be used by the DataSource class.
 * The only change I made was adding guessClass to display what the program thought the class was.
 *
 * @author  Justin Duong(100588398)
 * @version 1.0
 * @since   1/21/2017
 *
 */

public class TestFile {
    private String fileName;
    private String actualClass;
    private String guessClass;
    private double spamProbability;

    public TestFile(String fileName, String actualClass,String guessClass, double spamProbability) {
        this.fileName = fileName;
        this.actualClass = actualClass;
        this.guessClass = guessClass;
        this.spamProbability = spamProbability;
    }
    public String getGuessClass() {
        return this.guessClass;
    }

    public String getFileName() {
        return this.fileName;
    }

    public double getSpamProbability() {
        return this.spamProbability;
    }

    public String getSpamProbRounded() {
        DecimalFormat df = new DecimalFormat("0.00000");
        return df.format(this.spamProbability);
    }

    public String getActualClass() {
        return this.actualClass;
    }

    public void setFilename(String value) {
        this.fileName = value;
    }

    public void setSpamProbability(double val) {
        this.spamProbability = val;
    }

    public void setActualClass(String value) {
        this.actualClass = value;
    }



}
