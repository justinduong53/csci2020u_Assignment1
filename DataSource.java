import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 *
 * DataSource Class, Process Ham and Spam E-mail folders to train a probability map. Afterwards use the probability map
 * to calculate the chance of a file being a spam file. Give this list of data to the main UI for user display.
 *
 * @author  Justin Duong(100588398)
 * @version 1.0
 * @since   1/24/2017
 *
 */
public class DataSource {
    public static double accur = 0;
    public static double perci = 0;
    public static ArrayList<File> trainHamList = new ArrayList<File>();
    public static ArrayList<File> trainSpamList= new ArrayList<File>();
    public static ArrayList<File> testHamList= new ArrayList<File>();
    public static ArrayList<File> testSpamList= new ArrayList<File>();

    /**
     *
     * processFolder method processes the folders and adds them in their respective lists for use later
     *
     * @param file
     * @throws IOException
     */
    public static void processFolder(File file) throws IOException{
        if(file.getPath().contains("train")) {
            File[] filesInDir = file.listFiles();
            for (int i = 0; i < filesInDir.length; i++) {
                if(filesInDir[i].getPath().contains("ham")) {
                    trainHamList.add(filesInDir[i]);
                }
                else if(filesInDir[i].getPath().contains("spam")) {
                    trainSpamList.add(filesInDir[i]);
                }
            }
        }
        else if(file.getPath().contains("test")){
            File[] filesInDir = file.listFiles();
            for (int i = 0; i < filesInDir.length; i++) {
                if(filesInDir[i].getPath().contains("ham")) {

                    testHamList.add(filesInDir[i]);
                }
                else if(filesInDir[i].getPath().contains("spam")) {
                    testSpamList.add(filesInDir[i]);
                }
            }
        }

        else{
            if (file.isDirectory()) {
                File[] filesInDir = file.listFiles();
                for (int i = 0; i < filesInDir.length; i++) {
                    processFolder(filesInDir[i]);
                }
            }
        }


    }

