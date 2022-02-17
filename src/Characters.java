import java.util.ArrayList;

public class Characters {
    private String name;
    private int i;
    private int j;
    private int HP;
    private int AP;
    private int MaxMove;
    private int constantHP;


    public Characters(String name, int i, int j) {
        this.name = name;
        this.i = i;
        this.j = j;
    }
    public void AttackEnemy(ArrayList<Characters> charactersArrayList, String[][] board){
    }


    public void FightToDeath(Characters character){
    }

    public ArrayList<ArrayList<Integer>> controlEnemy (String[][] board) {
        return null;
    }

    public void Damage(int damage){
        HP -= damage;
    }

    public void Move(int x,int y){
        i += x;
        j += y;
    }

    public int getMaxMove() {
        return MaxMove;
    }

    public int getConstantHP() {
        return constantHP;
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }

    public String getName() {
        return name;
    }

    public int getI() {
        return i;
    }


    public int getJ() {
        return j;
    }
}
