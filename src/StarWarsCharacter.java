public class StarWarsCharacter {
    private int myHealth;

    public StarWarsCharacter(int health) {
        myHealth = health;
    }

    public int getHealth() {
        if (myHealth >= 0) { return myHealth; }
        else { return 0; }
    }

    public void setHealth(int myHealth) {
        this.myHealth = myHealth;
    }
}
