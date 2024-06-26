public class FirstClass3Team
{
    public static void main(String[] args)
    {
        System.out.println("這是用來練習GitHub協作的程式!");
        System.out.println("\"學號:\"01081022\"\n姓名:\"林韋全\"");
        System.out.println("\"學號:\"01157055\"\n姓名:\"蔡竣頎\"");
        System.out.println("\"學號:\"01157043\"\n姓名:\"蔡奇佑\"");
        System.out.println("\"學號:\"12345678\"\n姓名:\"谷翔平\"");
    }
}

// 一開始的畫面
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuInterface extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private BasketballApp basketballApp;

    public MenuInterface() {
        setTitle("Menu Interface");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);

        basketballApp = new BasketballApp(this);
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        JPanel menuPanel = new JPanel(new GridLayout(3, 1));
        JButton teamManagementButton = new JButton("create or manage teams and players");
        JButton otherButton1 = new JButton("create or search games data");
        JButton otherButton2 = new JButton("search or analyze players carrer data");

        teamManagementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                basketballApp.setVisible(true);
                MenuInterface.this.setVisible(false);
            }
        });

        menuPanel.add(teamManagementButton);
        menuPanel.add(otherButton1);
        menuPanel.add(otherButton2);

        cardPanel.add(menuPanel, "Menu");

        add(cardPanel);

        cardLayout.show(cardPanel, "Menu");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MenuInterface().setVisible(true);
            }
        });
    }
}

