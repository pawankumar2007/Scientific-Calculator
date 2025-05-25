import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Scientific Calculator Application
 * 
 * Features:
 * - Basic arithmetic operations (add, subtract, multiply, divide)
 * - Advanced scientific functions (sin, cos, tan, log, etc.)
 * - Memory functions
 * - Aesthetically pleasing UI with proper component placement
 * - Responsive design
 */
public class ScientificCalculator extends JFrame {
    
    // Constants
    private static final int WINDOW_WIDTH = 650;
    private static final int WINDOW_HEIGHT = 500;
    private static final Font DISPLAY_FONT = new Font("Arial", Font.BOLD, 20);
    private static final Font BUTTON_FONT = new Font("Arial", Font.PLAIN, 14);
    private static final Font MEMORY_BUTTON_FONT = new Font("Arial", Font.PLAIN, 12);
    
    // Colors for dark theme
    private static final Color BACKGROUND_COLOR = new Color(30, 30, 35);
    private static final Color BACKGROUND_COLOR_GRADIENT = new Color(45, 45, 55);
    private static final Color DISPLAY_BACKGROUND = new Color(20, 20, 25);
    private static final Color DISPLAY_TEXT_COLOR = new Color(255, 255, 255);
    private static final Color BUTTON_TEXT_COLOR = new Color(0, 0, 0);
    private static final Color NUMBER_BUTTON_COLOR = new Color(220, 220, 220);
    private static final Color OPERATOR_BUTTON_COLOR = new Color(180, 180, 240);
    private static final Color MEMORY_BUTTON_COLOR = new Color(160, 160, 210);
    private static final Color SCIENTIFIC_BUTTON_COLOR = new Color(180, 210, 240);
    private static final Color CLEAR_BUTTON_COLOR = new Color(240, 150, 150);
    
    // UI Components
    private JTextField displayField;
    private JPanel buttonPanel;
    private JPanel scientificPanel;
    private JPanel memoryPanel;
    private JLabel angleLabel;
    private JRadioButton degreeRadio;
    private JRadioButton radianRadio;
    private JLabel keyboardInputStatus;
    
    // Calculator state
    private double currentValue = 0;
    private String currentOperator = "";
    private boolean startNewInput = true;
    private boolean angleInDegrees = true;
    private double memoryValue = 0;
    private DecimalFormat df = new DecimalFormat("#.########");
    private boolean keyboardInputEnabled = true;
    