    /**
     *
     * getData method is the main calculating and data/file processing method. The process of this method depends on four
     * static ArrayLists that I wrote(TrainHamList,TrainSpamList,TestHamList,TestSpamList). These are lists corresponding
     * to their respective folder categories(e.g Train->Ham and Ham2 would go in TrainHamList) and use the processFile
     * method to collect this list data. Afterwards the first step is to go through the training, I collect a list of
     * unique words and maps for ham and spam by going through each word of each file. After I collect and
     * calculate, I being testing, this is done by using the probability map that was created in the training and going
     * through each test file. By using the given formulas, I calculate the final probability if a file is a spam or ham file
     * and send this on to the display. I also calculate accuracy and percision here by using the given formulas.
     *
     * @param dir
     * @return list
     */
    public static ObservableList<TestFile> getData(File dir){
        ObservableList<TestFile> list = FXCollections.observableArrayList();

        int hamTotal =0;
        int spamTotal=0;

        TreeMap<String,Integer> trainHamFreq = new TreeMap<String,Integer >();
        TreeMap<String,Integer> trainSpamFreq = new TreeMap<String,Integer >();
        Set<String> totalUniqueWords = new HashSet<String>();
        try {
            processFolder(dir);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        //File folder = new File("C:\\Users\\100588398\\Desktop\\school\\randy\\Assignment1\\src\\data\\train\\ham");
        for (int x = 0; x < trainHamList.size();x++) {
            File folder = trainHamList.get(x);
            File[] listOfFiles = folder.listFiles();

            for (int i = 0; i < listOfFiles.length; i++) {
                hamTotal++;
                try {
                    Set<String> uniqueWords = new HashSet<String>();


                    if (listOfFiles[i].isFile()) {
                        FileReader reader = new FileReader(listOfFiles[i]);
                        BufferedReader in = new BufferedReader(reader);

                        String line;
                        while ((line = in.readLine()) != null) {
                            if (line.trim().length() != 0) {
                                String[] dataFields = line.split(" ");
                                for (String word : dataFields) {
                                    totalUniqueWords.add(word.toLowerCase());
                                    uniqueWords.add(word.toLowerCase());
                                }

                            }
                        }
                    }
                    for (String s : uniqueWords) {
                        Integer count = trainHamFreq.get(s);
                        if (count != null) {
                            trainHamFreq.put(s, count + 1);
                        } else {
                            trainHamFreq.put(s, 1);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        /*
        Set set = trainHamFreq.entrySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry)iterator.next();
            System.out.print("key is: "+ mentry.getKey() + " & Value is: ");
            System.out.println(mentry.getValue());
        }
        */

        for (int x = 0; x < trainSpamList.size();x++) {
            File folder = trainSpamList.get(x);
            File[] listOfFiles = folder.listFiles();
            for (int i = 0; i < listOfFiles.length; i++) {
                spamTotal++;
                try {
                    Set<String> uniqueWords = new HashSet<String>();


                    if (listOfFiles[i].isFile()) {
                        FileReader reader = new FileReader(listOfFiles[i]);
                        BufferedReader in = new BufferedReader(reader);

                        String line;
                        while ((line = in.readLine()) != null) {
                            if (line.trim().length() != 0) {
                                String[] dataFields = line.split(" ");
                                for (String word : dataFields) {
                                    totalUniqueWords.add(word.toLowerCase());
                                    uniqueWords.add(word.toLowerCase());
                                }

                            }
                        }
                    }
                    for (String s : uniqueWords) {
                        Integer count = trainSpamFreq.get(s);
                        if (count != null) {
                            trainSpamFreq.put(s, count + 1);
                        } else {
                            trainSpamFreq.put(s, 1);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("It's done the file part");
        System.out.println(hamTotal);
        System.out.println(spamTotal);


        TreeMap<String,Double> hamProbability = new TreeMap<String,Double >();
        TreeMap<String,Double> spamProbability = new TreeMap<String,Double >();

        TreeMap<String,Double> fileSpamWiProbability = new TreeMap<String,Double >();

        for(String s : totalUniqueWords){
            Integer hamWord = trainHamFreq.get(s);
            double pHam = 0;
            if(hamWord != null) {
                pHam = (double)hamWord / hamTotal;
                //System.out.println(pHam);
            }

            Integer spamWord = trainSpamFreq.get(s);
            double pSpam = 0;
            if(spamWord != null) {

                pSpam = (double)spamWord / spamTotal;
                //System.out.println(pSpam);
            }

            hamProbability.put(s,pHam);
            spamProbability.put(s,pSpam);

            Double calculation = pSpam/(pSpam+pHam);
            //System.out.println(calculation);
            fileSpamWiProbability.put(s,calculation);

        }

        Set set = fileSpamWiProbability.entrySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry)iterator.next();
           // System.out.print("key is: "+ mentry.getKey() + " & Value is: ");
            //System.out.println(mentry.getValue());
        }

        TreeMap<String, Double> finalFileSpamProbability = new TreeMap<String, Double>();

        int numTruePositives = 0;
        int numTrueNegatives = 0;
        int numFalsePositives = 0;

        int testHamLength = 0;
        int testSpamLength = 0;
        //===================================== TESTING
        for (int x = 0; x < testHamList.size();x++) {
            File folder = testHamList.get(x);
            File[] listOfFiles = folder.listFiles();
            testHamLength += listOfFiles.length;



            for (int i = 0; i < listOfFiles.length; i++) {
                double sumProb = 0;
                try {

                    if (listOfFiles[i].isFile()) {
                        FileReader reader = new FileReader(listOfFiles[i]);
                        BufferedReader in = new BufferedReader(reader);

                        String line;
                        while ((line = in.readLine()) != null) {
                            if (line.trim().length() != 0) {
                                String[] dataFields = line.split(" ");
                                for (String word : dataFields) {
                                    Double spamProb = fileSpamWiProbability.get(word);
                                    if (spamProb != null) {
                                        if (spamProb != 0.0 && spamProb != 1.0) {
                                            sumProb += (Math.log(1 - spamProb) - Math.log(spamProb));
                                        }
                                    }
                                }

                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //System.out.println(sumProb);
                double calc = 1 / (1 + (Math.pow(Math.E, sumProb)));
                finalFileSpamProbability.put(listOfFiles[i].getName(), calc);
                String guessClass = "Ham";
                if (calc > 0.5) {
                    guessClass = "Spam";
                    numFalsePositives++;
                } else {
                    numTruePositives++;
                }

                list.add(new TestFile(listOfFiles[i].getName(), "Ham", guessClass, calc));
            }
        }
        for (int x = 0; x < testSpamList.size();x++) {
            File folder = testSpamList.get(x);
            File[] listOfFiles = folder.listFiles();
            testSpamLength += listOfFiles.length;


            for (int i = 0; i < listOfFiles.length; i++) {
                double sumProb = 0;
                try {

                    if (listOfFiles[i].isFile()) {
                        FileReader reader = new FileReader(listOfFiles[i]);
                        BufferedReader in = new BufferedReader(reader);

                        String line;
                        while ((line = in.readLine()) != null) {
                            if (line.trim().length() != 0) {
                                String[] dataFields = line.split(" ");
                                for (String word : dataFields) {
                                    Double spamProb = fileSpamWiProbability.get(word);
                                    if (spamProb != null) {
                                        if (spamProb != 0.0 && spamProb != 1.0) {
                                            sumProb += (Math.log(1 - spamProb) - Math.log(spamProb));
                                        }
                                    }
                                }

                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //System.out.println(sumProb);
                double calc = 1 / (1 + (Math.pow(Math.E, sumProb)));
                finalFileSpamProbability.put(listOfFiles[i].getName(), calc);
                String guessClass = "Ham";
                if (calc > 0.5) {
                    guessClass = "Spam";
                    numTrueNegatives++;
                    numFalsePositives++;

                }


                list.add(new TestFile(listOfFiles[i].getName(), "Spam", guessClass, calc));
            }
        }

        int totalFiles = testHamLength+testSpamLength;
        double a = (double)(numTruePositives+numTrueNegatives)/totalFiles;
        double p = (double)(numTrueNegatives)/(numFalsePositives);
        accur = a;
        perci = p;



        return list;
    }

}
