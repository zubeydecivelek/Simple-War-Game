import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;

public class Commands {
    private final String initialsFileName;
    private final String commandsFileName;
    private final String outputFileName;
    private int boardSize;
    private String[][] board;
    private final ArrayList<Characters> charactersArrayList = new ArrayList<>();
    public static int[][] possibleMove = {{0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}, {-1, -1}, {-1, 0}, {-1, 1}};


    public Commands(String initialsFileName, String commandsFileName, String outputFileName) {
        this.initialsFileName = initialsFileName;
        this.commandsFileName = commandsFileName;
        this.outputFileName = outputFileName;
    }

    public void readInitials() throws IOException {
        //Creating an empty output. If output file isn't empty before program runs, it cleans the output file.
        BufferedWriter emptyOutput = new BufferedWriter(new FileWriter(outputFileName));
        emptyOutput.close();

        //Reading the initials file.
        ArrayList<String> initialsArraylist = new ArrayList<>();
        BufferedReader initialsFile = new BufferedReader(new FileReader(initialsFileName));
        String line;
        while ((line=initialsFile.readLine())!=null){
            initialsArraylist.add(line);
        }

        boardSize = Integer.parseInt(initialsArraylist.get(1).split("x")[0]);

        //Saving the characters as class objects.
        for (String initial:initialsArraylist){
            String[] list =initial.split(" ");
            if (list[0].equals("ELF")) {
                Elf elf = new Elf(list[1], Integer.parseInt(list[3]), Integer.parseInt(list[2]));
                charactersArrayList.add(elf);
            }
            else if (list[0].equals("DWARF")){
                Dwarf dwarf = new Dwarf(list[1],Integer.parseInt(list[3]),Integer.parseInt(list[2]));
                charactersArrayList.add(dwarf);
            }
            else if (list[0].equals("HUMAN")){
                Human human = new Human(list[1],Integer.parseInt(list[3]),Integer.parseInt(list[2]));
                charactersArrayList.add(human);
            }
            else if (list[0].equals("GOBLIN")){
                Goblin goblin = new Goblin(list[1],Integer.parseInt(list[3]),Integer.parseInt(list[2]));
                charactersArrayList.add(goblin);
            }
            else if (list[0].equals("TROLL")){
                Troll troll = new Troll(list[1],Integer.parseInt(list[3]),Integer.parseInt(list[2]));
                charactersArrayList.add(troll);
            }
            else if (list[0].equals("ORK")){
                Ork ork = new Ork(list[1],Integer.parseInt(list[3]),Integer.parseInt(list[2]));
                charactersArrayList.add(ork);
            }
        }

        //CREATING FIRST BOARD AND PRINT IT IN OUTPUT FILE.
        board = makeBoard(charactersArrayList,boardSize);
        outputPrint(charactersArrayList,board,true,false,false);
    }


