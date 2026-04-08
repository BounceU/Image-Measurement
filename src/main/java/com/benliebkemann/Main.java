package com.benliebkemann;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class Main extends JPanel implements MouseListener, MouseMotionListener {

    BufferedImage image;
    List<Line> lines;

    int lineSize;

    Line baseLine;
    String units;
    double baseLength = 0.0;

    int mx, my;

    int lineX, lineY;

    private class Line {

        double x1, y1, x2, y2;
        double length;

        public Line(double x1, double y1, double x2, double y2, double length) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.length = length;
        }

        public void draw(Color c, int thickness, Graphics g) {

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setStroke(new BasicStroke(thickness));
            g2d.setColor(c);
            g.drawLine((int) (x1 * getWidth()), (int) (y1 * getHeight()), (int) (x2 * getWidth()),
                    (int) (y2 * getHeight()));

            if (length >= 0) {
                String text = String.format("%.02f %s", length, units);
                Font customFont = new Font("Arial", Font.BOLD, thickness + 5);
                g2d.setFont(customFont);

                FontMetrics metrics = g2d.getFontMetrics(customFont);

                int width = metrics.stringWidth(text);
                int height = metrics.getHeight();
                int ascent = metrics.getAscent();

                int textX = (int) (x2 * getWidth()) + 5;
                int textY = (int) (y2 * getHeight()) - 5;

                g.setColor(Color.white);
                g2d.fillRect(textX, textY - ascent, width, height);
                g2d.setColor(c);
                g2d.drawString(text, textX, textY);

            }
        }

    }

    public Main(JFrame frame) {
        this.lines = new ArrayList<Line>();
        this.image = null;
        this.lineSize = 10;
        this.mx = 0;
        this.my = 0;
        this.lineX = -1;
        this.lineY = -1;
        this.baseLine = null;
        while (this.image == null) {
            FileDialog fileDialog = new FileDialog(frame, "Select an Image", FileDialog.LOAD);
            fileDialog.setFile("*.jpg;*.jpeg;*.png;*.gif");
            fileDialog.setVisible(true);

            String directory = fileDialog.getDirectory();
            String filename = fileDialog.getFile();

            if (directory != null && filename != null) {
                File selectedFile = new File(directory, filename);
                System.out.println("Got file " + selectedFile.getAbsolutePath());
                try {
                    this.image = ImageIO.read(selectedFile);
                } catch (IOException ioe) {
                    JOptionPane.showMessageDialog(null, "Please select an image", "Error", JOptionPane.ERROR_MESSAGE);
                    this.image = null;
                }
            } else {
                System.exit(0);
            }
        }

    }

    @Override
    public Dimension getPreferredSize() {
        Container parent = getParent();

        double targetAspectRatio = image.getWidth() * 1.0 / image.getHeight();

        // Fallback for the initial load before the parent has a width/height on screen
        if (parent == null || parent.getWidth() == 0 || parent.getHeight() == 0) {
            return new Dimension(400, (int) (400 / targetAspectRatio));
        }

        int parentWidth = parent.getWidth();
        int parentHeight = parent.getHeight();

        // Start by assuming we will match the parent's full width,
        // and calculate the required height to maintain the ratio.
        int targetWidth = parentWidth;
        int targetHeight = (int) (targetWidth / targetAspectRatio);

        // If the calculated height is taller than what the parent allows,
        // we must instead match the parent's full height and calculate the width.
        if (targetHeight > parentHeight) {
            targetHeight = parentHeight;
            targetWidth = (int) (targetHeight * targetAspectRatio);
        }

        return new Dimension(targetWidth, targetHeight);
    }

    public void reset(JFrame frame) {
        this.image = null;
        this.lineSize = 10;
        this.mx = 0;
        this.my = 0;
        this.lineX = -1;
        this.lineY = -1;
        this.baseLine = null;
        this.lines = new ArrayList<Line>();
        while (this.image == null) {
            FileDialog fileDialog = new FileDialog(frame, "Select an Image", FileDialog.LOAD);
            fileDialog.setFile("*.jpg;*.jpeg;*.png;*.gif");
            fileDialog.setVisible(true);

            String directory = fileDialog.getDirectory();
            String filename = fileDialog.getFile();

            if (directory != null && filename != null) {
                File selectedFile = new File(directory, filename);
                System.out.println("Got file " + selectedFile.getAbsolutePath());
                try {
                    this.image = ImageIO.read(selectedFile);
                } catch (IOException ioe) {
                    JOptionPane.showMessageDialog(null, "Please select an image", "Error", JOptionPane.ERROR_MESSAGE);
                    this.image = null;
                }
            } else {
                System.exit(0);
            }
        }
    }

    public void clearLines() {
        this.lines = new ArrayList<Line>();
        this.baseLine = null;
    }

    public void saveImage(JFrame frame) {
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        this.paint(g2d);

        FileDialog fileDialog = new FileDialog(frame, "Save Image", FileDialog.SAVE);
        fileDialog.setFile("save.jpg");

        fileDialog.setVisible(true);

        String directory = fileDialog.getDirectory();
        String filename = fileDialog.getFile();

        if (directory != null && filename != null) {
            if (!filename.toLowerCase().endsWith(".jpg") && !filename.toLowerCase().endsWith(".jpeg")) {
                filename += ".jpg";
            }
            File outputFile = new File(directory, filename);

            try {

                boolean success = ImageIO.write(image, "jpg", outputFile);

                if (success) {
                    System.out.println("Image successfully saved to: " + outputFile.getAbsolutePath());
                } else {
                    System.err.println("No appropriate writer found for JPEG format.");
                }

            } catch (IOException e) {
                System.err.println("Failed to save the image. Error: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Save operation was cancelled by the user.");
        }

        g2d.dispose();

    }

    public void click() {
        if (this.lineX == -1 && this.lineY == -1) {
            this.lineX = this.mx;
            this.lineY = this.my;
        } else {

            double x1 = 1.0 * this.lineX / this.getWidth();
            double y1 = 1.0 * this.lineY / this.getHeight();
            double x2 = 1.0 * this.mx / this.getWidth();
            double y2 = 1.0 * this.my / this.getHeight();

            double length = 2.0;

            if (this.baseLine == null) {
                double validNumber = 0;
                String chosenUnit = "";
                boolean isValid = false;

                while (!isValid) {
                    JTextField numberField = new JTextField(10);

                    Object[] message = { "Please enter a number:", numberField };

                    Object[] options = { "Inches", "Centimeters" };

                    int buttonPressed = JOptionPane.showOptionDialog(null, message, "Unit Selector",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                    if (buttonPressed == JOptionPane.CLOSED_OPTION) {
                        JOptionPane.showMessageDialog(null, "Please enter a number and choose a unit", "Input Required",
                                JOptionPane.WARNING_MESSAGE);
                        continue;
                    }

                    try {
                        length = Double.parseDouble(numberField.getText());

                        units = (buttonPressed == 0) ? "in" : "cm";

                        isValid = true;

                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null,
                                "Invalid input. Please enter a valid number (e.g., 10 or 5.5).", "Invalid Number",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                length = baseLine.length;
            }

            if (this.baseLine != null) {
                double newDist = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
                double baseDist = Math.sqrt((baseLine.x1 - baseLine.x2) * (baseLine.x1 - baseLine.x2)
                        + (baseLine.y1 - baseLine.y2) * (baseLine.y1 - baseLine.y2));
                length *= newDist / baseDist;
            }

            Line newLine = new Line(x1, y1, x2, y2, length);

            if (this.baseLine == null) {
                this.baseLine = newLine;
            } else {
                this.lines.add(newLine);
            }

            this.lineX = -1;
            this.lineY = -1;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);

        if (this.baseLine != null) {
            this.baseLine.draw(Color.green, lineSize, g);
        }

        for (Line line : lines) {
            line.draw(Color.red, lineSize, g);
        }

        if (this.lineX != -1 && this.lineY != -1) {
            if (this.baseLine == null) {
                Line tempLine = new Line(1.0 * this.lineX / this.getWidth(), 1.0 * this.lineY / this.getHeight(),
                        1.0 * this.mx / this.getWidth(), 1.0 * this.my / this.getHeight(), -1.0);
                tempLine.draw(Color.green, lineSize, g);
            } else {

                double x1 = 1.0 * this.lineX / this.getWidth();
                double y1 = 1.0 * this.lineY / this.getHeight();
                double x2 = 1.0 * this.mx / this.getWidth();
                double y2 = 1.0 * this.my / this.getHeight();
                double newDist = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
                double baseDist = Math.sqrt((baseLine.x1 - baseLine.x2) * (baseLine.x1 - baseLine.x2)
                        + (baseLine.y1 - baseLine.y2) * (baseLine.y1 - baseLine.y2));
                double length = baseLine.length * newDist / baseDist;
                Line tempLine = new Line(x1, y1, x2, y2, length);
                tempLine.draw(Color.red, lineSize, g);
            }
        } else {
            if (this.baseLine == null) {
                g.setColor(Color.green);
            } else {
                g.setColor(Color.red);
            }
            g.fillOval(this.mx - lineSize / 2, this.my - lineSize / 2, lineSize, lineSize);
        }

    }

    public static void main(String[] args) {
        System.out.println("Launching");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Failed to set native look and feel. Falling back to default.");
        }

        JFrame window = new JFrame("Measurment");

        Main m = new Main(window);
        m.setFocusable(true);

        m.addMouseMotionListener(m);
        m.addMouseListener(m);

        JPanel buttonContainer = new JPanel();
        JButton saveFile = new JButton("Save");
        saveFile.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                m.saveImage(window);
                window.repaint();
            }

        });
        JButton clearLines = new JButton("Clear");
        clearLines.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                m.clearLines();
                window.repaint();
            }

        });
        JButton newImage = new JButton("New Image");
        newImage.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                m.reset(window);
                window.repaint();
            }

        });
        JButton sizeUp = new JButton("Size Up");
        sizeUp.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                m.lineSize++;
                window.repaint();
            }

        });
        JButton sizeDown = new JButton("Size Down");
        sizeDown.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                m.lineSize--;
                window.repaint();
            }

        });

        buttonContainer.add(saveFile);
        buttonContainer.add(clearLines);
        buttonContainer.add(newImage);
        buttonContainer.add(sizeUp);
        buttonContainer.add(sizeDown);

        JPanel wrapper = new JPanel(new GridBagLayout());

        window.add(buttonContainer, BorderLayout.NORTH);
        window.add(m, BorderLayout.CENTER);

        window.pack();

        window.setSize(800, 800);
        window.setLocationRelativeTo(null);

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        window.setVisible(true);

    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        this.mx = e.getX();
        this.my = e.getY();
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Click");
        this.mx = e.getX();
        this.my = e.getY();
        this.click();
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }
}