    /**
     * Constructor - initializes the calculator UI
     */
    public ScientificCalculator() {
        // Set up the frame
        super("Dark Theme Scientific Calculator");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setLocationRelativeTo(null);
        
        // Create a custom panel with gradient background
        JPanel mainPanel = new JPanel(new BorderLayout(5, 5)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, BACKGROUND_COLOR, 0, h, BACKGROUND_COLOR_GRADIENT);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.setOpaque(false);
        setContentPane(mainPanel);
        
        // Create display field with custom styling
        displayField = new JTextField("0") {
            @Override
            protected void paintComponent(Graphics g) {
                if (!isOpaque() && getBorder() instanceof EmptyBorder) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(DISPLAY_BACKGROUND);
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    g2d.dispose();
                }
                super.paintComponent(g);
            }
        };
        displayField.setFont(DISPLAY_FONT);
        displayField.setHorizontalAlignment(JTextField.RIGHT);
        displayField.setEditable(false);
        displayField.setOpaque(false);
        displayField.setBackground(new Color(0, 0, 0, 0));
        displayField.setForeground(DISPLAY_TEXT_COLOR);
        displayField.setCaretColor(DISPLAY_TEXT_COLOR);
        displayField.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(1, 1, 1, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        mainPanel.add(displayField, BorderLayout.NORTH);
        
        // Create center panel for all buttons
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.setOpaque(false);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Create angle mode panel
        JPanel angleModePanel = new JPanel();
        angleModePanel.setOpaque(false);
        angleLabel = new JLabel("Angle Mode:");
        angleLabel.setForeground(DISPLAY_TEXT_COLOR);
        
        // Custom radio buttons with modern look
        degreeRadio = new JRadioButton("Degrees", true);
        degreeRadio.setForeground(DISPLAY_TEXT_COLOR);
        degreeRadio.setOpaque(false);
        degreeRadio.setFocusPainted(false);
        
        radianRadio = new JRadioButton("Radians", false);
        radianRadio.setForeground(DISPLAY_TEXT_COLOR);
        radianRadio.setOpaque(false);
        radianRadio.setFocusPainted(false);
        ButtonGroup angleGroup = new ButtonGroup();
        angleGroup.add(degreeRadio);
        angleGroup.add(radianRadio);
        
        degreeRadio.addActionListener(e -> angleInDegrees = true);
        radianRadio.addActionListener(e -> angleInDegrees = false);
        
        angleModePanel.add(angleLabel);
        angleModePanel.add(degreeRadio);
        angleModePanel.add(radianRadio);
        
        centerPanel.add(angleModePanel, BorderLayout.NORTH);
        
        // Create memory panel
        createMemoryPanel();
        centerPanel.add(memoryPanel, BorderLayout.WEST);
        
        // Create main button panel
        JPanel calculatorPanel = new JPanel(new BorderLayout(5, 5));
        calculatorPanel.setOpaque(false);
        centerPanel.add(calculatorPanel, BorderLayout.CENTER);
        
        // Create scientific panel
        createScientificPanel();
        calculatorPanel.add(scientificPanel, BorderLayout.NORTH);
        
        // Create numeric button panel
        createButtonPanel();
        calculatorPanel.add(buttonPanel, BorderLayout.CENTER);
        
        // Create status panel for keyboard input
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setOpaque(false);
        keyboardInputStatus = new JLabel("Keyboard Input: Enabled (Press F6 to toggle)");
        keyboardInputStatus.setForeground(new Color(120, 200, 255));
        statusPanel.add(keyboardInputStatus);
        
        // Add checkbox for keyboard input
        JCheckBox keyboardToggle = new JCheckBox("Enable Keyboard Input", keyboardInputEnabled);
        keyboardToggle.setForeground(DISPLAY_TEXT_COLOR);
        keyboardToggle.setOpaque(false);
        keyboardToggle.addActionListener(e -> {
            keyboardInputEnabled = keyboardToggle.isSelected();
            updateKeyboardStatus();
        });
        statusPanel.add(keyboardToggle);
        
        mainPanel.add(statusPanel, BorderLayout.SOUTH);
        
        // Add keyboard listener
        addKeyboardListener();
    }
    
    /**
     * Updates the keyboard status label
     */
    private void updateKeyboardStatus() {
        keyboardInputStatus.setText("Keyboard Input: " + 
            (keyboardInputEnabled ? "Enabled" : "Disabled") + 
            " (Press F6 to toggle)");
    }
    
    /**
     * Creates the memory panel with memory operation buttons
     */
    private void createMemoryPanel() {
        memoryPanel = new JPanel(new GridLayout(5, 1, 8, 8));
        memoryPanel.setPreferredSize(new Dimension(120, 200));
        memoryPanel.setOpaque(false);
        
        // Create a custom titled border with white text
        Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
            emptyBorder, "Memory", TitledBorder.LEFT, TitledBorder.TOP, 
            new Font("Arial", Font.BOLD, 14), DISPLAY_TEXT_COLOR);
        memoryPanel.setBorder(titledBorder);
        
        String[] memoryButtons = {"Clear Mem", "Recall", "Store", "Add Mem", "Sub Mem"};
        