    public void doCommands() throws IOException {
        //Reading the commands file.
        ArrayList<String> commands = new ArrayList<>();
        BufferedReader commandFile = new BufferedReader(new FileReader(commandsFileName));
        String line1;
        while ((line1=commandFile.readLine())!=null){
            commands.add(line1);
        }

        //Applying commands.
        for (String lineCommand:commands){
            String[] command = lineCommand.substring(3).split(";");
            ArrayList<ArrayList<Integer>> moves = new ArrayList<>();
            for (int i =0;i<command.length/2;i++){
                ArrayList<Integer> add = new ArrayList<>();
                add.add(Integer.parseInt(command[2*i+1]));
                add.add(Integer.parseInt(command[2*i]));
                moves.add(add);
            }

            boolean bool = false;   //This boolean value for printing the board.
            boolean bool1 = false;  //This boolean value for Move Count Check.
            boolean bool2 = false;  //This boolean value for Boundary Check.

            if (lineCommand.charAt(0) == 'D'){
                Dwarf dwarf = (Dwarf) findTheCharacter(lineCommand.substring(0,2));

                if (dwarf.getMaxMove()!=moves.size()){      //Move Count Check.
                    bool1 = true;
                }
                if (dwarf.getMaxMove()==moves.size()){
                    for (ArrayList<Integer> move :moves){
                        board = makeBoard(charactersArrayList, boardSize);
                        ArrayList<Integer> control = new ArrayList<>();
                        control.add(dwarf.getI() + move.get(0));
                        control.add(dwarf.getJ() + move.get(1));
                        if (possibleMoves(dwarf.getI(), dwarf.getJ(), board,dwarf).contains(control)){
                            dwarf.Move(move.get(0), move.get(1));
                            bool = true;

                            if ("OTG".contains(board[control.get(0)][control.get(1)].substring(0,1))){  //fight to death
                                Zorde zorde = (Zorde) findTheCharacter(board[control.get(0)][control.get(1)]);
                                dwarf.FightToDeath(zorde);
                                break;
                            }
                            else {
                                dwarf.AttackEnemy(charactersArrayList,board);
                            }
                        }
                        else if(!controlAlly(dwarf.getI(), dwarf.getJ(), board,dwarf).contains(control)){   //Boundary Check.
                            bool2 = true;
                            break;
                        }
                    }
                }
            }
            else if (lineCommand.charAt(0) == 'T'){
                Troll troll = (Troll) findTheCharacter(lineCommand.substring(0, 2));

                if (troll.getMaxMove()!=moves.size()){  //Move Count Check.
                    bool1 = true;
                }
                if (troll.getMaxMove()==moves.size()){
                    for (ArrayList<Integer> move :moves){
                        ArrayList<Integer> control = new ArrayList<>();
                        control.add(troll.getI() + move.get(0));
                        control.add(troll.getJ() + move.get(1));
                        if (possibleMoves(troll.getI(), troll.getJ(), board,troll).contains(control)){
                            troll.Move(move.get(0), move.get(1));
                            bool = true;

                            if ("HED".contains(board[control.get(0)][control.get(1)].substring(0,1))){  //fight to death
                                Calliance calliance = (Calliance) findTheCharacter(board[control.get(0)][control.get(1)]);
                                troll.FightToDeath(calliance);
                                break;
                            }
                            else {
                                troll.AttackEnemy(charactersArrayList,board);
                            }
                        }
                        else if (!controlAlly(troll.getI(), troll.getJ(), board,troll).contains(control)){  //Boundary Check.
                            bool2 = true;
                            break;
                        }
                    }
                }
            }
            else if (lineCommand.charAt(0) == 'G'){
                Goblin goblin = (Goblin) findTheCharacter(lineCommand.substring(0, 2));

                if (goblin.getMaxMove()!=moves.size()){
                    bool1=true;
                }
                if (goblin.getMaxMove()==moves.size()){
                    for (ArrayList<Integer> move :moves){
                        board = makeBoard(charactersArrayList, boardSize);
                        ArrayList<Integer> control = new ArrayList<>();
                        control.add(goblin.getI() + move.get(0));
                        control.add(goblin.getJ() + move.get(1));
                        if (possibleMoves(goblin.getI(), goblin.getJ(), board,goblin).contains(control)){
                            goblin.Move(move.get(0), move.get(1));
                            bool=true;

                            if ("HED".contains(board[control.get(0)][control.get(1)].substring(0,1))){ //fight to death
                                Calliance calliance = (Calliance) findTheCharacter(board[control.get(0)][control.get(1)]);
                                goblin.FightToDeath(calliance);
                                break;
                            }
                            else {
                                goblin.AttackEnemy(charactersArrayList,board);
                            }
                        }
                        else if (!controlAlly(goblin.getI(), goblin.getJ(), board,goblin).contains(control)) {
                            bool2=true;
                            break;
                        }
                    }
                }
            }
            else if (lineCommand.charAt(0) == 'H'){
                Human human = (Human) findTheCharacter(lineCommand.substring(0, 2));

                if (human.getMaxMove()!=moves.size()){
                    bool1=true;
                }
                if (human.getMaxMove()==moves.size()){
                    int count = 0;
                    for (ArrayList<Integer> move :moves){
                        board = makeBoard(charactersArrayList, boardSize);
                        ArrayList<Integer> control = new ArrayList<>();
                        control.add(human.getI() + move.get(0));
                        control.add(human.getJ() + move.get(1));
                        if (possibleMoves(human.getI(), human.getJ(), board,human).contains(control)){
                            human.Move(move.get(0), move.get(1));
                            bool=true;
                            board = makeBoard(charactersArrayList, boardSize);
                            count++;

                            if ("OTG".contains(board[control.get(0)][control.get(1)].substring(0,1))){ //fight to death
                                Zorde zorde = (Zorde) findTheCharacter(board[control.get(0)][control.get(1)]);
                                human.FightToDeath(zorde);
                                break;
                            }
                            else if (count == moves.size()){
                                human.AttackEnemy(charactersArrayList,board);
                            }
                        }
                        else if (!controlAlly(human.getI(), human.getJ(), board,human).contains(control)){
                            bool2=true;
                            break;
                        }
                    }
                }
            }
            else if (lineCommand.charAt(0) == 'O'){
                Ork ork = (Ork) findTheCharacter(lineCommand.substring(0, 2));

                if (ork.getMaxMove()!=moves.size()){
                    bool1 = true;
                }
                if (ork.getMaxMove()==moves.size()){
                    for (ArrayList<Integer> move :moves){
                        ArrayList<Integer> control = new ArrayList<>();
                        control.add(ork.getI() + move.get(0));
                        control.add(ork.getJ() + move.get(1));
                        if (possibleMoves(ork.getI(), ork.getJ(), board,ork).contains(control)){
                            ork.heal();
                            for (ArrayList<Integer> healIndex: ork.HealList(board)){
                                Zorde healZorde = (Zorde) findTheCharacter(board[healIndex.get(0)][healIndex.get(1)]);
                                healZorde.heal();
                            }
                            ork.Move(move.get(0), move.get(1));
                            bool=true;
                            if ("HED".contains(board[control.get(0)][control.get(1)].substring(0,1))){ //fight to death
                                Calliance calliance = (Calliance) findTheCharacter(board[control.get(0)][control.get(1)]);
                                ork.FightToDeath(calliance);
                                break;
                            }
                            else {
                                ork.AttackEnemy(charactersArrayList,board);
                            }
                        }
                        else if (controlAlly(ork.getI(), ork.getJ(), board,ork).contains(control)) {
                            ork.heal();
                            for (ArrayList<Integer> healIndex : ork.HealList(board)) {
                                Zorde healZorde = (Zorde) findTheCharacter(board[healIndex.get(0)][healIndex.get(1)]);
                                healZorde.heal();
                            }
                        }
                        else {
                            bool2=true;
                            break;
                        }
                    }
                }
            }
            else if (lineCommand.charAt(0) == 'E'){
                Elf elf = (Elf) findTheCharacter(lineCommand.substring(0, 2));
                if (elf.getMaxMove()!=moves.size()){
                    bool1=true;
                }
                if (elf.getMaxMove()==moves.size()){
                    int count = 0; // if step is the last step do the ranged attack
                    for (ArrayList<Integer> move :moves){
                        board = makeBoard(charactersArrayList, boardSize);
                        ArrayList<Integer> control = new ArrayList<>();
                        control.add(elf.getI() + move.get(0));
                        control.add(elf.getJ() + move.get(1));
                        if (possibleMoves(elf.getI(), elf.getJ(), board,elf).contains(control)){
                            elf.Move(move.get(0), move.get(1));
                            bool=true;
                            count++;
                            if ("OTG".contains(board[control.get(0)][control.get(1)].substring(0,1))){ //fight to death
                                Zorde zorde = (Zorde) findTheCharacter(board[control.get(0)][control.get(1)]);
                                elf.FightToDeath(zorde);
                                break;
                            }
                            else if (count== moves.size()){ //ranged attack
                                elf.RangedAttack(board,charactersArrayList);
                            }
                            else {
                                elf.AttackEnemy(charactersArrayList,board);
                            }
                        }
                        else if (controlAlly(elf.getI(), elf.getJ(), board,elf).contains(control)){
                            bool2=true;
                            break;
                        }
                    }
                }
            }
            board = makeBoard(charactersArrayList, boardSize);
            outputPrint(charactersArrayList,board,bool,bool1,bool2);
        }
    }

