package spooktacular.app;

import spooktacular.game.Tabs;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/** SpookyApp — Java expression of UltimateContentView + ContentView.swift app shell.
 *  7 tabs mirroring iOS: Capture, Candy, Mine/Maze (3D engine), World, Potions, Games, Achieve.
 *  The heavy 3D engine (MazePanel/MineSim/Tabs from java-3d) is embedded as the Engine tab
 *  so nothing is lost; this shell adds the SwiftUI manager layer (capture battle, candy
 *  combos, potion brewing, music toggles, persisted store). Zero deps beyond the JDK. */
public final class SpookyApp {
    private final HalloweenUltimateManager mgr;
    private final GhostCapture capture;
    private final CandyCollection candy;
    private final PotionBrewing brew;

    public SpookyApp(HalloweenUltimateManager mgr, long seed) {
        this.mgr = mgr;
        this.capture = new GhostCapture(seed);
        this.candy = new CandyCollection(mgr);
        this.brew = new PotionBrewing(mgr, seed + 1);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HalloweenUltimateManager mgr = new HalloweenUltimateManager(SpookyStore.inUserHome());
            SpookyApp app = new SpookyApp(mgr, System.currentTimeMillis());
            JFrame f = new JFrame("Spooktacular Ultimate — Java Port");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.add(app.build());
            f.setSize(1100, 750);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }

    public JTabbedPane build() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Capture", capturePanel());
        tabs.addTab("Candy", candyPanel());
        tabs.addTab("Engine", new Tabs().build()); // Maze/Mine/World/Explore/Games/Achieve engine
        tabs.addTab("Mine 3D", new spooktacular.game.VoxelMinePanel());
        tabs.addTab("Potions", potionPanel());
        tabs.addTab("Music", musicPanel());
        tabs.addTab("Status", statusPanel());
        return tabs;
    }

    // ---- Capture tab: pick ghost, ZAP, throw net ----
    private JComponent capturePanel() {
        JPanel p = new JPanel(new BorderLayout());
        DefaultListModel<String> m = new DefaultListModel<>();
        List<Models.Ghost> ghosts = Models.starterGhosts();
        for (Models.Ghost g : ghosts) m.addElement(g.name() + " [" + g.rarity() + "] HP" + g.maxHp());
        JList<String> list = new JList<>(m);
        JLabel info = new JLabel("Pick a ghost, ZAP to weaken, then throw the net.", SwingConstants.CENTER);
        JPanel btns = new JPanel();
        JButton zap = new JButton("ZAP"), net = new JButton("Throw Net");
        final GhostCapture.Battle[] cur = { null };
        list.addListSelectionListener(e -> {
            int i = list.getSelectedIndex();
            if (i >= 0) { cur[0] = capture.start(ghosts.get(i)); info.setText(hpLine(cur[0])); }
        });
        zap.addActionListener(e -> {
            if (cur[0] == null) return;
            int d = capture.zap(cur[0]);
            SpookyMusic.shared().jingle("zap");
            info.setText("ZAP -" + d + "  " + hpLine(cur[0]));
        });
        net.addActionListener(e -> {
            if (cur[0] == null || cur[0].over) return;
            boolean ok = capture.throwNet(cur[0], false);
            if (ok) { mgr.captureGhost(cur[0].ghost); SpookyMusic.shared().bell(SpookyMusic.Bell.CAPTURE); info.setText("CAPTURED " + cur[0].ghost.name() + "! " + mgr.statusLine()); }
            else info.setText("It burst free! " + hpLine(cur[0]));
        });
        btns.add(zap); btns.add(net);
        p.add(new JScrollPane(list), BorderLayout.CENTER);
        p.add(info, BorderLayout.NORTH); p.add(btns, BorderLayout.SOUTH);
        return p;
    }

    private static String hpLine(GhostCapture.Battle b) {
        return b.ghost.name() + " HP " + b.hp + "/" + b.maxHp + " weakness " + (int) (b.weakness() * 100) + "%";
    }

    // ---- Candy tab ----
    private JComponent candyPanel() {
        JPanel p = new JPanel(new BorderLayout());
        DefaultListModel<String> m = new DefaultListModel<>();
        List<Models.CandyItem> cs = Models.starterCandies();
        for (Models.CandyItem c : cs) m.addElement(c.name() + " (" + c.type() + ")");
        JList<String> list = new JList<>(m);
        JLabel info = new JLabel(mgr.statusLine(), SwingConstants.CENTER);
        JButton take = new JButton("Collect");
        take.addActionListener(e -> {
            int i = list.getSelectedIndex();
            if (i < 0) return;
            candy.collect(cs.get(i), 1);
            SpookyMusic.shared().bell(SpookyMusic.Bell.CANDY);
            info.setText(mgr.statusLine() + " · combo " + candy.combo());
        });
        mgr.addListener(what -> info.setText(mgr.statusLine() + " · combo " + candy.combo()));
        p.add(new JScrollPane(list), BorderLayout.CENTER);
        p.add(info, BorderLayout.NORTH); p.add(take, BorderLayout.SOUTH);
        return p;
    }

    // ---- Potions tab ----
    private JComponent potionPanel() {
        JPanel p = new JPanel(new BorderLayout());
        JLabel info = new JLabel("Brew: first 2 starter candies -> potion. " + mgr.statusLine(), SwingConstants.CENTER);
        JTextArea log = new JTextArea(10, 40);
        log.setEditable(false);
        JButton brewBtn = new JButton("Brew Heal Draught");
        brewBtn.addActionListener(e -> {
            try {
                List<Models.CandyItem> ing = Models.starterCandies().subList(0, 2);
                Models.PotionItem potion = brew.brew(ing, Models.PotionEffect.HEAL);
                SpookyMusic.shared().bell(SpookyMusic.Bell.BREW);
                log.append("Brewed " + potion.name() + " [" + potion.quality() + "] potency " + potion.potency() + "\n");
                info.setText(mgr.statusLine());
            } catch (Exception ex) { log.append("Brew failed: " + ex.getMessage() + "\n"); }
        });
        p.add(info, BorderLayout.NORTH); p.add(new JScrollPane(log), BorderLayout.CENTER); p.add(brewBtn, BorderLayout.SOUTH);
        return p;
    }

    private JComponent musicPanel() {
        JPanel p = new JPanel(new GridLayout(0, 1));
        JCheckBox music = new JCheckBox("Music", SpookyMusic.shared().isMusicOn());
        JCheckBox sfx = new JCheckBox("SFX", SpookyMusic.shared().isSfxOn());
        music.addActionListener(e -> SpookyMusic.shared().setMusicOn(music.isSelected()));
        sfx.addActionListener(e -> SpookyMusic.shared().setSfxOn(sfx.isSelected()));
        p.add(new JLabel("SpookyMusic switchboard (SpookyMusic.swift port). Track: " + SpookyMusic.shared().currentTrack()));
        p.add(music); p.add(sfx);
        return p;
    }

    private JComponent statusPanel() {
        JPanel p = new JPanel(new BorderLayout());
        JLabel info = new JLabel(mgr.statusLine(), SwingConstants.CENTER);
        JTextArea quests = new JTextArea();
        quests.setEditable(false);
        for (Models.Quest q : Models.starterQuests())
            quests.append(q.title() + " [" + q.category() + "/" + q.difficulty() + "] " + q.objective() + "\n");
        mgr.addListener(what -> info.setText(mgr.statusLine()));
        p.add(info, BorderLayout.NORTH); p.add(new JScrollPane(quests), BorderLayout.CENTER);
        return p;
    }
}
