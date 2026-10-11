import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.io.*;
import java.nio.file.*;
import java.awt.event.*;
import java.awt.geom.Path2D;
import java.net.URI;

public class QuestKeysWidget {
    
    private static final Path SAVE_FILE =
        Paths.get(System.getProperty("user.home"), "questkeys.txt");
    
    public static Shape createMushroomShape() {

        Path2D.Double mushroom = new Path2D.Double();

        // Start at the bottom-left of the mushroom cap
        mushroom.moveTo(40, 230);

        // Draw the rounded mushroom cap
        mushroom.curveTo(0, 230, 0, 60, 200, 30);
        mushroom.curveTo(400, 60, 400, 230, 360, 230);

        // Connect the cap to the stem
        mushroom.lineTo(310, 230);

        // Right side of the stem
        mushroom.lineTo(310, 460);
        mushroom.quadTo(310, 490, 280, 490);

        // Bottom of the stem
        mushroom.lineTo(120, 490);
        mushroom.quadTo(90, 490, 90, 460);

        // Left side of the stem
        mushroom.lineTo(90, 230);

        // Close the shape
        mushroom.closePath();

        return mushroom;
    }

    public static JPanel createMushroomBackground() {

        JPanel mushroomPanel = new JPanel(null) {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setColor(MUSHROOM);
                g2.fill(createMushroomShape());

                g2.setColor(PARCHMENT);
                g2.fillRoundRect(90, 225, 220, 265, 30, 30);

                g2.dispose();
            }
        };

        mushroomPanel.setOpaque(false);

