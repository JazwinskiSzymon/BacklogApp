import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class BacklogApp extends JFrame {
    private List<TaskPanel> taskPanels;
    private JPanel tablesPanel;
    private Color selectedColor = Color.WHITE;

    public BacklogApp() {
        setTitle("Backlog App");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        tablesPanel = new JPanel(new GridLayout(1, 5));
        taskPanels = new ArrayList<>();

        addTaskPanel("Milestones");
        addTaskPanel("Todo");
        addTaskPanel("To correct");
        addTaskPanel("In Progress");
        addTaskPanel("Done");

        add(tablesPanel, BorderLayout.CENTER);

        JPanel buttonPanel = createButtonPanel();

        add(buttonPanel, BorderLayout.NORTH);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton addButton = new JButton("Add Task");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTask();
            }
        });



        JButton colorButton = new JButton("Select Color");
        colorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectColor();
            }
        });

        JButton removeButton = new JButton("Remove Task");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeTask();
            }
        });

        JButton addPanelButton = new JButton("Add Task Panel");
        addPanelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewTaskPanel();
            }
        });

        JButton removePanelButton = new JButton("Remove Task Panel");
        removePanelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeTaskPanel();
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(colorButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(addPanelButton);
        buttonPanel.add(removePanelButton);

        return buttonPanel;
    }

    private void addTaskPanel(String title) {
        TaskPanel taskPanel = new TaskPanel(title, this);
        taskPanels.add(taskPanel);
        tablesPanel.add(taskPanel);
        tablesPanel.setLayout(new GridLayout(1, taskPanels.size()));
        revalidate();
    }

    private void addNewTaskPanel() {
        String panelTitle = JOptionPane.showInputDialog(this, "Enter panel title:");
        if (panelTitle != null && !panelTitle.trim().isEmpty()) {
            addTaskPanel(panelTitle);
        }
    }

    private void removeTaskPanel() {
        if (taskPanels.size() > 1) {
            String[] panelTitles = new String[taskPanels.size()];
            for (int i = 0; i < taskPanels.size(); i++) {
                panelTitles[i] = taskPanels.get(i).getTitle();
            }
            String panelToRemove = (String) JOptionPane.showInputDialog(this, "Select panel to remove:", "Remove Task Panel", JOptionPane.QUESTION_MESSAGE, null, panelTitles, panelTitles[0]);

            if (panelToRemove != null) {
                for (TaskPanel panel : taskPanels) {
                    if (panel.getTitle().equals(panelToRemove)) {
                        taskPanels.remove(panel);
                        tablesPanel.remove(panel);
                        tablesPanel.setLayout(new GridLayout(1, taskPanels.size()));
                        revalidate();
                        repaint();
                        break;
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "At least one panel must remain.", "Cannot Remove Panel", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void addTask() {
        String taskDescription = JOptionPane.showInputDialog(this, "Enter task description:");
        if (taskDescription != null && !taskDescription.trim().isEmpty()) {
            if (!taskPanels.isEmpty()) {
                taskPanels.get(0).addTask(new Task(taskDescription, selectedColor));
            }
        }
    }

    private void selectColor() {
        selectedColor = JColorChooser.showDialog(this, "Select Task Color", selectedColor);
    }

    private void removeTask() {
        Task selectedTask = getSelectedTask();
        TaskPanel currentPanel = getCurrentPanel();

        if (selectedTask != null && currentPanel != null) {
            selectedTask.setVisible(false);
            currentPanel.repaintTaskList();
        }
    }

    private Task getSelectedTask() {
        for (TaskPanel panel : taskPanels) {
            if (panel.getTaskList().getSelectedValue() != null) {
                return panel.getTaskList().getSelectedValue();
            }
        }
        return null;
    }

    private TaskPanel getCurrentPanel() {
        for (TaskPanel panel : taskPanels) {
            if (panel.getTaskList().getSelectedValue() != null) {
                return panel;
            }
        }
        return null;
    }

    public void moveTask(TaskPanel fromPanel, TaskPanel toPanel, Task task) {
        fromPanel.removeTask(task);
        toPanel.addTask(task);
    }

    public TaskPanel getNextPanel(TaskPanel currentPanel) {
        int index = taskPanels.indexOf(currentPanel);
        if (index < taskPanels.size() - 1) {
            return taskPanels.get(index + 1);
        }
        return null;
    }

    public TaskPanel getPreviousPanel(TaskPanel currentPanel) {
        int index = taskPanels.indexOf(currentPanel);
        if (index > 0) {
            return taskPanels.get(index - 1);
        }
        return null;
    }

    class TaskPanel extends JPanel {
        private DefaultListModel<Task> listModel;
        private JList<Task> taskList;
        private JButton leftButton;
        private JButton rightButton;
        private BacklogApp app;
        private String title;

        public TaskPanel(String title, BacklogApp app) {
            this.app = app;
            this.title = title;
            setLayout(new BorderLayout());

            JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
            add(titleLabel, BorderLayout.NORTH);

            listModel = new DefaultListModel<>();
            taskList = new JList<>(listModel);
            taskList.setCellRenderer(new TaskCellRenderer());
            taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            taskList.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        Task selectedTask = taskList.getSelectedValue();
                        if (selectedTask != null) {
                            Color newColor = JColorChooser.showDialog(TaskPanel.this, "Select Task Color", selectedTask.getColor());
                            if (newColor != null) {
                                selectedTask.setColor(newColor);
                                repaintTaskList();
                            }
                        }
                    }
                }
            });

            add(new JScrollPane(taskList), BorderLayout.CENTER);

            leftButton = new JButton("<");
            rightButton = new JButton(">");

            leftButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Task selectedTask = taskList.getSelectedValue();
                    if (selectedTask != null) {
                        moveTaskLeft(selectedTask);
                    }
                }
            });

            rightButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Task selectedTask = taskList.getSelectedValue();
                    if (selectedTask != null) {
                        moveTaskRight(selectedTask);
                    }
                }
            });

            JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
            buttonPanel.add(leftButton);
            buttonPanel.add(rightButton);
            add(buttonPanel, BorderLayout.SOUTH);
        }

        public String getTitle() {
            return title;
        }

        public void addTask(Task task) {
            listModel.addElement(task);
        }

        public void removeTask(Task task) {
            listModel.removeElement(task);
        }

        public JList<Task> getTaskList() {
            return taskList;
        }

        public void repaintTaskList() {
            taskList.repaint();
        }

        private void moveTaskLeft(Task task) {
            TaskPanel previousPanel = app.getPreviousPanel(TaskPanel.this);
            if (previousPanel != null) {
                app.moveTask(TaskPanel.this, previousPanel, task);
            }
        }

        private void moveTaskRight(Task task) {
            TaskPanel nextPanel = app.getNextPanel(TaskPanel.this);
            if (nextPanel != null) {
                app.moveTask(TaskPanel.this, nextPanel, task);
            }
        }
    }

    class Task {
        private String description;
        private Color color;
        private boolean visible;

        public Task(String description, Color color) {
            this.description = description;
            this.color = color;
            this.visible = true;
        }

        public String getDescription() {
            return description;
        }

        public Color getColor() {
            return color;
        }

        public void setColor(Color color) {
            this.color = color;
        }

        public boolean isVisible() {
            return visible;
        }

        public void setVisible(boolean visible) {
            this.visible = visible;
        }

        @Override
        public String toString() {
            return description;
        }
    }

    class TaskCellRenderer extends JPanel implements ListCellRenderer<Task> {
        private JLabel taskLabel;

        public TaskCellRenderer() {
            setLayout(new BorderLayout());
            taskLabel = new JLabel();
            add(taskLabel, BorderLayout.CENTER);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Task> list, Task value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value.isVisible()) {
                taskLabel.setText(value.getDescription());
                taskLabel.setBackground(value.getColor());
                taskLabel.setOpaque(true);
            } else {
                taskLabel.setText("");
                taskLabel.setBackground(Color.WHITE);
                taskLabel.setOpaque(false);
            }

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            return this;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BacklogApp().setVisible(true);
            }
        });
    }
}
