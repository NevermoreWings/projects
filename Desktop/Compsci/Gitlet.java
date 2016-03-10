import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardCopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;


public class Gitlet {
    // this idea has to be implemented as well, using different branches, assign
    //first one to master and then when calling

    public static void main(String[] args) {
        if (args[0].equals("init")) {
            init();
        } else if (args[0].equals("add")) {
            add(args[1]);
        } else if (args[0].equals("commit")) {
            if (args.length < 2) {
                System.out.println("Please enter a commit message");
            } else {
                String commtMsg = args[1];
                commit(commtMsg);
            }
        } else if (args[0].equals("rm")) {
            remove(args[1]);
        } else if (args[0].equals("log")) { //order does matter
            log();

        } else if (args[0].equals("global-log")) { //order doesn't matter
            globalLog();
        } else if (args[0].equals("find")) { //uses commit message to find the commit ID
            find(args[1]);
        } else if (args[0].equals("status")) { //prints out staged files, what branches you have, files to be removed
            status();
        } else if (args[0].equals("checkout")) { //checkout rread up on it

            Instance inst;
            inst = deserializeInstance();
            if (args.length == 2) {
                if (inst.getBranches().containsKey(args[1])) {
                    checkout1Branch(args[1]);
                } else {
                    checkout1File(args[1]);
                }
            } else {
                checkout2(args[1], args[2]);
            }

        } else if (args[0].equals("branch")) {
            branch(args[1]);
        } else if (args[0].equals("rm-branch")) {
            removeBranch(args[1]);
        } else if (args[0].equals("reset")) { //resets all the files to the given id.
            reset(args[1]);
        } else if (args[0].equals("merge")) { // squash branches together.
            merge(args[1]);

        }
        //long
        else if (args[0].equals("rebase")) { //makes a copy of the current one and applies it to the current
            rebase(args[1]);
        } else if (args[0].equals("i-rebase")) {
            rebase(args[1]);
        }
    }
    //helper code