        return mushroomPanel;
    }

    private static final Color MUSHROOM = new Color(169, 54, 59);    
    private static final Color PARCHMENT = new Color(255, 243, 221);
    private static final Color GOLD = new Color(244, 183, 68);
    private static final Color SAGE = new Color(85, 201, 141);
    private static final Color INK = new Color(39, 84, 74);
    private static final Color CREAM = new Color(255, 255, 255);
    private static final Color PINK = new Color(229, 84, 153);
    private static final Color BLUE = new Color(75, 164, 232);

    public static void main(String[] args) {

        JLabel exploreLink = new JLabel("Explore your world →");
        exploreLink.setForeground(INK);
        exploreLink.setFont(new Font("SansSerif", Font.BOLD, 12));
        exploreLink.setHorizontalAlignment(SwingConstants.CENTER);
        exploreLink.setBounds(105, 400, 190, 20);

        exploreLink.setCursor(
            Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );

        exploreLink.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            try {
                Desktop.getDesktop().browse(
                    new URI("http://localhost:8000")
                );
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    });
        
        SwingUtilities.invokeLater(() -> {

            JFrame frame = new JFrame("Burgeon");

            frame.setUndecorated(true);
            frame.setBackground(new Color(0, 0, 0, 0));
            frame.getRootPane().setDoubleBuffered(false);

            frame.setSize(400, 500);
            frame.setShape(createMushroomShape());


            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setAlwaysOnTop(true);
            frame.setLocationRelativeTo(null);

            Point[] dragStart = {null};

            frame.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    dragStart[0] = e.getPoint();
                }
            });

            frame.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (dragStart[0] != null) {
                        Point location = e.getLocationOnScreen();

                        frame.setLocation(
                            location.x - dragStart[0].x,
                            location.y - dragStart[0].y
                        );
                    }
                }
            });

            JPanel panel = createMushroomBackground();
            panel.setOpaque(false);
            panel.setDoubleBuffered(false);

            JLabel title = new JLabel("Burgeon");
            title.setFont(new Font("Georgia", Font.BOLD, 34));
            title.setHorizontalAlignment(SwingConstants.CENTER);
            title.setForeground(CREAM);
            title.setBounds(65, 135, 270, 55);   

            JButton closeButton = new JButton("×");

            closeButton.setForeground(Color.WHITE);
            closeButton.setBackground(MUSHROOM);
            closeButton.setBorderPainted(false);
            closeButton.setFocusPainted(false);
            closeButton.setBounds(45, 205, 42, 30);  

            closeButton.addActionListener(e -> {
                frame.dispose();
            });

            JTextField taskInput = new JTextField(15);
            JButton addButton = new JButton("+");

            JPanel taskPanel = new JPanel();
            taskPanel.setLayout(
                new BoxLayout(taskPanel, BoxLayout.Y_AXIS)
            );
            taskPanel.setBackground(PARCHMENT);

            ArrayList<JCheckBox> taskList = new ArrayList<>();

            JLabel pointsLabel = new JLabel("Seeds: 0");
            pointsLabel.setForeground(GOLD);
            pointsLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
            pointsLabel.setForeground(INK);
            pointsLabel.setHorizontalAlignment(SwingConstants.CENTER);
            pointsLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
            pointsLabel.setBounds(110, 419, 180, 20);

            JProgressBar progressBar = new JProgressBar(0, 100);
            progressBar.setStringPainted(true);
            progressBar.setForeground(SAGE);
            progressBar.setBackground(PARCHMENT);
            progressBar.setBorderPainted(false);
            progressBar.setBounds(110, 445, 180, 15);

        addButton.setBounds(265, 378, 32, 32);
        addButton.setFont(new Font("SansSerif", Font.BOLD, 20));

        addButton.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(
                frame,
                "What is your new quest?"
            );

            if (name != null && !name.trim().isEmpty()) {
                addQuest(
                    name.trim(),
                    false,
                    taskList,
                    taskPanel,
                    pointsLabel,
                    progressBar
                );

                saveQuests(taskList);
            }
        });

            taskInput.addActionListener(event -> {
                addButton.doClick();
            });

            JPanel topPanel = new JPanel(new BorderLayout());

            topPanel.setBackground(MUSHROOM);

            topPanel.setOpaque(false);

            JPanel centerPanel = new JPanel(new BorderLayout());
            centerPanel.setBackground(MUSHROOM);
            JScrollPane scrollPane = new JScrollPane(taskPanel);
            scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
            );
            centerPanel.add(scrollPane, BorderLayout.CENTER);
            centerPanel.setBounds(100, 270, 200, 105);
            centerPanel.setOpaque(false);

            taskPanel.setBackground(PARCHMENT);

            panel.add(title);
            panel.add(closeButton);
            panel.add(centerPanel);
            panel.add(addButton);
            panel.add(exploreLink);
            panel.add(pointsLabel);
            panel.add(progressBar);

            loadQuests(taskList, taskPanel, pointsLabel, progressBar);

            frame.add(panel);
            frame.setVisible(true);
        });
    }

    public static void updateProgress(
            ArrayList<JCheckBox> taskList,
            JLabel pointsLabel,
            JProgressBar progressBar) {

        int completed = 0;

        for (JCheckBox task : taskList) {
            if (task.isSelected()) {
                completed++;
            }
        }

        int points = completed * 10;

        int percentage = 0;

        if (taskList.size() > 0) {
            percentage = (completed * 100) / taskList.size();
        }

        pointsLabel.setText("Seeds: " + points);
        progressBar.setValue(percentage);

        try {
            Path seedFile = Paths.get("seeds.json");
            
            Files.writeString(
                seedFile,
                "{\"earnedSeeds\": " + points + "}"
            );
        } catch (IOException e) {
            System.out.println("Could not update seeds: " + e.getMessage());
        }

        
    }

    public static void saveQuests(ArrayList<JCheckBox> taskList) {

        try (BufferedWriter writer =
                Files.newBufferedWriter(SAVE_FILE)) {

            for (JCheckBox task : taskList) {

                String name = task.getText();
                boolean completed = task.isSelected();

                writer.write(completed + "\t" + name);
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error saving quests: " + e.getMessage());
        }
    }

    public static void loadQuests(
            ArrayList<JCheckBox> taskList,
            JPanel taskPanel,
            JLabel pointsLabel,
            JProgressBar progressBar) {

        if (!Files.exists(SAVE_FILE)) {
            return;
        }

        try (BufferedReader reader =
                Files.newBufferedReader(SAVE_FILE)) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split("\t", 2);

                if (parts.length != 2) {
                    continue;
                }

                boolean completed = Boolean.parseBoolean(parts[0]);
                String name = parts[1];

                addQuest(
                    name,
                    completed,
                    taskList,
                    taskPanel,
                    pointsLabel,
                    progressBar
                );
            }

            updateProgress(taskList, pointsLabel, progressBar);

        } catch (IOException e) {
            System.out.println("Error loading quests: " + e.getMessage());
        }
    }

    public static void addQuest(
            String name,
            boolean completed,
            ArrayList<JCheckBox> taskList,
            JPanel taskPanel,
            JLabel pointsLabel,
            JProgressBar progressBar) {

        JPanel questRow = new JPanel(new BorderLayout());
        questRow.setBackground(PARCHMENT);
        questRow.setOpaque(true);
        questRow.setBorder(
            BorderFactory.createEmptyBorder(3, 2, 3, 2)
        );

        JCheckBox task = new JCheckBox(name);
        task.setOpaque(true);
        task.setBackground(PARCHMENT);
        task.setForeground(INK);
        task.setFont(new Font("SansSerif", Font.PLAIN, 13));
        task.setSelected(completed);

        JButton editButton = new JButton("✎");
        JButton deleteButton = new JButton("×");
        editButton.setMargin(new Insets(2, 4, 2, 4));
        deleteButton.setMargin(new Insets(2, 4, 2, 4));
        editButton.setForeground(BLUE);
        deleteButton.setForeground(PINK);

        editButton.setPreferredSize(new Dimension(20, 20));
        deleteButton.setPreferredSize(new Dimension(20, 20));

        editButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        deleteButton.setFont(new Font("SansSerif", Font.PLAIN, 12));

        editButton.setFocusable(false);
        deleteButton.setFocusable(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 0));
        buttonPanel.setOpaque(true);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.setBackground(PARCHMENT);

        questRow.add(task, BorderLayout.CENTER);
        questRow.add(buttonPanel, BorderLayout.EAST);

        taskList.add(task);
        taskPanel.add(questRow);

        task.addActionListener(event -> {
            updateProgress(taskList, pointsLabel, progressBar);
            saveQuests(taskList);
        });

        editButton.addActionListener(event -> {

            String newName = JOptionPane.showInputDialog(
                questRow,
                "Edit your quest:",
                task.getText()
            );

            if (newName != null && !newName.trim().isEmpty()) {
                task.setText(newName.trim());
                saveQuests(taskList);
            }
        });

        deleteButton.addActionListener(event -> {

            taskList.remove(task);
            taskPanel.remove(questRow);

            updateProgress(taskList, pointsLabel, progressBar);
            saveQuests(taskList);

            taskPanel.revalidate();
            taskPanel.repaint();
        });

        updateProgress(taskList, pointsLabel, progressBar);

        taskPanel.revalidate();
        taskPanel.repaint();
    }
}