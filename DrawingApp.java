import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;


public class DrawingApp {

    public static class InteractiveDrawingCanvas extends JFrame {
    
        // Swing components
        private JComboBox<String> shapeSelector;
        private JButton clearButton;
        private JLabel instructionsLabel;
        private DrawingPanel drawingPanel;
    
        // List to store the drawn shapes
        private ArrayList<ShapeItem> shapes;
    
        /**
         * Constructor for InteractiveDrawingCanvas.
         * Sets up the GUI components and event listeners.
         */
        public InteractiveDrawingCanvas() {
            super("Interactive Drawing Canvas");
            shapes = new ArrayList<>();
    
            // Initialize Swing components
            shapeSelector = new JComboBox<>(new String[] {"Line", "Rectangle", "Oval"});
            clearButton = new JButton("Clear Canvas");
            instructionsLabel = new JLabel("Select any shape and draw by dragging mouse on canvas.");
    
            // Setup control panel and add components
            JPanel controlPanel = new JPanel();
            controlPanel.add(instructionsLabel);
            controlPanel.add(shapeSelector);
            controlPanel.add(clearButton);
    
            // Initialize the drawing panel and set its background color
            drawingPanel = new DrawingPanel();
            drawingPanel.setBackground(Color.WHITE);
    
            // Attach an action listener to the clear button to clear the canvas
            clearButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    shapes.clear();      // Remove all shapes from the list
                    drawingPanel.repaint();  // Repaint the panel to reflect changes
                }
            });
    
            // Set up the main frame layout
            this.setLayout(new BorderLayout());
            this.add(controlPanel, BorderLayout.NORTH);
            this.add(drawingPanel, BorderLayout.CENTER);
    
            // Configure the main frame properties
            this.setSize(800, 600);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setVisible(true);
        }
    
        /**
         * DrawingPanel is a custom JPanel that acts as a canvas for drawing shapes.
         * It listens for mouse events to capture drawing coordinates and uses the
         * Graphics2D API to render the shapes.
         */
        class DrawingPanel extends JPanel {
            private int startX, startY, currentX, currentY;
            private boolean drawing;
    
            /**
             * Constructor for DrawingPanel.
             * Sets up mouse listeners for drawing.
             */
            public DrawingPanel() {
                drawing = false;
    
                // Mouse listener to detect press and release events
                addMouseListener(new MouseAdapter() {
                    public void mousePressed(MouseEvent e) {
                        // Record the starting point when mouse is pressed
                        startX = e.getX();
                        startY = e.getY();
                        drawing = true;
                    }
    
                    public void mouseReleased(MouseEvent e) {
                        if (drawing) {
                            // Record the end point when mouse is released
                            currentX = e.getX();
                            currentY = e.getY();
                            // Get the current shape type from the combo box
                            String shapeType = (String) shapeSelector.getSelectedItem();
                            // Add the completed shape to the list
                            shapes.add(new ShapeItem(shapeType, startX, startY, currentX, currentY));
                            drawing = false;
                            repaint();
                        }
                    }
                });
    
                // Mouse motion listener to update the shape while dragging
                addMouseMotionListener(new MouseMotionAdapter() {
                    public void mouseDragged(MouseEvent e) {
                        if (drawing) {
                            currentX = e.getX();
                            currentY = e.getY();
                            repaint();
                        }
                    }
                });
            }
    
            /**
             * Overrides the paintComponent method to draw all shapes on the canvas.
             * Also draws the shape that is currently being drawn.
             */
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
    
                // Draw all shapes that have been completed
                for (ShapeItem shape : shapes) {
                    shape.draw(g2);
                }
    
                // Draw the shape currently being drawn (if any)
                if (drawing) {
                    String shapeType = (String) shapeSelector.getSelectedItem();
                    ShapeItem currentShape = new ShapeItem(shapeType, startX, startY, currentX, currentY);
                    currentShape.draw(g2);
                }
            }
        }
    
        /**
         * ShapeItem represents a shape drawn on the canvas.
         * It stores the type and coordinates, and contains a method to draw itself.
         */
        class ShapeItem {
            private String type;
            private int x1, y1, x2, y2;
    
            /**
             * Constructor for ShapeItem.
             */
            public ShapeItem(String type, int x1, int y1, int x2, int y2) {
                this.type = type;
                this.x1 = x1;
                this.y1 = y1;
                this.x2 = x2;
                this.y2 = y2;
            }
    
            /**
             * Draws the shape using the provided Graphics2D object.
             */
            public void draw(Graphics2D g2) {
                if ("Rectangle".equals(type)) {
                    // Calculate top-left corner and dimensions
                    int x = Math.min(x1, x2);
                    int y = Math.min(y1, y2);
                    int width = Math.abs(x2 - x1);
                    int height = Math.abs(y2 - y1);
                    g2.drawRect(x, y, width, height);
                } else if ("Oval".equals(type)) {
                    // Calculate top-left corner and dimensions
                    int x = Math.min(x1, x2);
                    int y = Math.min(y1, y2);
                    int width = Math.abs(x2 - x1);
                    int height = Math.abs(y2 - y1);
                    g2.drawOval(x, y, width, height);
                } else if ("Line".equals(type)) {
                    // Draw a simple line from the starting to ending point
                    g2.drawLine(x1, y1, x2, y2);
                }
            }
        }
    }
    //Simple main method
    public static void main(String[] args) {
        new InteractiveDrawingCanvas();
    }
}
