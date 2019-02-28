import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class StarWarsArenaGUI extends JFrame {
    private final static String IMAGE_PATH = "Images/";
    private final static String MAIN_ICON = IMAGE_PATH + "StarWarsArenaIcon4.png";
    private final static String LIGHTSABER_ICON = IMAGE_PATH + "LightSaberIcon.png";
    private final static String FORCE_PUSH_ICON = IMAGE_PATH + "ForcePushIcon.png";
    private final static String OBI_HG_ICON = IMAGE_PATH + "ForcePushIcon.png";
    private final static String OBIWAN_DEFAULT_IMAGE = IMAGE_PATH + "ObiWan3.png";
    private final static String OBIWAN_ATTACK_IMAGE = IMAGE_PATH + "ObiWan2.png";
    private final static String ANAKIN_DEFAULT_IMAGE = IMAGE_PATH + "Anakin1.png";
    private final static String ANAKIN_ATTACK_IMAGE = IMAGE_PATH + "Anakin5.png";
    private final static String LIGHTSABER_FIGHT_IMAGE = IMAGE_PATH + "LightSaberFight.png";
    private final static String FORCEPUSH_FIGHT_IMAGE = IMAGE_PATH + "ForcePushFight2.png";
    private final static String GAMEOVER_IMAGE = IMAGE_PATH + "GameOver.png";

    private final static String TITLE = "Star Wars Arena";
    private final static String DEFEAT_ANAKIN_TEXT = "Defeat Anakin!";

    private final static int SIZE_WIDTH = 1000;
    private final static int SIZE_HEIGHT = 600;
    private final static int DEFAULT_HP = 100;

    private JPanel mainPanel;
    private JLabel statusBar;
    private JLabel mainObiWanImage;
    private JLabel mainAnakinImage;
    private JLabel mainBattleImage;
    private JToolBar commandBar;

    private JButton attackLightsaberButton;
    private JButton attackForcePushButton;
    private JButton attackHighGroundButton;
    private JButton resetButton;
    private JLabel yourHealthStatus;
    private JLabel enemyHealthStatus;
    private JLabel turnCounter;

    private StarWarsCharacter player;
    private StarWarsCharacter computer;
    private int turns;

    public StarWarsArenaGUI() {
        player = new StarWarsCharacter(DEFAULT_HP);
        computer = new StarWarsCharacter(DEFAULT_HP);
        turns = 0;
        initUI();
    }

    /////////////Creates the Main GUI//////////////
    private void initUI() {
        setWindowAttributes(MAIN_ICON, TITLE, SIZE_WIDTH, SIZE_HEIGHT);
        createMainPanel(OBIWAN_DEFAULT_IMAGE, ANAKIN_DEFAULT_IMAGE);
        createStatusBar(DEFEAT_ANAKIN_TEXT);
        createCommandBar();
        createLightsaberButton(LIGHTSABER_ICON);
        createForcePushButton();
        createHighGroundButton();
        createResetButton();
        createHealthStatus();
        createTurnCounter();
    }

    private void setWindowAttributes(String icon, String title, int width, int height) {
        ImageIcon webIcon = new ImageIcon(icon);
        setIconImage(webIcon.getImage());
        setTitle(title);
        setSize(width, height);

        //center the window on the screen
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setVisible(true);
    }

    private void createMainPanel(String westImage, String eastImage) {
        mainPanel = new JPanel(new BorderLayout());
        //creating a border around the bottom panel
        mainPanel.setBorder(new EmptyBorder(new Insets(0, 0 ,30, 0)));
        mainObiWanImage = new JLabel(new ImageIcon(westImage));
        mainPanel.add(mainObiWanImage, BorderLayout.WEST);

        mainBattleImage = new JLabel(new ImageIcon());
        mainPanel.add(mainBattleImage, BorderLayout.CENTER);

        mainAnakinImage = new JLabel(new ImageIcon(eastImage));
        mainPanel.add(mainAnakinImage, BorderLayout.EAST);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void createStatusBar(String status) {
        statusBar = new JLabel(status);
        add(statusBar, BorderLayout.NORTH);
    }

    private void createCommandBar() {
        commandBar = new JToolBar(JToolBar.HORIZONTAL);
        commandBar.setFloatable(false);
        commandBar.setMargin(new Insets(10,5,5,5));
        add(commandBar, BorderLayout.SOUTH);
    }

    private void createLightsaberButton(String icon) {
        ImageIcon lightsaberIcon = new ImageIcon(icon);
        attackLightsaberButton = new JButton("Lightsaber", lightsaberIcon);
        ButtonLightSaberListener lightSaberListener = new ButtonLightSaberListener();
        attackLightsaberButton.addActionListener(lightSaberListener);
        attackLightsaberButton.setToolTipText("An inconsistent attack. Deals 5 dmg. If enemy is below 100 HP, deals 0-20 dmg. Receive 10 dmg.");
        commandBar.add(attackLightsaberButton);
    }

    private void createForcePushButton() {
        ImageIcon forcepushIcon = new ImageIcon(FORCE_PUSH_ICON);
        attackForcePushButton = new JButton("Force Push", forcepushIcon);
        ButtonForcePushListener forcePushListener = new ButtonForcePushListener();
        attackForcePushButton.addActionListener(forcePushListener);
        attackForcePushButton.setToolTipText("A consistent attack. Deals 5 dmg. Receive 0-20 dmg.");
        commandBar.add(attackForcePushButton);
    }

    private void createHighGroundButton() {
        ImageIcon obiwanIcon = new ImageIcon(OBI_HG_ICON);
        attackHighGroundButton = new JButton("ULT: High Ground", obiwanIcon);
        ButtonHighGroundListener highGroundListener = new ButtonHighGroundListener();
        attackHighGroundButton.addActionListener(highGroundListener);
        attackHighGroundButton.setToolTipText("Can only be used when enemy is 40 Health or below.");
        commandBar.add(attackHighGroundButton);
    }

    private void createResetButton() {
        resetButton = new JButton("RESET");
        ResetButtonListener resetButtonListener = new ResetButtonListener();
        resetButton.addActionListener(resetButtonListener);
        resetButton.setToolTipText("Reset Game");
        commandBar.add(resetButton);
    }

    private void createHealthStatus() {
        yourHealthStatus = new JLabel("Your Health: " + player.getHealth());
        yourHealthStatus.setBorder(new EmptyBorder(0,10,0,40));
        commandBar.add(yourHealthStatus);

        enemyHealthStatus = new JLabel("Enemy Health: " + computer.getHealth());
        enemyHealthStatus.setBorder(new EmptyBorder(0,10,0,40));
        commandBar.add(enemyHealthStatus);
    }

    private void createTurnCounter() {
        turnCounter = new JLabel("Turns: " + turns);
        turnCounter.setBorder(new EmptyBorder(0,10,0,40));
        commandBar.add(turnCounter);
    }

    ////////////////////////CREATING LISTENERS FOR THE BUTTONS/////////////////
    /*
     * Adding functions for lightsaber button
     * Adding conditional statements for various scenarios regarding player's health
     * If player alive, change information on screen and allow certain actions based on enemy's health
     * If player is not alive, game over
     */
    public class ButtonLightSaberListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (player.getHealth() > 0) {
                if (computer.getHealth() > 100) {
                    lightSaber();
                    turns++;
                    turnCounter.setText("Turns: " + turns);
                    mainBattleImage.setIcon(new ImageIcon(LIGHTSABER_FIGHT_IMAGE));
                    yourHealthStatus.setText("Your Health: " + player.getHealth());
                    enemyHealthStatus.setText("Enemy Health: " + computer.getHealth());
                }
                if (computer.getHealth() <= 100 && computer.getHealth() > 40) {
                    lightSaber(5, 6);
                    turns++;
                    turnCounter.setText("Turns: " + turns);
                    mainBattleImage.setIcon(new ImageIcon(LIGHTSABER_FIGHT_IMAGE));
                    yourHealthStatus.setText("Your Health: " + player.getHealth());
                    enemyHealthStatus.setText("Enemy Health: " + computer.getHealth());
                }
                if (computer.getHealth() > 0 && computer.getHealth() <= 40) {
                    lightSaber(5, 6);
                    turns++;
                    statusBar.setText("Obi Wan: It's over Anakin! I have the high ground!");
                    mainObiWanImage.setIcon(new ImageIcon(OBIWAN_ATTACK_IMAGE));
                    mainAnakinImage.setIcon(new ImageIcon(ANAKIN_ATTACK_IMAGE));
                    mainBattleImage.setIcon(new ImageIcon());
                    attackHighGroundButton.setBackground(new Color(255, 204, 204));
                    yourHealthStatus.setText("Your Health: " + player.getHealth());
                    enemyHealthStatus.setText("Enemy Health: " + computer.getHealth());
                }
                if (computer.getHealth() <= 0) {
                    mainBattleImage.setIcon(new ImageIcon());
                    statusBar.setText("Obi Wan: You were the Chosen One! It was said that you would destroy the Sith, "
                            + "not join them!");
                    mainObiWanImage.setIcon(new ImageIcon(OBIWAN_ATTACK_IMAGE));
                    mainAnakinImage.setIcon(new ImageIcon(ANAKIN_ATTACK_IMAGE));
                    enemyHealthStatus.setText("Enemy Health: " + computer.getHealth());
                    yourHealthStatus.setText("Your Health: " + player.getHealth());
                }
            } else {
                statusBar.setText("Game Over. You failed to defeat Anakin.");
                mainObiWanImage.setIcon(new ImageIcon());
                mainAnakinImage.setIcon(new ImageIcon());
                mainBattleImage.setIcon(new ImageIcon(GAMEOVER_IMAGE));
                yourHealthStatus.setText("Your Health: " + player.getHealth());
                enemyHealthStatus.setText("Enemy Health: " + computer.getHealth());
            }
        }
    }

    /*
     * Adding functions for force push button
     * Adding conditional statements for various scenarios regarding player's health
     * If player alive, change information on screen and allow certain actions based on enemy's health
     * If player is not alive, game over
     */
    public class ButtonForcePushListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (player.getHealth() > 0) {
                if (computer.getHealth() > 40) {
                    forcePush(5, 6);
                    turns++;
                    turnCounter.setText("Turns: " + turns);
                    yourHealthStatus.setText("Your Health: " + player.getHealth());
                    enemyHealthStatus.setText("Enemy Health: " + computer.getHealth());
                    mainBattleImage.setIcon(new ImageIcon(FORCEPUSH_FIGHT_IMAGE));
                }
                if (computer.getHealth() > 0 && computer.getHealth() <= 40) {
                    forcePush(5, 6);
                    turns++;
                    turnCounter.setText("Turns: " + turns);
                    statusBar.setText("It's over Anakin! I have the high ground!");
                    yourHealthStatus.setText("Your Health: " + player.getHealth());
                    enemyHealthStatus.setText("Enemy Health: " + computer.getHealth());
                    attackHighGroundButton.setBackground(new Color(255, 204, 204));
                    mainObiWanImage.setIcon(new ImageIcon(OBIWAN_ATTACK_IMAGE));
                    mainAnakinImage.setIcon(new ImageIcon(ANAKIN_ATTACK_IMAGE));
                    mainBattleImage.setIcon(new ImageIcon());
                }
                if (computer.getHealth() <= 0) {
                    enemyHealthStatus.setText("Enemy Health: 0");
                    mainBattleImage.setIcon(new ImageIcon());
                    statusBar.setText("Obi Wan: You were the Chosen One! It was said that you would destroy the Sith, "
                            + "not join them!");
                    mainObiWanImage.setIcon(new ImageIcon(OBIWAN_ATTACK_IMAGE));
                    mainAnakinImage.setIcon(new ImageIcon(ANAKIN_ATTACK_IMAGE));
                }
            } else {
                statusBar.setText("Game Over. You failed to defeat Anakin.");
                mainObiWanImage.setIcon(new ImageIcon());
                mainAnakinImage.setIcon(new ImageIcon());
                mainBattleImage.setIcon(new ImageIcon(GAMEOVER_IMAGE));
                yourHealthStatus.setText("Your Health: 0");
                enemyHealthStatus.setText("Enemy Health: " + computer.getHealth());
            }
        }
    }

    /*
     * Adding functions for high ground button
     * Adding conditional statements for various scenarios regarding enemy's health
     * If enemy is below 40 health, trigger on screen images and text, and call highGround() method
     * Otherwise, does not let player use button
     */
    public class ButtonHighGroundListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (computer.getHealth() <= 40 && player.getHealth() > 0) {
                highGround();
                turns++;
                turnCounter.setText("Turns: " + turns);
                yourHealthStatus.setText("Your Health: " + player.getHealth());
                enemyHealthStatus.setText("Enemy Health: " + computer.getHealth());
                mainBattleImage.setIcon(new ImageIcon());
                statusBar.setText("Obi Wan: You were the Chosen One! It was said that you would destroy the Sith, "
                        + "not join them!");
                mainObiWanImage.setIcon(new ImageIcon(OBIWAN_ATTACK_IMAGE));
                mainAnakinImage.setIcon(new ImageIcon(ANAKIN_ATTACK_IMAGE));
            } else {
                statusBar.setText("You cannot use High Ground while enemy has more than 40 health or if you are dead.");
            }
        }
    }

    public class ResetButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            player.setHealth(DEFAULT_HP);
            computer.setHealth(DEFAULT_HP);
            turns = 0;
            turnCounter.setText("Turns: " + turns);
            statusBar.setText("Defeat Anakin!");
            yourHealthStatus.setText("Your Health: " + player.getHealth());
            enemyHealthStatus.setText("Enemy Health: " + computer.getHealth());
            mainObiWanImage.setIcon(new ImageIcon(OBIWAN_DEFAULT_IMAGE));
            mainAnakinImage.setIcon(new ImageIcon(ANAKIN_DEFAULT_IMAGE));
            mainBattleImage.setIcon(new ImageIcon());
            attackHighGroundButton.setBackground(new JButton().getBackground());
        }
    }

    /////////PLAYER ATTACK MOVES////////////
    //A lightSaber method that applies standard damages using light saber
    private void lightSaber() {
        player.setHealth(player.getHealth() - 10);
        computer.setHealth(computer.getHealth() - 5);
    }

    /*
     * An overloaded lightSaber method that allows player to use a riskier lightsaber attack.
     * @param randomIndex choose an int and use it to initiate a random number generator
     * @param randomMultiplier choose an int and use it to initiate a random number generator
     * Random index of a damageArray containing attack damages is multiplied by random multiplier.
     * Output value is used to deduct enemy health
     */
    private void lightSaber(int randomIndex, int randomMultiplier) {
        Random rand = new Random();
        int multiplier = rand.nextInt(randomMultiplier);
        int index = rand.nextInt(randomIndex);
        int[] damageArray = {0, 1, 2, 3, 4};
        int randomDamage = damageArray[index] * multiplier;
        player.setHealth(player.getHealth() - 10);
        computer.setHealth(computer.getHealth() - randomDamage);
    }

    /*
     * A forcePush method that allows player to use a consistent force push attack.
     * @param randomIndex choose an int and use it to initiate a random number generator
     * @param randomMultiplier choose an int and use it to initiate a random number generator
     * Random index of a damageArray containing attack damages is multiplied by random multiplier.
     * Output value is used to deduct player health.
     */
    private void forcePush(int randomIndex, int randomMultiplier) {
        Random rand = new Random();
        int multiplier = rand.nextInt(randomMultiplier);
        int index = rand.nextInt(randomIndex);
        int[] damageArray = {0, 1, 2, 3, 4};
        int randomDamage = damageArray[index] * multiplier;
        player.setHealth(player.getHealth() - 5);
        computer.setHealth(computer.getHealth() - randomDamage);
    }

    private void highGround() {
        computer.setHealth(0);
    }

    public static void main(String[] args) {
        new StarWarsArenaGUI();
    }
}