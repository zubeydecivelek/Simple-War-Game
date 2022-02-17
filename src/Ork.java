import java.util.ArrayList;

public class Ork extends Zorde{
    private int HP = 200;
    private final int AP = Constants.orkAP;
    private final int constantHP = 200;

    public Ork(String name, int i, int j) {
        super(name, i, j);
    }

    @Override
    public void heal() {
        HP += Constants.orkHealPoints;
        if (HP>constantHP){
            HP = constantHP;
        }
    }

    @Override
    public void AttackEnemy(ArrayList<Characters> charactersArrayList, String[][] board) {
        for (ArrayList<Integer> list : controlEnemy(board)){
            for (Characters characters :charactersArrayList){
                if (board[list.get(0)][list.get(1)].equals(characters.getName())){
                    characters.Damage(AP);
                    break;
                }
            }
        }
    }


    public ArrayList<ArrayList<Integer>> HealList(String[][] board){
        ArrayList<ArrayList<Integer>> returnList = new ArrayList<>();
        for (int[] row : Commands.possibleMove) {
            int newI = getI() + row[0];
            int newJ = getJ() + row[1];
            if (0 <= newI && newI < board.length && 0 <= newJ && newJ < board.length && "TG".contains(board[newI][newJ].substring(0, 1))) {
                ArrayList<Integer> add = new ArrayList<>();
                add.add(newI);
                add.add(newJ);
                returnList.add(add);
            }
        }
        return returnList;
    }


    @Override
    public ArrayList<ArrayList<Integer>> controlEnemy(String[][] board) {
        ArrayList<ArrayList<Integer>> returnList = new ArrayList<>();
        for (int[] row : Commands.possibleMove) {
            int newI = getI() + row[0];
            int newJ = getJ() + row[1];
            if (0 <= newI && newI < board.length && 0 <= newJ && newJ < board.length) {
                if ("HED".contains(board[newI][newJ].substring(0, 1))) {
                    ArrayList<Integer> add = new ArrayList<>();
                    add.add(newI);
                    add.add(newJ);
                    returnList.add(add);
                }
            }
        }
        return returnList;
    }

    @Override
    public void FightToDeath(Characters character) {
        character.Damage(AP);
        if (HP > character.getHP()){
            Damage(character.getHP());
            character.setHP(0);
        }
        else if (HP < character.getHP()){
            character.Damage(HP);
            setHP(0);
        }
        else if (HP==character.getHP()){
            character.setHP(0);
            setHP(0);
        }
    }

    @Override
    public void Damage(int damage) {
        HP-=damage;
        if (HP<0) {
            HP=0;
        }
    }

    @Override
    public void setHP(int HP) {
        this.HP = HP;
    }

    @Override
    public int getHP() {
        return HP;
    }

    @Override
    public int getMaxMove() {
        return Constants.orkMaxMove;
    }

    @Override
    public int getConstantHP() {
        return constantHP;
    }
}