    public static void reset(String s) {
        Instance inst = deserializeInstance();
        String comString = ".gitlet/commit" + s + ".ser";
        File f1 = new File(comString);
        if (f1.exists()) {
            System.out.println("Warning: The command you entered may alter the files in your working directory. Uncommitted changes may be lost. Are you sure you want to continue?(yes/no)");

            Scanner scan = new Scanner(System.in);
            if (scan.nextLine().equals("yes")) {
                Commit c = deserializeCommit(comString);
                String temp1 = "";
                String temp2 = "";
                HashMap<String, String> letrack = c.getTracked();
                for (String t : letrack.keySet()) {
                    temp1 = letrack.get(t);
                    // now i need to substring this to get rid of the .gitlet/commitID/...
                    temp2 = t;
                    try {
                        Path begin = Paths.get(temp1);
                        Path end = Paths.get(temp2);
                        Files.copy(begin, end, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException error) {

                    }
                }
                inst.updateHead(inst.getCurrentBranch(), Integer.parseInt(s));
                serializeInstance(inst);
            } else {
            }

        } else {
            System.out.println("No commit with that id exists.");
        }

    }
    public static void rebase(String s) {
        System.out.println("Warning: The command you entered may alter the files in your working directory. Uncommitted changes may be lost. Are you sure you want to continue?(yes/no)");
        Scanner scan = new Scanner(System.in);
        if (scan.nextLine().equals("yes")) {
            Instance inst;
            inst = deserializeInstance();
            if (inst.getBranches().containsKey(s)) {
                Commit current = deserializeCommit(".gitlet/commit" + inst.getBranches().get(inst.getCurrentBranch()) + ".ser");
                Commit given = deserializeCommit(".gitlet/commit" + inst.getBranches().get(s) + ".ser");
                Commit c = given;
                ArrayList<Integer> ahri = new ArrayList<Integer>();
                if (!inst.getCurrentBranch().equals(s)) {
                    int splitID = findSplitPoint(current, given);
                    if (inst.getBranches().get(inst.getCurrentBranch()).equals(splitID)) {
                        System.out.println("Already up-to-date");
                    } else {
                        while (c.getCommitNum() != splitID) {
                            ahri.add(c.getCommitNum());
                            c = deserializeCommit(".gitlet/commit" + c.getParentID() + ".ser");
                        }
                        for (int i = ahri.size() - 1; i >= 0; i--) {
                            Commit doh = deserializeCommit(".gitlet/commit" + ahri.get(i) + ".ser");
                            if (i == (ahri.size() - 1)) {
                                c = new Commit(inst.getBranches().get(inst.getCurrentBranch()), inst.getCounter(), doh.getCommitMsg());
                                c.setTracked(doh);
                                serializeCommit(c);
                                inst.augmentCounter();

                            } else {

                                c = new Commit(inst.getCounter() - 1, inst.getCounter(), doh.getCommitMsg());
                                if (i == 0) {
                                    inst.updateHead(s, inst.getCounter());
                                }
                                c.setTracked(doh);
                                serializeCommit(c);
                                inst.augmentCounter();
                            }
                        }
                        reset(s);
                        serializeInstance(inst);

                    }

                } else {
                    System.out.println("Cannot rebase a branch onto itself.");
                }
            } else {
                System.out.println("A branch with that name does not exist.");
            }


        } else {

        }
    }
    public static void irebase(String s) {
        System.out.println("Warning: The command you entered may alter the files in your working directory. Uncommitted changes may be lost. Are you sure you want to continue?(yes/no)");
        Scanner scan = new Scanner(System.in);
        if (scan.nextLine().equals("yes")) {
            Instance inst;
            inst = deserializeInstance();
            if (inst.getBranches().containsKey(s)) {
                Commit current = deserializeCommit(".gitlet/commit" + inst.getBranches().get(inst.getCurrentBranch()) + ".ser");
                Commit given = deserializeCommit(".gitlet/commit" + inst.getBranches().get(s) + ".ser");
                Commit c = given;
                ArrayList<Integer> ahri = new ArrayList<Integer>();
                if (!inst.getCurrentBranch().equals(s)) {
                    int splitID = findSplitPoint(current, given);
                    if (inst.getBranches().get(inst.getCurrentBranch()).equals(splitID)) {
                        System.out.println("Already up-to-date");
                    } else {
                        while (c.getCommitNum() != splitID) {
                            ahri.add(c.getCommitNum());
                            c = deserializeCommit(".gitlet/commit" + c.getParentID() + ".ser");
                        }
                        for (int i = ahri.size() - 1; i >= 0; i--) {
                            Commit doh = deserializeCommit(".gitlet/commit" + ahri.get(i) + ".ser");
                            if (i == (ahri.size() - 1)) {
                                String command = "";
                                while (!command.equals("c") && !command.equals("m")) {
                                    System.out.println("Would you like to (c)ontinue, (s)kip this commit, or change this commit's (m)essage?");
                                    command = scan.nextLine();
                                }
                                if (command.equals("m")) {
                                    System.out.println("Please enter a new message for this commit.");
                                    c = new Commit(inst.getBranches().get(inst.getCurrentBranch()), inst.getCounter(), scan.nextLine());
                                } else {
                                    c = new Commit(inst.getBranches().get(inst.getCurrentBranch()), inst.getCounter(), doh.getCommitMsg());
                                }
                                c.setTracked(doh);
                                serializeCommit(c);
                                inst.augmentCounter();

                            } else if (i == 0) {
                                String command = "";
                                while (!command.equals("c") && !command.equals("m")) {
                                    System.out.println("Would you like to (c)ontinue, (s)kip this commit, or change this commit's (m)essage?");
                                    command = scan.nextLine();
                                }
                                if (command.equals("c")) {
                                    c = new Commit(inst.getCounter() - 1, inst.getCounter(), doh.getCommitMsg());
                                } else {
                                    System.out.println("Please enter a new message for this commit.");
                                    c = new Commit(inst.getBranches().get(inst.getCurrentBranch()), inst.getCounter(), scan.nextLine());
                                }
                                c.setTracked(doh);
                                serializeCommit(c);
                                inst.augmentCounter();

                            } else {
                                String command = "";
                                while (!command.equals("c") && !command.equals("s") && !command.equals("m")) {
                                    System.out.println("Would you like to (c)ontinue, (s)kip this commit, or change this commit's (m)essage?");
                                    command = scan.nextLine();
                                }
                                if (command.equals("s")) {
                                } else if ( command.equals("c")) {
                                    c = new Commit(inst.getCounter() - 1, inst.getCounter(), doh.getCommitMsg());
                                    c.setTracked(doh);
                                    serializeCommit(c);
                                    inst.augmentCounter();
                                } else {
                                    System.out.println("Please enter a new message for this commit.");
                                    c = new Commit(inst.getBranches().get(inst.getCurrentBranch()), inst.getCounter(), scan.nextLine());
                                    c.setTracked(doh);
                                    serializeCommit(c);
                                    inst.augmentCounter();
                                }
                            }
                        }
                        checkout1Branch(s);
                        serializeInstance(inst);
                    }
                } else {
                    System.out.println("Cannot rebase a branch onto itself.");
                }
            } else {
                System.out.println("A branch with that name does not exist.");
            }
        } else {

        }
    }
    //checkout in strange fashion merge two in working directory.
    public static void merge(String s) {
        System.out.println("Warning: The command you entered may alter the files in your working directory. Uncommitted changes may be lost. Are you sure you want to continue?(yes/no)");

        Scanner scan = new Scanner(System.in);
        if (scan.nextLine().equals("yes")) {
            Instance inst = deserializeInstance();
            if (inst.getBranches().containsKey(s)) {
                if (s.equals(inst.getCurrentBranch())) {
                    System.out.println("Cannot merge with a branch with itself.");
                } else {
                    Commit current = deserializeCommit(".gitlet/commit" + inst.getBranches().get(inst.getCurrentBranch()) + ".ser");
                    Commit given = deserializeCommit(".gitlet/commit" + inst.getBranches().get(s) + ".ser");
                    int splitpoint = findSplitPoint(current, given);
                    Commit origin = deserializeCommit(".gitlet/commit" + splitpoint + ".ser");
                    HashSet<String> changedInCurrent = new HashSet<String>();
                    HashSet<String> changedInGiven = new HashSet<String>();
                    for (String key : origin.getTracked().keySet()) {
                        try {
                        	if(current.getTracked().get(key)==null)
                        	{}
                        	else
                        	{
	                            if (compareFilesbyByte(origin.getTracked().get(key), current.getTracked().get(key)))
	                                changedInCurrent.add(key);
                        	}
                        	if(given.getTracked().get(key)==null)
                        	{}
                        	else
                        	{
                            	if (compareFilesbyByte(origin.getTracked().get(key), given.getTracked().get(key) ))
                                	changedInGiven.add(key);
                        	}
                        } catch (IOException e)
                        {}
                    }
                    for (String files : changedInGiven ) {
                        if (changedInCurrent.contains(files)) {
                            //create conflicted files
                            try {
                                File whatIwant = new File(files + ".conflicted");
                                whatIwant.createNewFile();
                            } catch (IOException error) {
                            }
                        } else {
                            //copy files over
                            try {
                                Path begin = Paths.get(given.getTracked().get(files));
                                Path end = Paths.get(files);
                                Files.copy(begin, end, StandardCopyOption.REPLACE_EXISTING);
                            } catch (IOException error) {

                            }
                        }
                    }
                }
                serializeInstance(inst);
            } else {
                System.out.println("A branch with that name does not exist.");
            }
        } else {
        }

    }
    public static void removeBranch(String s) {

        Instance inst;
        inst = deserializeInstance();

        if (s.equals(inst.getCurrentBranch())) {
            System.out.println("Cannot remove the current branch.");
        } else {
            if (!inst.getBranches().containsKey(s)) {
                System.out.println("A branch with that name does not exist.");
            } else {
                inst.deletePointerBranch(s);
            }
        }
        serializeInstance(inst);
    }

    public static int findSplitPoint(Commit c, Commit d) {
        HashSet<Integer> pathNum = new HashSet<Integer>();
        Commit commit1 = c;
        Commit commit2 = d;
        String commitName = "";
        while (commit1.getParentID() != -1) {
            System.out.println("first while loop " + commit1.getCommitNum() );
            pathNum.add(commit1.getCommitNum());
            commitName = ".gitlet/commit" + commit1.getParentID() + ".ser";
            commit1 = deserializeCommit(commitName);
        }
        while (commit2.getParentID() != -1) {
            System.out.println("2nd while loop" + commit2.getCommitNum());
            if (pathNum.contains(commit2.getCommitNum()))
                return commit2.getCommitNum();
            commitName = ".gitlet/commit" + commit2.getParentID() + ".ser";
            commit2 = deserializeCommit(commitName);
        }
        return -1;
    }
    public static void checkout1File(String filename) {

        Instance inst;
        inst = deserializeInstance();

        System.out.println("Warning: The command you entered may alter the files in your working directory. Uncommitted changes may be lost. Are you sure you want to continue?(yes/no)");

        Scanner scan = new Scanner(System.in);
        if (scan.nextLine().equals("yes")) {
            String branch = inst.getCurrentBranch();
            Integer commitNum = inst.getBranches().get(branch);
            String commitName = ".gitlet/commit" + commitNum;
            Commit c;
            c = deserializeCommit(commitName + ".ser");
            for (String s : c.getTracked().keySet()) {

                System.out.println(s);
            }
            if (c.isTracked(filename)) {
                String fileInTheCommitFolder = c.getTracked().get(filename);
                System.out.println(fileInTheCommitFolder);
                String fileInTheWorkingDirectory = filename;
                try {
                    Path begin = Paths.get(fileInTheCommitFolder);
                    Path end = Paths.get(fileInTheWorkingDirectory);
                    Files.copy(begin, end, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException error)
                {}
            } else {
                System.out.println("File does not exist in the most recent commit, or no such branch exists.");
            }
        } else {

        }
    }

    public static void checkout2(String commitid, String filename) {

        System.out.println("Warning: The command you entered may alter the files in your working directory. Uncommitted changes may be lost. Are you sure you want to continue?(yes/no)");
        Scanner scan = new Scanner(System.in);
        if (scan.nextLine().equals("yes")) {
            File foofoo = new File(".gitlet/commit" + commitid + ".ser");
            if (foofoo.exists()) {
                Commit c = deserializeCommit(".gitlet/commit" + commitid + ".ser");

                if (c.isTracked(filename)) {
                    try {
                        Path begin = Paths.get(c.getTracked().get(filename));
                        Path end = Paths.get(filename);
                        Files.copy(begin, end, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException error) {

                    }
                } else {
                    System.out.println("File does not exist in that commit.");
                }
            } else {
                System.out.println("No commit with that id exists.");
            }
        } else {

        }

        //make file same as the samestate it is at at the commit id given
    }

    public static void checkout1Branch(String branchname) {
        //make working directory the same as the branch that is given and set current branch as this branch
        System.out.println("Warning: The command you entered may alter the files in your working directory. Uncommitted changes may be lost. Are you sure you want to continue?(yes/no)");
        Scanner scan = new Scanner(System.in);
        if (scan.nextLine().equals("yes")) {
            Instance inst;
            inst = deserializeInstance();
            if (inst.getBranches().containsKey(branchname)) {
                if (inst.getCurrentBranch().equals(branchname)) {
                    System.out.println("No need to checkout the current branch.");
                } else {
                    Commit c = deserializeCommit(".gitlet/commit" + inst.getBranches().get(branchname) + ".ser");
                    String temp1 = "";
                    String temp2 = "";
                    HashMap<String, String> letrack = c.getTracked();
                    for (String s : letrack.keySet()) {
                        temp1 = letrack.get(s);
                        // now i need to substring this to get rid of the .gitlet/commitID/...
                        temp2 = s;
                        try {
                            Path begin = Paths.get(temp1);
                            Path end = Paths.get(temp2);
                            Files.copy(begin, end, StandardCopyOption.REPLACE_EXISTING);
                        } catch (IOException error) {

                        }
                    }
                    inst.changeCurrentBranch(branchname);
                    serializeInstance(inst);
                }
            } else {
                System.out.println("File does not exist in the most recent commit, or no such branch exists.");
            }

        } else {

        }
    }
    //init
    public static void init() {
        File file = new File(".gitlet/");

        if (!file.exists()) {
            Commit currentCommit;
            Instance linstance;
            linstance = new Instance();
            currentCommit = new Commit();
            file.mkdir();
            File file1 = new File(".gitlet/commit0");
            file1.mkdir();
            serializeCommit(currentCommit);//committed the old one
            serializeInstance(linstance);
        } else {
            System.out.println("A gitlet version control system already exists in the current directory.");
        }
    }
    //add
    public static void add(String s) {
        boolean check = new File(s).exists();
        File f2 = new File(s);
        if (!check) {
            System.out.println("File does not exist.");
        } else {
            Instance inst;
            inst = deserializeInstance();
            String branch = inst.getCurrentBranch();
            Integer commitNum = inst.getBranches().get(branch); //current location

            // c is the "not yet created "
            Commit c;
            String commitName = ".gitlet/commit" + commitNum + ".ser";
            c = deserializeCommit(commitName);
            if (inst.getRemovedFiles().contains(s)) {
                inst.removeFromRemove(s);
            }
            if (c.isTracked(s)) {
                File f1 = new File(".gitlet/commit" + commitNum + "/" + s);
                check = f1.exists();
                if (check) {
                    try {
                        if (compareFilesbyByte(".gitlet/commit" + commitNum + "/" + s, s)) {
                            System.out.println("File has not been modified since the last commit.");
                            return;
                        }

                    } catch (IOException error) {

                    }

                }
            } else {
                inst.addToStaged(s);
            }
            serializeCommit(c);
            serializeInstance(inst);

        }
    }
    public static void commit(String inputmsg) {
        Instance inst;
        inst = deserializeInstance();
        String branch = inst.getCurrentBranch();
        Integer commitNum = inst.getBranches().get(branch);
        String commitName = ".gitlet/commit" + commitNum + ".ser";
        Commit c;
        c = deserializeCommit(commitName);
        Commit d = new Commit(commitNum, inst.getCounter(), inputmsg);
        File file1 = new File(".gitlet/commit" + inst.getCounter());
        file1.mkdir();
        // time to set tracked fiels yay.....
        HashMap<String, String> letracked = c.getTracked();
        HashSet<String> lestaged = inst.getStagedFiles();
        HashSet<String> leremoved = inst.getRemovedFiles();
        for (String s : letracked.keySet()) {
            if (!lestaged.contains(s) && !leremoved.contains(s)) {
                d.add(s, letracked.get(s));
            }
        }
        for (String s : lestaged) {
            int i = s.lastIndexOf("/");
            String e = ".gitlet/commit" + inst.getCounter() + "/";
            if (i != -1) {
                String a = s.substring(0, i);
                Path p1 = Paths.get(e + a); // creates original folders
                Path p2 = Paths.get(s); //original file
                Path p3 = Paths.get(e + s); //original file in new commit folder
                try {
                    Files.createDirectories(p1);
                    Files.copy(p2, p3, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException error) {
                    System.out.println("YOU DONE GOOFED");
                    error.printStackTrace();
                }
            } else {
                Path p1 = Paths.get(s);
                Path p2 = Paths.get(e + s);
                try {
                    File whatIwant = new File(e + s);
                    whatIwant.createNewFile();
                    Files.copy(p1, p2, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException error) {
                }
            }
            d.add(s, e + s);
        }
        inst.addToMToC(inputmsg, inst.getCounter());
        inst.clearStaged();
        inst.clearRemove();
        inst.updateHead(branch, inst.getCounter());
        inst.augmentCounter();
        serializeInstance(inst);
        serializeCommit(d);
    }
    public static void remove(String s) {
        Instance inst;
        inst = deserializeInstance();
        String branch = inst.getCurrentBranch();
        Integer commitNum = inst.getBranches().get(branch);
        String commitName = ".gitlet/commit" + commitNum + ".ser";
        Commit c;
        c = deserializeCommit(commitName);


        if (!inst.getStagedFiles().contains(s) && !c.isTracked(s)) {
            System.out.println("No reason to remove the file.");
        } else {
            if (inst.getStagedFiles().contains(s)) {
                inst.removeFromStaged(s);
            } else {
                inst.addtoRemove(s);
            }
        }
        serializeInstance(inst);
    }
    public static void log() {
        Instance inst;
        inst = deserializeInstance();
        String branch = inst.getCurrentBranch();
        Integer commitNum = inst.getBranches().get(branch);
        String commitName = ".gitlet/commit" + commitNum + ".ser";
        Commit c;
        c = deserializeCommit(commitName);
        while (c.getParentID() != -1) {
            System.out.println("====");
            System.out.println("Commit " + c.getCommitNum() + ".");
            System.out.println(c.getDate());
            System.out.println(c.getCommitMsg());
            System.out.println();
            commitName = ".gitlet/commit" + c.getParentID() + ".ser";

            c = deserializeCommit(commitName);
        }
        System.out.println("====");
        System.out.println("Commit " + c.getCommitNum() + ".");
        System.out.println(c.getDate());
        System.out.println(c.getCommitMsg());
        System.out.println();
    }
    public static void globalLog() {
        Instance inst;
        inst = deserializeInstance();
        String s;
        Commit c;

        for (int i = 0; i < inst.getCounter(); i++) {
            s = ".gitlet/commit" + i + ".ser";
            c = deserializeCommit(s);
            System.out.println("====");
            System.out.println("Commit " + c.getCommitNum() + ".");
            System.out.println(c.getDate());
            System.out.println(c.getCommitMsg());
            System.out.println();

        }
    }
    public static void find(String s) {
        Instance inst;
        inst = deserializeInstance();
        if (inst.getCommitMap().get(s) == null) {
            System.out.println("Found no commit with that message.");
        } else {
            for (Integer x : inst.getCommitMap().get(s)) {
                System.out.println(x);
            }
        }
    }
    public static void status() {
        Instance inst;
        inst = deserializeInstance();

        System.out.println("=== Branches ===");
        for (String s : inst.getBranches().keySet()) {
            if (s.equals(inst.getCurrentBranch()))
                System.out.print("*");
            System.out.println(s);
        }
        System.out.println();
        System.out.println("=== Staged Files ===");
        for (String s : inst.getStagedFiles()) {
            System.out.println(s);
        }
        System.out.println();
        System.out.println("=== Files Marked for Removal ===");
        for (String s : inst.getRemovedFiles()) {
            System.out.println(s);
        }
    }
    public static void branch(String s) {
        Instance inst;
        inst = deserializeInstance();
        if (inst.getBranches().containsKey(s)) {
            System.out.println("A branch with that name already exists.");
        } else {
            inst.addNewBranch(s);
        }
        serializeInstance(inst);
    }
    ////Serializing a Commit
    public static void serializeCommit(Commit c) {
        String s = "commit" + c.getCommitNum();
        try {

            FileOutputStream fos = new FileOutputStream(".gitlet/" + s + ".ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(c);
            oos.close();
            fos.close();

        } catch (IOException e) {

        }
    }
    //deserializing a Commit
    public static Commit deserializeCommit(String s) {
        Commit a = new Commit();
        try {
            FileInputStream fileIn = new FileInputStream(s);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            a = (Commit) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException e) {

        } catch (ClassNotFoundException f) {

        }
        return a;
    }
    ////Serializing the saved instance variables.
    public static void serializeInstance(Instance inst) {

        try {
            FileOutputStream fos = new FileOutputStream(".gitlet/instance.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(inst);
            oos.close();
            fos.close();

        } catch (IOException e) {

        }

    }
    public static Instance deserializeInstance() {
        Instance a = new Instance();
        try {
            FileInputStream fileIn = new FileInputStream(".gitlet/instance.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            a = (Instance) in.readObject();
            in.close();
            fileIn.close();

        } catch (IOException e) {

        } catch (ClassNotFoundException e) {

        }
        return a;
    }





    // comparing files
    //this code was found online (note to self: cite it) http://javaonlineguide.net/2014/10/compare-two-files-in-java-example-code.html
    public static boolean compareFilesbyByte(String file1, String file2) throws IOException {
        File f1 = new File(file1);
        File f2 = new File(file2);
        FileInputStream fis1 = new FileInputStream(f1);
        FileInputStream fis2 = new FileInputStream(f2);
        if (f1.length() == f2.length()) {
            int n = 0;
            byte[] b1;
            byte[] b2;
            while ((n = fis1.available()) > 0) {
                if (n > 80)
                    n = 80;
                b1 = new byte[n];
                b2 = new byte[n];
                int res1 = fis1.read(b1);
                int res2 = fis2.read(b2);
                if (Arrays.equals(b1, b2) == false) {
                    return false;
                }

            }
        } else {
            return false;
        }
        return true;
    }


}