    public void outputPrint(ArrayList<Characters> charactersArrayList, String[][] board, boolean bool, boolean bool1, boolean bool2) throws IOException {
        BufferedWriter output = new BufferedWriter(new FileWriter(outputFileName,true));
        boolean gameOverZorde = false;
        boolean gameOverCalliance = false;
        if (bool || (!bool1 && !bool2)) {
            String star = "*";
            int n=board[0].length * 2 + 2;
            String repeated = new String(new char[n]).replace("\0", star);
            output.write(repeated + "\n");
            for (String[] strings : board) {
                for (int j = 0; j < board[0].length; j++) {
                    if (j == 0) {
                        output.write("*");
                    }
                    output.write(strings[j]);
                    if (j == board[0].length - 1) {
                        output.write("*");
                    }
                }
                output.write("\n");
            }
            output.write(repeated + "\n\n");

            charactersArrayList.removeIf(character -> character.getHP() <= 0);
            charactersArrayList.sort(Comparator.comparing(Characters::getName));

            for (Characters character : charactersArrayList) {
                output.write(character.getName() + "\t" + character.getHP() + "\t(" + character.getConstantHP() + ")\n");
                if (character instanceof Zorde){
                    gameOverZorde = true;
                }
                else{
                    gameOverCalliance = true;
                }
            }
            output.write("\n");

            if (!gameOverZorde){
                output.write("\nGame Finished\nCalliance Wins");
                output.close();
                System.exit(0);
            }
            else if (!gameOverCalliance){
                output.write("\nGame Finished\nZorde Wins");
                output.close();
                System.exit(0);
            }


        }
        if (bool1){
            output.write("Error : Move sequence contains wrong number of move steps. Input line ignored.\n\n");
        }
        if (bool2){
            output.write("Error : Game board boundaries are exceeded. Input line ignored.\n\n");
        }
        output.close();
    }

