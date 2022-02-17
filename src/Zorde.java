public class Zorde extends Characters{
    private int HP;

    public Zorde(String name, int i, int j) {
        super(name, i, j);
    }

    public void heal(){
        HP+=10;
    }


}
