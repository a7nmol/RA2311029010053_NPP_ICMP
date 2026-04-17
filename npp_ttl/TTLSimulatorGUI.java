import javax.swing.*;
import java.awt.*;
import java.util.regex.Pattern;

/**
 * Enhanced TTL Simulator with better UI, structured code, and visual
 * connections.
 */
public class TTLSimulatorGUI extends JFrame {

    private JTextArea outputArea;
    private JButton simulateBtn;
    private NetworkPanel animationPanel;
    private JTextField sourceField, destField, ttlField;

    private int currentHop = -1;
    private boolean isDropped = false;
    private boolean isReached = false;

    private final String[] routers = {
            "192.168.1.254", "10.0.0.1", "10.0.0.2", "10.0.0.3", "10.0.0.4", "10.0.0.5"
    };

    public TTLSimulatorGUI() {
        super("Network Protocol Simulator: TTL & ICMP");
        initializeUI();
    }

    private void initializeUI() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // --- TOP CONTROL PANEL ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        topPanel.setBorder(BorderFactory.createEtchedBorder());

        sourceField = new JTextField("192.168.1.1", 10);
        destField = new JTextField("10.0.0.4", 10);
        ttlField = new JTextField("3", 5);
        simulateBtn = new JButton("Start Simulation");
        simulateBtn.setBackground(new Color(46, 204, 113));
        simulateBtn.setForeground(Color.WHITE);
        simulateBtn.setFocusPainted(false);

        topPanel.add(new JLabel("Source IP:"));
        topPanel.add(sourceField);
        topPanel.add(new JLabel("Destination IP:"));
        topPanel.add(destField);
        topPanel.add(new JLabel("TTL (Hops):"));
        topPanel.add(ttlField);
        topPanel.add(simulateBtn);

        // --- VISUALIZATION PANEL ---
        animationPanel = new NetworkPanel();
        animationPanel.setBackground(Color.WHITE);

        // --- OUTPUT LOG PANEL ---
        outputArea = new JTextArea();
        outputArea.setFont(new Font("Monospaced", Font.BOLD, 13));
        outputArea.setEditable(false);
        outputArea.setBackground(new Color(30, 30, 30));
        outputArea.setForeground(new Color(0, 255, 65)); // Matrix Green
        outputArea.setMargin(new java.awt.Insets(10, 10, 10, 10));

        JScrollPane scroll = new JScrollPane(outputArea);
        scroll.setPreferredSize(new Dimension(450, 0));
        scroll.setBorder(BorderFactory.createTitledBorder(null, "PACKET LOG", 0, 0, null, Color.GRAY));

        // --- LAYOUT ASSEMBLY ---
        add(topPanel, BorderLayout.NORTH);
        add(animationPanel, BorderLayout.CENTER);
        add(scroll, BorderLayout.EAST);

        simulateBtn.addActionListener(e -> startSimulation());
    }

    private void startSimulation() {
        try {
            String src = sourceField.getText();
            String dest = destField.getText();
            int ttl = Integer.parseInt(ttlField.getText());

            if (!isValidIP(src) || !isValidIP(dest)) {
                JOptionPane.showMessageDialog(this, "Please enter valid IP addresses.");
                return;
            }

            simulateBtn.setEnabled(false);
            resetState();

            new Thread(() -> {
                runPacketLogic(src, dest, ttl);
                SwingUtilities.invokeLater(() -> simulateBtn.setEnabled(true));
            }).start();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "TTL must be a whole number.");
        }
    }

    private void resetState() {
        currentHop = -1;
        isDropped = false;
        isReached = false;
        outputArea.setText("");
        animationPanel.repaint();
    }

    private void runPacketLogic(String src, String dest, int ttl) {
        log(">>> NEW PACKET INITIALIZED");
        log("SRC: " + src + " | DST: " + dest + " | TTL: " + ttl);
        log("------------------------------------------");

        for (int i = 0; i < routers.length; i++) {
            currentHop = i;
            animationPanel.repaint();

            log("HOP " + (i + 1) + ": Arrived at " + routers[i]);

            // 1. Check if destination reached
            if (routers[i].equals(dest)) {
                isReached = true;
                log("\n[SUCCESS] Destination reached!");
                log("Packet accepted by host.");
                animationPanel.repaint();
                return;
            }

            // 2. Decrement TTL (Forwarding Logic)
            ttl--;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }

            if (ttl <= 0) {
                isDropped = true;
                log("\n[ERROR] TTL Expired at " + routers[i]);
                log("ICMP Type 11 (Time Exceeded) sent to " + src);
                animationPanel.repaint();
                return;
            }

            log("TTL decremented to " + ttl + ". Forwarding...");
            log("");
        }

        log("\n[WARNING] Routing table end reached. Destination unreachable.");
    }

    private void log(String msg) {
        SwingUtilities.invokeLater(() -> {
            outputArea.append(msg + "\n");
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
        });
    }

    private boolean isValidIP(String ip) {
        String zeroTo255 = "(\\d{1,2}|(0|1)\\d{2}|2[0-4]\\d|25[0-5])";
        String regex = zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255;
        return Pattern.matches(regex, ip);
    }

    // --- INNER CLASS FOR CUSTOM DRAWING ---
    private class NetworkPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int startX = 100;
            int y = getHeight() / 2 - 25;
            int gap = 150;

            // Draw Connections First
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.LIGHT_GRAY);
            for (int i = 0; i < routers.length - 1; i++) {
                g2.drawLine(startX + i * gap + 50, y + 25, startX + (i + 1) * gap, y + 25);
            }

            // Draw Routers
            for (int i = 0; i < routers.length; i++) {
                int x = startX + i * gap;

                // Determine Color
                if (i == currentHop) {
                    if (isDropped)
                        g2.setColor(new Color(231, 76, 60)); // Red
                    else if (isReached)
                        g2.setColor(new Color(46, 204, 113)); // Green
                    else
                        g2.setColor(new Color(52, 152, 219)); // Blue (Active)
                } else {
                    g2.setColor(new Color(189, 195, 199)); // Grey (Idle)
                }

                g2.fillOval(x, y, 60, 60);
                g2.setColor(Color.DARK_GRAY);
                g2.drawOval(x, y, 60, 60);

                // Labels
                g2.setFont(new Font("SansSerif", Font.BOLD, 12));
                g2.drawString("Router " + (i + 1), x + 5, y - 10);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
                g2.drawString(routers[i], x - 10, y + 80);

                // Packet Icon
                if (i == currentHop && !isDropped) {
                    g2.setColor(Color.YELLOW);
                    g2.fillRoundRect(x + 20, y + 20, 20, 15, 5, 5);
                    g2.setColor(Color.BLACK);
                    g2.drawRoundRect(x + 20, y + 20, 20, 15, 5, 5);
                }
            }
        }
    }

    public static void main(String[] args) {
        // Set System Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        SwingUtilities.invokeLater(() -> new TTLSimulatorGUI().setVisible(true));
    }
}