    public String[][] makeBoard(ArrayList<Characters> charactersArrayList, int boardSize){
        String[][] board = new String[boardSize][boardSize];
        charactersArrayList.removeIf(zorde -> zorde.getHP() <= 0);
        for (Characters character:charactersArrayList){
            board[character.getI()][character.getJ()]=character.getName();
        }
        for (int i =0;i<board.length;i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == null) {
                    board[i][j] = "  ";
                }
            }
        }
        return board;
    }
    public ArrayList<ArrayList<Integer>> possibleMoves(int nowI, int nowJ,String[][] board, Characters character){
        ArrayList<ArrayList<Integer>> returnList = new ArrayList<>();
        for (int[] row :possibleMove){
            int newI = nowI + row[0];
            int newJ = nowJ + row[1];
            if (0<=newI && newI< board.length && 0<=newJ && newJ< board.length){
                if (character instanceof Calliance && !("HED".contains(board[newI][newJ].substring(0,1)))){
                    ArrayList<Integer> add = new ArrayList<>();
                    add.add(newI);
                    add.add(newJ);
                    returnList.add(add);
                }
                else if (character instanceof Zorde && !("OTG".contains(board[newI][newJ].substring(0,1)))){
                    ArrayList<Integer> add = new ArrayList<>();
                    add.add(newI);
                    add.add(newJ);
                    returnList.add(add);
                }
            }
        }
        return returnList;
    }

    public ArrayList<ArrayList<Integer>> controlAlly (int nowI, int nowJ,String[][] board, Characters character) {
        ArrayList<ArrayList<Integer>> returnList = new ArrayList<>();
        for (int[] row : possibleMove) {
            int newI = nowI + row[0];
            int newJ = nowJ + row[1];
            if (0 <= newI && newI < board.length && 0 <= newJ && newJ < board.length) {
                if (character instanceof Calliance && "HED".contains(board[newI][newJ].substring(0, 1))) {
                    ArrayList<Integer> add = new ArrayList<>();
                    add.add(newI);
                    add.add(newJ);
                    returnList.add(add);
                } else if (character instanceof Zorde && "OTG".contains(board[newI][newJ].substring(0, 1))) {
                    ArrayList<Integer> add = new ArrayList<>();
                    add.add(newI);
                    add.add(newJ);
                    returnList.add(add);
                }
            }
        }
        return returnList;
    }

    public Characters findTheCharacter(String commandFindName){
        for (Characters characters:charactersArrayList){
            if (characters.getName().equals(commandFindName)){
                if (characters instanceof Zorde){
                    return (Zorde) characters;
                }
                return (Calliance) characters;
            }
        }
        return null;
    }
}