        for (String label : memoryButtons) {
            JButton button = createButton(label, MEMORY_BUTTON_COLOR);
            button.setFont(MEMORY_BUTTON_FONT);
            button.setForeground(BUTTON_TEXT_COLOR);
            memoryPanel.add(button);
        }
    }
    
    /**
     * Creates the scientific panel with advanced function buttons
     */
    private void createScientificPanel() {
        scientificPanel = new JPanel(new GridLayout(2, 6, 8, 8));
        scientificPanel.setOpaque(false);
        
        String[] scientificButtons = {
            "sin", "cos", "tan", "log", "ln", "√",
            "x²", "xʸ", "1/x", "π", "e", "abs"
        };
        
        for (String label : scientificButtons) {
            JButton button = createButton(label, SCIENTIFIC_BUTTON_COLOR);
            button.setForeground(BUTTON_TEXT_COLOR);
            scientificPanel.add(button);
        }
    }
    
    /**
     * Creates the main button panel with numeric and basic operation buttons
     */
    private void createButtonPanel() {
        buttonPanel = new JPanel(new GridLayout(5, 4, 8, 8));
        buttonPanel.setOpaque(false);
        
        String[][] buttonLabels = {
            {"7", "8", "9", "/"},
            {"4", "5", "6", "*"},
            {"1", "2", "3", "-"},
            {"0", ".", "=", "+"},
            {"AC", "CE", "±", "%"}
        };
        
        for (String[] row : buttonLabels) {
            for (String label : row) {
                Color buttonColor;
                
                if (label.matches("[0-9.]")) {
                    buttonColor = NUMBER_BUTTON_COLOR; // Light gray for numbers
                } else if (label.matches("[+\\-*/=]")) {
                    buttonColor = OPERATOR_BUTTON_COLOR; // Light purple for operators
                } else if (label.matches("AC|CE")) {
                    buttonColor = CLEAR_BUTTON_COLOR; // Light red for clear
                } else {
                    buttonColor = NUMBER_BUTTON_COLOR; // Default gray
                }
                
                JButton button = createButton(label, buttonColor);
                button.setForeground(BUTTON_TEXT_COLOR);
                buttonPanel.add(button);
            }
        }
    }
    
    /**
     * Creates a button with the specified label and color
     * 
     * @param label The button label
     * @param color The button background color
     * @return The created button
     */
    private JButton createButton(String label, Color color) {
        // Create a custom button with rounded corners
        JButton button = new JButton(label) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2d.setColor(color.darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(color.brighter());
                } else {
                    g2d.setColor(color);
                }
                
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                // Draw a subtle border
                g2d.setColor(new Color(0, 0, 0, 50));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                
                g2d.dispose();
                
                super.paintComponent(g);
            }
            
            @Override
            public Dimension getPreferredSize() {
                Dimension size = super.getPreferredSize();
                size.width = Math.max(size.width, 60);
                size.height = Math.max(size.height, 40);
                return size;
            }
        };
        
        button.setFont(BUTTON_FONT);
        button.setForeground(BUTTON_TEXT_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setMargin(new Insets(10, 10, 10, 10));
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.repaint();
            }
        });
        
        button.addActionListener(new ButtonClickListener());
        return button;
    }
    
    /**
     * Converts an angle to radians if needed based on the current angle mode
     * 
     * @param angle The angle to convert
     * @return The angle in radians
     */
    private double toRadians(double angle) {
        return angleInDegrees ? Math.toRadians(angle) : angle;
    }
    
    /**
     * Adds a keyboard listener to the frame
     */
    private void addKeyboardListener() {
        // Add key listener to the frame
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!keyboardInputEnabled) {
                    return;
                }
                
                int keyCode = e.getKeyCode();
                
                // Toggle keyboard input with F6
                if (keyCode == KeyEvent.VK_F6) {
                    keyboardInputEnabled = !keyboardInputEnabled;
                    updateKeyboardStatus();
                    return;
                }
                
                // Handle numeric keys and operators
                if (keyCode >= KeyEvent.VK_0 && keyCode <= KeyEvent.VK_9) {
                    // Number keys 0-9
                    processKeyboardInput(String.valueOf(keyCode - KeyEvent.VK_0));
                } else if (keyCode >= KeyEvent.VK_NUMPAD0 && keyCode <= KeyEvent.VK_NUMPAD9) {
                    // Numpad keys 0-9
                    processKeyboardInput(String.valueOf(keyCode - KeyEvent.VK_NUMPAD0));
                } else {
                    // Handle other keys
                    switch (keyCode) {
                        case KeyEvent.VK_PERIOD:
                        case KeyEvent.VK_DECIMAL:
                            processKeyboardInput(".");
                            break;
                        case KeyEvent.VK_PLUS:
                        case KeyEvent.VK_ADD:
                            processKeyboardInput("+");
                            break;
                        case KeyEvent.VK_MINUS:
                        case KeyEvent.VK_SUBTRACT:
                            processKeyboardInput("-");
                            break;
                        case KeyEvent.VK_ASTERISK:
                        case KeyEvent.VK_MULTIPLY:
                            processKeyboardInput("*");
                            break;
                        case KeyEvent.VK_SLASH:
                        case KeyEvent.VK_DIVIDE:
                            processKeyboardInput("/");
                            break;
                        case KeyEvent.VK_EQUALS:
                            if (e.isShiftDown()) {  // + is Shift+= on most keyboards
                                processKeyboardInput("+");
                            } else {
                                processKeyboardInput("=");
                            }
                            break;
                        case KeyEvent.VK_ENTER:
                            processKeyboardInput("=");
                            break;
                        case KeyEvent.VK_BACK_SPACE:
                            handleBackspace();
                            break;
                        case KeyEvent.VK_ESCAPE:
                            processKeyboardInput("AC");
                            break;
                        case KeyEvent.VK_DELETE:
                            processKeyboardInput("CE");
                            break;
                        case KeyEvent.VK_S:
                            handleScientificFunction("sin");
                            break;
                        case KeyEvent.VK_C:
                            handleScientificFunction("cos");
                            break;
                        case KeyEvent.VK_T:
                            handleScientificFunction("tan");
                            break;
                        case KeyEvent.VK_L:
                            handleScientificFunction("log");
                            break;
                        case KeyEvent.VK_N:
                            handleScientificFunction("ln");
                            break;
                        case KeyEvent.VK_R:
                            handleScientificFunction("√");
                            break;
                        case KeyEvent.VK_P:
                            handleScientificFunction("π");
                            break;
                        case KeyEvent.VK_E:
                            handleScientificFunction("e");
                            break;
                        case KeyEvent.VK_5:
                            if (e.isShiftDown()) {  // % is Shift+5 on most keyboards
                                processKeyboardInput("%");
                            } else {
                                processKeyboardInput("5");
                            }
                            break;
                        case KeyEvent.VK_8:
                            if (e.isShiftDown()) {  // * is Shift+8 on most keyboards
                                processKeyboardInput("*");
                            } else {
                                processKeyboardInput("8");
                            }
                            break;
                        // Note: VK_EQUALS is already handled above with VK_ENTER
                    }
                }
            }
        });
        
        // Make sure the frame can receive key events
        setFocusable(true);
        requestFocus();
    }
    
    /**
     * Process keyboard input by simulating button clicks
     * 
     * @param buttonText The button text to simulate
     */
    private void processKeyboardInput(String buttonText) {
        // Find the button with the given text
        for (Component component : buttonPanel.getComponents()) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                if (button.getText().equals(buttonText)) {
                    // Simulate button click
                    button.doClick();
                    return;
                }
            }
        }
        
        // Check scientific panel if not found in button panel
        for (Component component : scientificPanel.getComponents()) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                if (button.getText().equals(buttonText)) {
                    // Simulate button click
                    button.doClick();
                    return;
                }
            }
        }
        
        // Check memory panel if not found in scientific panel
        for (Component component : memoryPanel.getComponents()) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                if (button.getText().equals(buttonText)) {
                    // Simulate button click
                    button.doClick();
                    return;
                }
            }
        }
    }
    
    /**
     * Handle scientific function keys directly
     * 
     * @param functionName The name of the scientific function
     */
    private void handleScientificFunction(String functionName) {
        try {
            // If the display shows a calculation result, extract the result value
            String displayText = displayField.getText();
            if (displayText.contains(" = ")) {
                String[] parts = displayText.split(" = ");
                if (parts.length > 1) {
                    displayText = parts[1];
                }
            }
            
            // For constants like π and e, we don't need an input value
            if (functionName.equals("π")) {
                double result = Math.PI;
                displayField.setText(formatNumber(result));
                currentValue = result;
                startNewInput = true;
                return;
            } else if (functionName.equals("e")) {
                double result = Math.E;
                displayField.setText(formatNumber(result));
                currentValue = result;
                startNewInput = true;
                return;
            }
            
            // For other functions, we need an input value
            double value = Double.parseDouble(displayText);
            double result = 0;
            boolean validFunction = true;
            
            // Calculate the result based on the function name
            switch (functionName) {
                case "sin":
                    result = Math.sin(toRadians(value));
                    break;
                case "cos":
                    result = Math.cos(toRadians(value));
                    break;
                case "tan":
                    result = Math.tan(toRadians(value));
                    break;
                case "log":
                    if (value <= 0) throw new ArithmeticException("Log of non-positive number");
                    result = Math.log10(value);
                    break;
                case "ln":
                    if (value <= 0) throw new ArithmeticException("Log of non-positive number");
                    result = Math.log(value);
                    break;
                case "√":
                    if (value < 0) throw new ArithmeticException("Square root of negative number");
                    result = Math.sqrt(value);
                    break;
                case "x²":
                    result = value * value;
                    break;
                case "1/x":
                    if (value == 0) throw new ArithmeticException("Division by zero");
                    result = 1 / value;
                    break;
                case "abs":
                    result = Math.abs(value);
                    break;
                default:
                    validFunction = false;
                    break;
            }
            
            if (validFunction) {
                // Just show the result, not the function name
                displayField.setText(formatNumber(result));
                currentValue = result;
                startNewInput = true;
            }
        } catch (Exception ex) {
            displayField.setText("Error");
            startNewInput = true;
        }
    }
    
    /**
     * Handle backspace key by removing the last character from the display
     */
    private void handleBackspace() {
        if (startNewInput) {
            return;
        }
        
        String currentText = displayField.getText();
        if (currentText.length() > 0) {
            // Remove the last character
            String newText = currentText.substring(0, currentText.length() - 1);
            if (newText.isEmpty() || newText.equals("-")) {
                displayField.setText("0");
                startNewInput = true;
            } else {
                displayField.setText(newText);
            }
        }
    }
    
    /**
     * Formats a number for display
     * 
     * @param number The number to format
     * @return The formatted number as a string
     */
    private String formatNumber(double number) {
        // Handle special cases
        if (Double.isNaN(number)) return "Error";
        if (Double.isInfinite(number)) return "Infinity";
        
        // Format the number
        String result = df.format(number);
        
        // Remove trailing zeros after decimal point
        if (result.contains(".")) {
            result = result.replaceAll("0*$", "").replaceAll("\\.$", "");
        }
        
        return result;
    }
    
    /**
     * Button click listener for all calculator buttons
     */
    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            String buttonText = source.getText();
            
            try {
                // Handle different button types
                if (buttonText.matches("[0-9.]")) {
                    handleNumberButton(buttonText);
                } else if (buttonText.matches("[+\\-*/=]")) {
                    handleOperatorButton(buttonText);
                } else if (buttonText.matches("Clear Mem|Recall|Store|Add Mem|Sub Mem")) {
                    handleMemoryButton(buttonText);
                } else {
                    handleFunctionButton(buttonText);
                }
            } catch (Exception ex) {
                displayField.setText("Error");
                startNewInput = true;
            }
        }
        
        /**
         * Handles number button clicks (0-9 and decimal point)
         * 
         * @param buttonText The button text
         */
        private void handleNumberButton(String buttonText) {
            // If the display shows a calculation result, start fresh
            if (displayField.getText().contains(" = ")) {
                displayField.setText(buttonText);
                startNewInput = false;
                return;
            }
            
            if (startNewInput) {
                displayField.setText(buttonText);
                startNewInput = false;
            } else {
                String currentText = displayField.getText();
                
                // Handle decimal point
                if (buttonText.equals(".") && currentText.contains(".")) {
                    return;
                }
                
                displayField.setText(currentText + buttonText);
            }
        }
        
        /**
         * Handles operator button clicks (+, -, *, /, =)
         * 
         * @param buttonText The button text
         */
        private void handleOperatorButton(String buttonText) {
            String displayText = displayField.getText();
            
            // If the display shows a calculation result, extract the result value
            if (displayText.contains(" = ")) {
                String[] parts = displayText.split(" = ");
                if (parts.length > 1) {
                    displayText = parts[1];
                    displayField.setText(displayText);
                }
            }
            
            if (!startNewInput) {
                calculate();
            }
            
            if (!buttonText.equals("=")) {
                currentOperator = buttonText;
                
                // Show the operator in the display
                if (displayText.contains(" = ")) {
                    // Extract the result part
                    String[] parts = displayText.split(" = ");
                    if (parts.length > 1) {
                        displayField.setText(parts[1] + " " + getOperatorSymbol(buttonText) + " ");
                    }
                } else {
                    // Just append the operator
                    displayField.setText(displayField.getText() + " " + getOperatorSymbol(buttonText) + " ");
                }
                
                startNewInput = true;
            } else {
                currentOperator = "";
            }
        }
        
        /**
         * Gets a display-friendly symbol for an operator
         * 
         * @param operator The operator
         * @return The display symbol
         */
        private String getOperatorSymbol(String operator) {
            switch (operator) {
                case "*": return "×";
                case "/": return "÷";
                default: return operator;
            }
        }
        
        /**
         * Handles memory button clicks (Clear Mem, Recall, Store, Add Mem, Sub Mem)
         * 
         * @param buttonText The button text
         */
        private void handleMemoryButton(String buttonText) {
            switch (buttonText) {
                case "Clear Mem": // Memory Clear
                    memoryValue = 0;
                    break;
                    
                case "Recall": // Memory Recall
                    displayField.setText(formatNumber(memoryValue));
                    startNewInput = true;
                    break;
                    
                case "Store": // Memory Store
                    memoryValue = Double.parseDouble(displayField.getText());
                    startNewInput = true;
                    break;
                    
                case "Add Mem": // Memory Add
                    memoryValue += Double.parseDouble(displayField.getText());
                    startNewInput = true;
                    break;
                    
                case "Sub Mem": // Memory Subtract
                    memoryValue -= Double.parseDouble(displayField.getText());
                    startNewInput = true;
                    break;
            }
        }
        
        /**
         * Handles function button clicks (sin, cos, etc.)
         * 
         * @param buttonText The button text
         */
        private void handleFunctionButton(String buttonText) {
            // If the display shows a calculation result, extract the result value
            String displayText = displayField.getText();
            if (displayText.contains(" = ")) {
                String[] parts = displayText.split(" = ");
                if (parts.length > 1) {
                    displayText = parts[1];
                    displayField.setText(displayText);
                }
            }
            
            double value;
            
            switch (buttonText) {
                case "AC": // All Clear
                    displayField.setText("0");
                    currentValue = 0;
                    currentOperator = "";
                    startNewInput = true;
                    break;
                    
                case "CE": // Clear entry
                    displayField.setText("0");
                    startNewInput = true;
                    break;
                    
                case "±": // Change sign
                    value = Double.parseDouble(displayField.getText());
                    displayField.setText(formatNumber(-value));
                    break;
                    
                case "%": // Percentage
                    value = Double.parseDouble(displayField.getText());
                    displayField.setText(formatNumber(value / 100));
                    startNewInput = true;
                    break;
                    
                case "sin": // Sine
                case "cos": // Cosine
                case "tan": // Tangent
                case "log": // Log base 10
                case "ln": // Natural log
                case "√": // Square root
                case "x²": // Square
                case "1/x": // Reciprocal
                case "abs": // Absolute value
                case "π": // Pi
                case "e": // Euler's number
                    // Use the handleScientificFunction method for all scientific functions
                    handleScientificFunction(buttonText);
                    break;
                    
                case "xʸ": // Power
                    if (!startNewInput) {
                        currentValue = Double.parseDouble(displayField.getText());
                        currentOperator = "^";
                        displayField.setText(formatNumber(currentValue) + " ^ ");
                        startNewInput = true;
                    }
                    break;
                    
                // These cases are already handled in the scientific functions section above
            }
        }
        
        /**
         * Calculates the result of the current operation
         */
        private void calculate() {
            // If the display already shows a calculation result, do nothing
            if (displayField.getText().contains(" = ")) {
                return;
            }
            
            String displayText = displayField.getText();
            double secondValue;
            
            // Check if we're in the middle of entering a power operation
            if (currentOperator.equals("^") && displayText.contains(" ^ ")) {
                String[] parts = displayText.split(" \\^ ");
                if (parts.length > 1 && !parts[1].isEmpty()) {
                    secondValue = Double.parseDouble(parts[1]);
                } else {
                    return; // Not enough information to calculate
                }
            } else {
                // Normal operation
                secondValue = Double.parseDouble(displayText);
            }
            
            double result = 0;
            String expression = "";
            
            switch (currentOperator) {
                case "+":
                    result = currentValue + secondValue;
                    expression = formatNumber(currentValue) + " + " + formatNumber(secondValue);
                    break;
                    
                case "-":
                    result = currentValue - secondValue;
                    expression = formatNumber(currentValue) + " - " + formatNumber(secondValue);
                    break;
                    
                case "*":
                    result = currentValue * secondValue;
                    expression = formatNumber(currentValue) + " × " + formatNumber(secondValue);
                    break;
                    
                case "/":
                    if (secondValue == 0) throw new ArithmeticException("Division by zero");
                    result = currentValue / secondValue;
                    expression = formatNumber(currentValue) + " ÷ " + formatNumber(secondValue);
                    break;
                    
                case "^":
                    result = Math.pow(currentValue, secondValue);
                    expression = formatNumber(currentValue) + " ^ " + formatNumber(secondValue);
                    break;
                    
                default:
                    result = secondValue;
                    expression = formatNumber(secondValue);
                    break;
            }
            
            // Display the full expression and result
            displayField.setText(expression + " = " + formatNumber(result));
            currentValue = result;
            startNewInput = true;
        }
    }
    
    /**
     * Main method to start the application
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Set look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Create and show the calculator
        SwingUtilities.invokeLater(() -> {
            ScientificCalculator calculator = new ScientificCalculator();
            calculator.setVisible(true);
            
            // Request focus after the calculator is visible
            calculator.requestFocusInWindow();
            
            // Add a help message dialog
            JOptionPane.showMessageDialog(calculator,
                "Keyboard Shortcuts:\n" +
                "- Numbers: 0-9 keys or numpad\n" +
                "- Operators: +, -, *, /, =\n" +
                "- Enter: Calculate result\n" +
                "- Backspace: Delete last digit\n" +
                "- Escape: All Clear (AC)\n" +
                "- Delete: Clear entry\n" +
                "- F6: Toggle keyboard input\n" +
                "- S: Sine\n" +
                "- C: Cosine\n" +
                "- T: Tangent\n" +
                "- L: Log (base 10)\n" +
                "- N: Natural Log\n" +
                "- R: Square Root\n" +
                "- P: Pi\n" +
                "- E: Euler's number\n" +
                "- Shift+5: Percentage",
                "Keyboard Shortcuts",
                JOptionPane.INFORMATION_MESSAGE
            );
        });
    }
}