// 球隊和球員
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BasketballApp extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JPanel teamPanel;
    private ArrayList<String[]> teamsList;
    private MenuInterface menuInterface;
    private HashMap<String, List<String[]>> teamPlayers = new HashMap<>();

    // teams
    public BasketballApp(MenuInterface menuInterface) {
        if (menuInterface == null) {
            throw new IllegalArgumentException("MenuInterface cannot be null");
        }

        this.menuInterface = menuInterface;
        setTitle("NBA Teams and Players");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        teamsList = new ArrayList<>();
        teamsList.add(new String[] { "Lakers", "Los Angeles", "1947" });
        teamsList.add(new String[] { "Celtics", "Boston", "1946" });
        teamsList.add(new String[] { "Nets", "Brooklyn", "1967" });

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        teamPanel = createTeamPanel();
        cardPanel.add(teamPanel, "Teams");

        for (String[] team : teamsList) {
            cardPanel.add(createPlayersPanel(team[0]), team[0] + " Players");
        }

        add(cardPanel, BorderLayout.CENTER);

        initializeUI();
    }

    private void initializeUI() {
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> backToMenu());
        add(backButton, BorderLayout.SOUTH);
    }

    private void backToMenu() {
        this.setVisible(false);
        menuInterface.setVisible(true);
    }

    private JPanel createTeamPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1));

        for (String[] team : teamsList) {
            JButton teamButton = new JButton(team[0]);
            teamButton.addActionListener(e -> cardLayout.show(cardPanel, team[0] + " Players"));
            panel.add(teamButton);
        }

        JButton addButton = new JButton("Add Team");
        addButton.addActionListener(e -> {
            JPanel addPanel = createAddDataPanel();
            cardPanel.add(addPanel, "AddData");
            cardLayout.show(cardPanel, "AddData");
        });
        panel.add(addButton);

        return panel;
    }

    // jump to players
    private JPanel createPlayersPanel(String teamName) {
        JPanel panel = new JPanel(new BorderLayout());

        String[][] data = {
                { "Player Name", "Number", "Position", "Age", "Height", "Weight" }
        };

        if (teamName.equals("Lakers")) {
            data = new String[][] {
                    { "LeBron James", "23", "SF", "39", "6'7", "210 lbs" },
                    { "Anthony Davis", "3", "PF-C", "31", "6'10", "253 lbs" }
            };
        } else if (teamName.equals("Celtics")) {
            data = new String[][] {
                    { "Jayson Tatum", "0", "SF", "26", "6'8", "210 lbs" }
            };
        } else if (teamName.equals("Nets")) {
            data = new String[][] {
                    { "Ben Simmons", "10", "PG-PF", "27", "6'10", "240 lbs" },
            };
        }

        String[] columnNames = { "Player", "Number", "Position", "Age", "Height", "Weight" };
        JTable table = new JTable(data, columnNames);

        table.setFillsViewportHeight(true);
        table.setGridColor(Color.GRAY);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel nameLabel = new JLabel(teamName);
        nameLabel.setFont(new Font("Serif", Font.BOLD, 24));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(nameLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backButton = new JButton("Back to Teams");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "Teams"));

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> showNextTeamPanel(teamName));
        JButton previousButton = new JButton("Previous");
        previousButton.addActionListener(e -> showPreviousTeamPanel(teamName));

        buttonPanel.add(backButton);
        buttonPanel.add(previousButton);
        buttonPanel.add(nextButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        panel.add(headerPanel, BorderLayout.NORTH);

        JButton createPlayerButton = new JButton("Create Player");
        createPlayerButton.addActionListener(e -> {
            JPanel addPlayerPanel = createAddPlayerPanel(teamName);
            cardPanel.add(addPlayerPanel, teamName + " AddPlayer");
            cardLayout.show(cardPanel, teamName + " AddPlayer");
        });

        buttonPanel.add(backButton);
        buttonPanel.add(createPlayerButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    // create team
    private JPanel createAddDataPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2));

        JTextField nameField = new JTextField();
        JTextField cityField = new JTextField();
        JTextField yearField = new JTextField();

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("City:"));
        panel.add(cityField);
        panel.add(new JLabel("Foundation Year:"));
        panel.add(yearField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String[] newTeam = { nameField.getText(), cityField.getText(), yearField.getText() };
            teamsList.add(newTeam);
            cardPanel.remove(teamPanel);
            teamPanel = createTeamPanel();
            cardPanel.add(teamPanel, "Teams");
            cardLayout.show(cardPanel, "Teams");
        });

        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> cardLayout.show(cardPanel, "Teams"));

        panel.add(saveButton);
        panel.add(quitButton);

        return panel;
    }

    // creat players
    private JPanel createAddPlayerPanel(String teamName) {
        JPanel panel = new JPanel(new GridLayout(4, 2));

        JTextField playerNameField = new JTextField();
        JTextField playerNumberField = new JTextField();
        JTextField playerPositionField = new JTextField();
        JTextField playerAgeField = new JTextField();
        JTextField playerHeightField = new JTextField();
        JTextField playerWeightField = new JTextField();

        panel.add(new JLabel("Player Name:"));
        panel.add(playerNameField);
        panel.add(new JLabel("Number:"));
        panel.add(playerNumberField);
        panel.add(new JLabel("Position:"));
        panel.add(playerPositionField);
        panel.add(new JLabel("Age:"));
        panel.add(playerAgeField);
        panel.add(new JLabel("Height:"));
        panel.add(playerHeightField);
        panel.add(new JLabel("Weight:"));
        panel.add(playerWeightField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String[] newPlayer = { playerNameField.getText(), playerNumberField.getText(),
                    playerPositionField.getText(), playerAgeField.getText(),
                    playerHeightField.getText(), playerWeightField.getText() };

            teamPlayers.putIfAbsent(teamName, new ArrayList<>());
            teamPlayers.get(teamName).add(newPlayer);

            cardPanel.remove(teamPanel);
            teamPanel = createPlayersPanel(teamName); // Assuming this method now uses `teamPlayers`
            cardPanel.add(teamPanel, teamName);
            cardLayout.show(cardPanel, teamName);
        });

        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> cardLayout.show(cardPanel, "Teams"));

        panel.add(saveButton);
        panel.add(quitButton);

        return panel;
    }

    // for the circle of teams
    private void showNextTeamPanel(String currentTeam) {
        int index = findTeamIndex(currentTeam);
        index = (index + 1) % teamsList.size();
        String nextTeam = teamsList.get(index)[0];
        cardLayout.show(cardPanel, nextTeam + " Players");
    }

    private void showPreviousTeamPanel(String currentTeam) {
        int index = findTeamIndex(currentTeam);
        index = (index - 1 + teamsList.size()) % teamsList.size();
        String prevTeam = teamsList.get(index)[0];
        cardLayout.show(cardPanel, prevTeam + " Players");
    }

    private int findTeamIndex(String teamName) {
        for (int i = 0; i < teamsList.size(); i++) {
            if (teamsList.get(i)[0].equals(teamName)) {
                return i;
            }
        }
        return -1;
    }

}
